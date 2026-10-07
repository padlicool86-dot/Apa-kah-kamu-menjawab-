package com.example

import com.example.audio.NonCopyrightAudioPlayer
import com.example.data.GeminiChatService
import com.example.data.QuestRepository
import com.example.data.WorldInstitutionsRepository
import com.example.ui.QuestPlayUiState
import com.example.ui.UserStats
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testQuestQuestionsCountAndIntegrity() {
        val questions = QuestRepository.defaultQuestions
        assertTrue("Questions should not be empty", questions.isNotEmpty())
        questions.forEach { q ->
            assertTrue("Options should have at least 2 choices", q.options.size >= 2)
            assertTrue("CorrectIndex must be within options bounds", q.correctIndex in q.options.indices)
            assertNotNull("Question text must not be null", q.question)
            assertNotNull("Explanation must not be null", q.explanation)
        }
    }

    @Test
    fun testScoringSystemTracking() {
        val userStats = UserStats(
            totalScore = 1500,
            totalCorrectAnswers = 15,
            totalIncorrectAnswers = 5
        )
        assertEquals(1500, userStats.totalScore)
        assertEquals(15, userStats.totalCorrectAnswers)
        assertEquals(5, userStats.totalIncorrectAnswers)
        assertEquals(75, userStats.accuracyPercentage)

        val playState = QuestPlayUiState(
            score = 350,
            correctAnswersCount = 3,
            incorrectAnswersCount = 1
        )
        assertEquals(350, playState.score)
        assertEquals(3, playState.correctAnswersCount)
        assertEquals(1, playState.incorrectAnswersCount)
        assertEquals(75, playState.accuracyPercentage)
    }

    @Test
    fun testWorldInstitutionsSearch() {
        val pbbResults = WorldInstitutionsRepository.search("PBB")
        assertFalse("Should find PBB", pbbResults.isEmpty())
        assertEquals("PBB / UN", pbbResults.first().shortName)

        val nasaResults = WorldInstitutionsRepository.search("NASA")
        assertFalse("Should find NASA", nasaResults.isEmpty())
        assertEquals("NASA", nasaResults.first().shortName)

        val cernResults = WorldInstitutionsRepository.search("CERN")
        assertFalse("Should find CERN", cernResults.isEmpty())
        assertEquals("CERN", cernResults.first().shortName)
    }

    @Test
    fun testNonCopyrightAudioPlayerPlaylist() {
        val playlist = NonCopyrightAudioPlayer.playlist
        assertEquals(11, playlist.size)
        playlist.forEach { track ->
            assertTrue(track.durationSeconds > 0)
            assertTrue(track.licenseNotice.contains("Hak Cipta") || track.licenseNotice.contains("Royalty-Free"))
        }
    }

    @Test
    fun testDjTikTokQuestQuestions() {
        val djQuestions = QuestRepository.getQuestionsByCategory("DJ TikTok & Musik Viral")
        assertTrue("Should have DJ TikTok questions", djQuestions.isNotEmpty())
        assertEquals(5, djQuestions.size)
        assertTrue(djQuestions.any { it.question.contains("Malam Pagi") })
        assertTrue(djQuestions.any { it.question.contains("Karna Su Sayang") })
        assertTrue(djQuestions.any { it.question.contains("Dumes") })
    }

    @Test
    fun testGeminiChatbotService() = runBlocking {
        val response = GeminiChatService.sendMessage(emptyList(), "Ceritakan tentang keyboard")
        assertTrue(response.isSuccess)
        val text = response.getOrNull().orEmpty()
        assertTrue("Response should contain helpful trivia", text.isNotEmpty())
    }

    @Test
    fun testQuestSoundEffectsMethods() {
        assertTrue(com.example.audio.QuestSoundEffects.isSfxEnabled)
        com.example.audio.QuestSoundEffects.isSfxEnabled = false
        assertFalse(com.example.audio.QuestSoundEffects.isSfxEnabled)
        com.example.audio.QuestSoundEffects.isSfxEnabled = true
        com.example.audio.QuestSoundEffects.playCorrectAnswerSound()
        com.example.audio.QuestSoundEffects.playIncorrectAnswerSound()
        com.example.audio.QuestSoundEffects.playStreakBonusSound()
        com.example.audio.QuestSoundEffects.playQuestCompletedFanfare()
    }
}
