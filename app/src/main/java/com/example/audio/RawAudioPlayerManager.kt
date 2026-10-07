package com.example.audio

import android.content.Context
import android.media.MediaPlayer
import com.example.R

object RawAudioPlayerManager {
    private const val TAG = "RawAudioPlayerManager"
    var isEnabled: Boolean = true

    private fun safeLog(tag: String, msg: String) {
        try {
            android.util.Log.e(tag, msg)
        } catch (_: Throwable) {
            System.err.println("[$tag] $msg")
        }
    }

    /**
     * Plays sound effect using Android MediaPlayer from res/raw resource.
     */
    fun playRawSound(context: Context?, rawResId: Int) {
        if (!isEnabled || context == null) return
        try {
            val mediaPlayer = MediaPlayer.create(context, rawResId) ?: return
            mediaPlayer.setOnCompletionListener { mp ->
                try {
                    mp.stop()
                    mp.release()
                } catch (e: Throwable) {
                    safeLog(TAG, "Error releasing MediaPlayer: ${e.message}")
                }
            }
            mediaPlayer.setOnErrorListener { mp, _, _ ->
                try {
                    mp.release()
                } catch (_: Throwable) {}
                true
            }
            mediaPlayer.start()
        } catch (e: Throwable) {
            safeLog(TAG, "Error playing raw sound $rawResId: ${e.message}")
            // Fallback to procedural synth if MediaPlayer cannot initialize
            when (rawResId) {
                R.raw.sfx_correct -> QuestSoundEffects.playCorrectAnswerSound()
                R.raw.sfx_incorrect -> QuestSoundEffects.playIncorrectAnswerSound()
                R.raw.sfx_quiz_complete -> QuestSoundEffects.playQuestCompletedFanfare()
            }
        }
    }

    fun playCorrect(context: Context?) {
        playRawSound(context, R.raw.sfx_correct)
    }

    fun playIncorrect(context: Context?) {
        playRawSound(context, R.raw.sfx_incorrect)
    }

    fun playQuizComplete(context: Context?) {
        playRawSound(context, R.raw.sfx_quiz_complete)
    }
}
