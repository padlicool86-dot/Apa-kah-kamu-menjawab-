package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.MusicTrack
import com.example.audio.NonCopyrightAudioPlayer
import com.example.audio.PlayerState
import com.example.audio.QuestSoundEffects
import com.example.data.ChatMessage
import com.example.data.ChatSender
import com.example.data.GeminiChatService
import com.example.data.QuestQuestion
import com.example.data.QuestRepository
import com.example.data.WorldInstitution
import com.example.data.WorldInstitutionsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    QUEST_PLAY,
    LEMBAGA_EMAS,
    GEMINI_CHAT,
    CREATOR_PROFILE
}

data class QuestPlayUiState(
    val category: String = "Semua Quest",
    val questions: List<QuestQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswered: Boolean = false,
    val isCorrect: Boolean = false,
    val score: Int = 0,
    val streak: Int = 0,
    val maxStreak: Int = 0,
    val correctAnswersCount: Int = 0,
    val incorrectAnswersCount: Int = 0,
    val timeLeftSeconds: Int = 25,
    val isGameOver: Boolean = false,
    val geminiHint: String? = null,
    val isLoadingHint: Boolean = false,
    val showExplanation: Boolean = false
) {
    val accuracyPercentage: Int
        get() {
            val total = correctAnswersCount + incorrectAnswersCount
            return if (total > 0) ((correctAnswersCount.toFloat() / total) * 100).toInt() else 0
        }
}

data class UserStats(
    val totalScore: Int = 1250,
    val totalCorrectAnswers: Int = 12,
    val totalIncorrectAnswers: Int = 3,
    val goldenCoins: Int = 450,
    val masterRankTitle: String = "Quest Master Elit",
    val completedQuestsCount: Int = 14,
    val unlockedLembagaBadges: Int = 8
) {
    val accuracyPercentage: Int
        get() {
            val total = totalCorrectAnswers + totalIncorrectAnswers
            return if (total > 0) ((totalCorrectAnswers.toFloat() / total) * 100).toInt() else 100
        }
}

class QuestViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _questPlayState = MutableStateFlow(QuestPlayUiState())
    val questPlayState: StateFlow<QuestPlayUiState> = _questPlayState.asStateFlow()

    private val _userStats = MutableStateFlow(UserStats())
    val userStats: StateFlow<UserStats> = _userStats.asStateFlow()

    // Lembaga Emas search state
    private val _institutionSearchQuery = MutableStateFlow("")
    val institutionSearchQuery: StateFlow<String> = _institutionSearchQuery.asStateFlow()

    private val _institutionCategoryFilter = MutableStateFlow("Semua")
    val institutionCategoryFilter: StateFlow<String> = _institutionCategoryFilter.asStateFlow()

    private val _selectedInstitutionDetail = MutableStateFlow<WorldInstitution?>(null)
    val selectedInstitutionDetail: StateFlow<WorldInstitution?> = _selectedInstitutionDetail.asStateFlow()

    // Audio player modal sheet
    private val _showMusicSheet = MutableStateFlow(false)
    val showMusicSheet: StateFlow<Boolean> = _showMusicSheet.asStateFlow()

    val audioPlayerState: StateFlow<PlayerState> = NonCopyrightAudioPlayer.playerState

    // Sound effects state
    private val _isSfxEnabled = MutableStateFlow(QuestSoundEffects.isSfxEnabled)
    val isSfxEnabled: StateFlow<Boolean> = _isSfxEnabled.asStateFlow()

    fun toggleSfx() {
        val nextState = !_isSfxEnabled.value
        _isSfxEnabled.value = nextState
        QuestSoundEffects.isSfxEnabled = nextState
    }

    // Multi-turn Gemini Chatbot State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = ChatSender.GEMINI_BOT,
                text = "✨ Halo Penjelajah! Saya adalah Gemini Quest Master & Asisten Lembaga Emas. Ada hal seputar tombol keyboard, lembaga dunia (PBB, NASA, CERN, dll.), atau teka-teki logika yang ingin kamu tanyakan atau uji bersama saya?"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    private val _chatInputText = MutableStateFlow("")
    val chatInputText: StateFlow<String> = _chatInputText.asStateFlow()

    private var timerJob: Job? = null

    init {
        // Start playing the non-copyright track softly so user enjoys the requested background music right away
        NonCopyrightAudioPlayer.play()
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setShowMusicSheet(show: Boolean) {
        _showMusicSheet.value = show
    }

    // Audio controls
    fun togglePlayPause() = NonCopyrightAudioPlayer.togglePlayPause()
    fun nextTrack() = NonCopyrightAudioPlayer.nextTrack()
    fun previousTrack() = NonCopyrightAudioPlayer.previousTrack()
    fun playTrack(track: MusicTrack) = NonCopyrightAudioPlayer.playTrack(track)
    fun setVolume(volume: Float) = NonCopyrightAudioPlayer.setVolume(volume)
    fun seekTo(seconds: Int) = NonCopyrightAudioPlayer.seekTo(seconds)

    // Lembaga Emas search
    fun updateInstitutionSearchQuery(query: String) {
        _institutionSearchQuery.value = query
    }

    fun updateInstitutionCategoryFilter(category: String) {
        _institutionCategoryFilter.value = category
    }

    fun selectInstitutionDetail(institution: WorldInstitution?) {
        _selectedInstitutionDetail.value = institution
    }

    fun getFilteredInstitutions(): List<WorldInstitution> {
        return WorldInstitutionsRepository.search(
            _institutionSearchQuery.value,
            _institutionCategoryFilter.value
        )
    }

    // Gemini Chatbot Actions
    fun updateChatInput(text: String) {
        _chatInputText.value = text
    }

    fun sendChatMessage(presetText: String? = null) {
        val messageText = (presetText ?: _chatInputText.value).trim()
        if (messageText.isEmpty() || _isChatLoading.value) return

        val userMsg = ChatMessage(sender = ChatSender.USER, text = messageText)
        val currentHistory = _chatMessages.value
        _chatMessages.value = currentHistory + userMsg
        _chatInputText.value = ""
        _isChatLoading.value = true

        viewModelScope.launch {
            val result = GeminiChatService.sendMessage(currentHistory, messageText)
            val replyText = result.getOrElse {
                "💡 Gemini Quest Master: Menarik sekali! Mari kita perdalam pembahasan ini berdasarkan arsip Lembaga Pembuat dan data dunia terverifikasi."
            }
            _chatMessages.value = _chatMessages.value + ChatMessage(sender = ChatSender.GEMINI_BOT, text = replyText)
            _isChatLoading.value = false
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                sender = ChatSender.GEMINI_BOT,
                text = "Obrolan telah disegarkan. Tanyakan apa saja tentang quest, keyboard, atau sejarah lembaga dunia!"
            )
        )
    }

    // Quest Gameplay
    fun startQuest(category: String = "Semua Quest") {
        val qList = QuestRepository.getQuestionsByCategory(category).shuffled()
        _questPlayState.value = QuestPlayUiState(
            category = category,
            questions = if (qList.isEmpty()) QuestRepository.defaultQuestions else qList,
            currentIndex = 0,
            score = 0,
            streak = 0,
            correctAnswersCount = 0,
            incorrectAnswersCount = 0,
            timeLeftSeconds = 25,
            isGameOver = false
        )
        _currentScreen.value = AppScreen.QUEST_PLAY
        startQuestionTimer()
    }

    private fun startQuestionTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && !_questPlayState.value.isAnswered && !_questPlayState.value.isGameOver) {
                delay(1000)
                val timeLeft = _questPlayState.value.timeLeftSeconds
                if (timeLeft <= 1) {
                    // Time's up: count as incorrect
                    submitAnswer(-1)
                    break
                } else {
                    _questPlayState.value = _questPlayState.value.copy(timeLeftSeconds = timeLeft - 1)
                }
            }
        }
    }

    fun submitAnswer(optionIndex: Int) {
        if (_questPlayState.value.isAnswered) return
        timerJob?.cancel()

        val state = _questPlayState.value
        val currentQ = state.questions.getOrNull(state.currentIndex) ?: return
        val isCorrect = optionIndex == currentQ.correctIndex

        val speedBonus = state.timeLeftSeconds * 4
        val streakBonus = if (isCorrect) (state.streak + 1) * 15 else 0
        val earnedScore = if (isCorrect) 100 + speedBonus + streakBonus else 0

        val newStreak = if (isCorrect) state.streak + 1 else 0
        val newMaxStreak = maxOf(state.maxStreak, newStreak)
        val newCorrectCount = if (isCorrect) state.correctAnswersCount + 1 else state.correctAnswersCount
        val newIncorrectCount = if (!isCorrect) state.incorrectAnswersCount + 1 else state.incorrectAnswersCount
        val newScore = state.score + earnedScore

        _questPlayState.value = state.copy(
            selectedOptionIndex = optionIndex,
            isAnswered = true,
            isCorrect = isCorrect,
            score = newScore,
            streak = newStreak,
            maxStreak = newMaxStreak,
            correctAnswersCount = newCorrectCount,
            incorrectAnswersCount = newIncorrectCount,
            showExplanation = true
        )

        // Award golden coins & update lifetime user stats (correct & incorrect tracking)
        _userStats.value = _userStats.value.copy(
            totalScore = _userStats.value.totalScore + earnedScore,
            totalCorrectAnswers = if (isCorrect) _userStats.value.totalCorrectAnswers + 1 else _userStats.value.totalCorrectAnswers,
            totalIncorrectAnswers = if (!isCorrect) _userStats.value.totalIncorrectAnswers + 1 else _userStats.value.totalIncorrectAnswers,
            goldenCoins = if (isCorrect) _userStats.value.goldenCoins + 25 else _userStats.value.goldenCoins
        )

        // Trigger procedural non-copyright sound effects
        if (isCorrect) {
            if (newStreak >= 2) {
                QuestSoundEffects.playStreakBonusSound()
            } else {
                QuestSoundEffects.playCorrectAnswerSound()
            }
        } else {
            QuestSoundEffects.playIncorrectAnswerSound()
        }
    }

    fun dismissExplanation() {
        _questPlayState.value = _questPlayState.value.copy(showExplanation = false)
    }

    fun nextQuestion() {
        val state = _questPlayState.value
        if (state.currentIndex + 1 >= state.questions.size) {
            // Game over / Session completed: Play celebratory victory fanfare!
            QuestSoundEffects.playQuestCompletedFanfare()
            _questPlayState.value = state.copy(
                isGameOver = true,
                showExplanation = false
            )
            _userStats.value = _userStats.value.copy(
                completedQuestsCount = _userStats.value.completedQuestsCount + 1
            )
        } else {
            _questPlayState.value = state.copy(
                currentIndex = state.currentIndex + 1,
                selectedOptionIndex = null,
                isAnswered = false,
                isCorrect = false,
                timeLeftSeconds = 25,
                geminiHint = null,
                isLoadingHint = false,
                showExplanation = false
            )
            startQuestionTimer()
        }
    }

    fun requestGeminiHint() {
        val state = _questPlayState.value
        val currentQ = state.questions.getOrNull(state.currentIndex) ?: return
        if (state.isLoadingHint || state.geminiHint != null) return

        _questPlayState.value = state.copy(isLoadingHint = true)
        viewModelScope.launch {
            val hint = QuestRepository.getGeminiHint(currentQ)
            _questPlayState.value = _questPlayState.value.copy(
                geminiHint = hint,
                isLoadingHint = false
            )
        }
    }

    fun resetAppState() {
        _questPlayState.value = QuestPlayUiState()
        _userStats.value = UserStats(
            totalScore = 0,
            totalCorrectAnswers = 0,
            totalIncorrectAnswers = 0,
            goldenCoins = 0,
            masterRankTitle = "Pemula Quest"
        )
        clearChat()
        NonCopyrightAudioPlayer.playTrack(NonCopyrightAudioPlayer.playlist[0])
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
