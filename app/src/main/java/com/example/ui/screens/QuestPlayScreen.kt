package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.QuestQuestion
import com.example.ui.AppScreen
import com.example.ui.QuestPlayUiState
import com.example.ui.QuestViewModel
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.CyberNavyElevated
import com.example.ui.theme.CyberNavySurface
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FadllyGoldBlueOrangeBrush
import com.example.ui.theme.GeminiGoldBrush
import com.example.ui.theme.LembagaEmasBrush
import com.example.ui.theme.NeonOrangePrimary
import com.example.ui.theme.PureGold
import com.example.ui.theme.QuestErrorRed
import com.example.ui.theme.QuestSuccessGreen
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibrantOrange

@Composable
fun QuestPlayScreen(
    viewModel: QuestViewModel,
    playState: QuestPlayUiState,
    modifier: Modifier = Modifier
) {
    if (playState.isGameOver) {
        QuestGameOverView(
            playState = playState,
            onRestart = { viewModel.startQuest(playState.category) },
            onBackHome = { viewModel.navigateTo(AppScreen.HOME) },
            modifier = modifier
        )
        return
    }

    val currentQ = playState.questions.getOrNull(playState.currentIndex)
    if (currentQ == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = VibrantOrange)
        }
        return
    }

    // Explanation Dialog upon answering
    if (playState.showExplanation) {
        QuestExplanationDialog(
            isCorrect = playState.isCorrect,
            question = currentQ,
            onDismiss = { viewModel.dismissExplanation() },
            onNext = { viewModel.nextQuestion() }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Top Bar: Back, Category title, Streak
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("quest_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = TextPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "QUEST INTERAKTIF",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = VibrantOrange,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Bisa Kah Kamu Menjawab?!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val isSfxEnabled by viewModel.isSfxEnabled.collectAsStateWithLifecycle()
                    IconButton(
                        onClick = { viewModel.toggleSfx() },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("toggle_sfx_button")
                    ) {
                        Icon(
                            imageVector = if (isSfxEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = if (isSfxEnabled) "SFX Aktif" else "SFX Mati",
                            tint = if (isSfxEnabled) PureGold else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Streak counter pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (playState.streak > 0) Color(0x33FF6B35) else Color(0x22FFFFFF))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = if (playState.streak > 0) NeonOrangePrimary else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${playState.streak}x",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (playState.streak > 0) NeonOrangePrimary else TextSecondary
                        )
                    }
                }
            }
        }

        // 2. Progress & Live Scoreboard Bar (Tracks correct, incorrect, score, timer)
        item {
            QuestProgressBar(
                currentIndex = playState.currentIndex,
                totalQuestions = playState.questions.size,
                score = playState.score,
                correctCount = playState.correctAnswersCount,
                incorrectCount = playState.incorrectAnswersCount,
                accuracy = playState.accuracyPercentage,
                timeLeftSeconds = playState.timeLeftSeconds
            )
        }

        // 3. Question Card
        item {
            QuestionCard(question = currentQ)
        }

        // 4. Gemini Hint Banner / Request Button
        item {
            GeminiHintSection(
                geminiHint = playState.geminiHint,
                isLoadingHint = playState.isLoadingHint,
                onRequestHint = { viewModel.requestGeminiHint() }
            )
        }

        // 5. Four Answer Options
        items(currentQ.options.size) { index ->
            val optionText = currentQ.options[index]
            OptionButton(
                index = index,
                text = optionText,
                isSelected = playState.selectedOptionIndex == index,
                isCorrect = index == currentQ.correctIndex,
                isAnswered = playState.isAnswered,
                onClick = { viewModel.submitAnswer(index) }
            )
        }

        // Bottom padding to clear Spotify mini player
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

/**
 * Live Quest Scoreboard Bar tracking:
 * - Current Total Score
 * - Correct Answers
 * - Incorrect Answers
 * - Accuracy Rate
 * - Countdown Timer
 */
@Composable
fun QuestProgressBar(
    currentIndex: Int,
    totalQuestions: Int,
    score: Int,
    correctCount: Int,
    incorrectCount: Int,
    accuracy: Int,
    timeLeftSeconds: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("quest_progress_bar"),
        colors = CardDefaults.cardColors(containerColor = CyberNavyElevated.copy(alpha = 0.92f)),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3300B4D8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Top Row: Question number, timer, live score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Soal ${currentIndex + 1} / $totalQuestions",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan
                )

                // Timer Countdown
                val timerColor = when {
                    timeLeftSeconds > 12 -> QuestSuccessGreen
                    timeLeftSeconds > 6 -> VibrantOrange
                    else -> QuestErrorRed
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = timerColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${timeLeftSeconds}s",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = timerColor
                    )
                }

                // Live Session Score
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = null,
                        tint = PureGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Skor: $score",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = PureGold,
                        modifier = Modifier.testTag("live_score_text")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Timer progress bar
            val timerFraction = (timeLeftSeconds / 25f).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { timerFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = when {
                    timeLeftSeconds > 12 -> ElectricCyan
                    timeLeftSeconds > 6 -> VibrantOrange
                    else -> QuestErrorRed
                },
                trackColor = Color(0x22FFFFFF)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Sub-Row: Correct vs Incorrect answers live counter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberNavySurface)
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Correct Answers Counter
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Benar",
                        tint = QuestSuccessGreen,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$correctCount Benar",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuestSuccessGreen,
                        modifier = Modifier.testTag("live_correct_count")
                    )
                }

                // Incorrect Answers Counter
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Salah",
                        tint = QuestErrorRed,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$incorrectCount Salah",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuestErrorRed,
                        modifier = Modifier.testTag("live_incorrect_count")
                    )
                }

                // Accuracy Indicator
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrackChanges,
                        contentDescription = "Akurasi",
                        tint = ElectricCyan,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$accuracy% Akurasi",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        modifier = Modifier.testTag("live_accuracy_text")
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionCard(question: QuestQuestion) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.5.dp,
                Brush.linearGradient(listOf(ElectricCyan, NeonOrangePrimary)),
                RoundedCornerShape(20.dp)
            )
            .testTag("question_card"),
        colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Badges row: Creator & Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF132A4D))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = question.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x22FFD700))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Tingkat: ${question.difficulty}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PureGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // The Question Text
            Text(
                text = question.question,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                lineHeight = 23.sp
            )

            // Optional Keyboard / Hardware Trivia hint
            if (question.keyboardTrivia != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0A1F3B))
                        .border(1.dp, Color(0x3300B4D8), RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Keyboard,
                        contentDescription = "Keyboard Logo",
                        tint = VibrantOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = question.keyboardTrivia,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun GeminiHintSection(
    geminiHint: String?,
    isLoadingHint: Boolean,
    onRequestHint: () -> Unit
) {
    if (geminiHint != null) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF221A05)),
            border = androidx.compose.foundation.BorderStroke(1.dp, PureGold),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Hint",
                    tint = PureGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Petunjuk Gemini AI:",
                        style = TextStyle(
                            brush = GeminiGoldBrush,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Text(
                        text = geminiHint,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    } else {
        OutlinedButton(
            onClick = onRequestHint,
            enabled = !isLoadingHint,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("request_gemini_hint_button"),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = PureGold
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, PureGold.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoadingHint) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = PureGold,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Menghubungkan ke Gemini...", fontSize = 12.sp)
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Gemini",
                    tint = PureGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Minta Petunjuk Gemini AI (Gratis)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureGold
                )
            }
        }
    }
}

@Composable
fun OptionButton(
    index: Int,
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isAnswered: Boolean,
    onClick: () -> Unit
) {
    val optionLetters = listOf("A", "B", "C", "D")
    val letter = optionLetters.getOrElse(index) { "${index + 1}" }

    // Color feedback
    val (containerBg, borderBrush, textColor) = when {
        isAnswered && isCorrect -> Triple(
            Color(0x3310B981),
            Brush.linearGradient(listOf(QuestSuccessGreen, QuestSuccessGreen)),
            QuestSuccessGreen
        )
        isAnswered && isSelected && !isCorrect -> Triple(
            Color(0x33EF4444),
            Brush.linearGradient(listOf(QuestErrorRed, QuestErrorRed)),
            QuestErrorRed
        )
        isSelected -> Triple(
            Color(0x33FF6B35),
            Brush.linearGradient(listOf(VibrantOrange, ElectricCyan)),
            VibrantOrange
        )
        else -> Triple(
            CyberNavyElevated.copy(alpha = 0.85f),
            Brush.linearGradient(listOf(Color(0x3300B4D8), Color(0x33FF6B35))),
            TextPrimary
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = !isAnswered) { onClick() }
            .testTag("option_button_$index"),
        colors = CardDefaults.cardColors(containerColor = containerBg),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, borderBrush),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Letter Badge
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isAnswered && isCorrect -> QuestSuccessGreen
                            isAnswered && isSelected && !isCorrect -> QuestErrorRed
                            else -> Color(0xFF132A4D)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isAnswered && (isCorrect || isSelected)) CyberNavyDark else ElectricCyan
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                modifier = Modifier.weight(1f)
            )

            if (isAnswered) {
                if (isCorrect) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Benar",
                        tint = QuestSuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                } else if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Salah",
                        tint = QuestErrorRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuestExplanationDialog(
    isCorrect: Boolean,
    question: QuestQuestion,
    onDismiss: () -> Unit,
    onNext: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .border(
                    2.dp,
                    if (isCorrect) QuestSuccessGreen else QuestErrorRed,
                    RoundedCornerShape(22.dp)
                )
                .testTag("quest_explanation_dialog"),
            colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Error,
                    contentDescription = null,
                    tint = if (isCorrect) QuestSuccessGreen else QuestErrorRed,
                    modifier = Modifier.size(54.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isCorrect) "Jawaban Kamu Tepat Sekali!" else "Jawaban Belum Tepat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isCorrect) QuestSuccessGreen else QuestErrorRed
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Verification Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F264A))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = PureGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Terverifikasi oleh Lembaga Pembuat & Gemini",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PureGold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Explanation Text
                Text(
                    text = question.explanation,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("next_question_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Lanjut Soal Berikutnya",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberNavyDark
                    )
                }
            }
        }
    }
}

/**
 * Game Over screen displaying comprehensive trivia score tracking:
 * - Session Score
 * - Correct Answers
 * - Incorrect Answers
 * - Accuracy Rate
 * - Max Streak
 * - Golden Coins
 */
@Composable
fun QuestGameOverView(
    playState: QuestPlayUiState,
    onRestart: () -> Unit,
    onBackHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .border(2.dp, FadllyGoldBlueOrangeBrush, RoundedCornerShape(26.dp))
                .testTag("game_over_card"),
            colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
            shape = RoundedCornerShape(26.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = PureGold,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "QUEST SELESAI!",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonOrangePrimary,
                    letterSpacing = 1.5.sp
                )

                Text(
                    text = "Bisa Kah Kamu Menjawab?!",
                    style = TextStyle(
                        brush = FadllyGoldBlueOrangeBrush,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Scoreboard Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0C192E)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ScoreRow(label = "Skor Sesi Ini", value = "${playState.score} Poin", color = PureGold)
                        ScoreRow(
                            label = "Jawaban Benar",
                            value = "${playState.correctAnswersCount} Soal",
                            color = QuestSuccessGreen
                        )
                        ScoreRow(
                            label = "Jawaban Salah",
                            value = "${playState.incorrectAnswersCount} Soal",
                            color = QuestErrorRed
                        )
                        ScoreRow(
                            label = "Tingkat Akurasi",
                            value = "${playState.accuracyPercentage}%",
                            color = ElectricCyan
                        )
                        ScoreRow(
                            label = "Kombo Streak Tertinggi",
                            value = "${playState.maxStreak}x",
                            color = NeonOrangePrimary
                        )
                        ScoreRow(
                            label = "Koin Lembaga Didapat",
                            value = "+${playState.correctAnswersCount * 25} Koin",
                            color = RadiantGold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("restart_quest_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = CyberNavyDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mainkan Lagi",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberNavyDark
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onBackHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("back_to_home_button"),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Kembali ke Beranda",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
