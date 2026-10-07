package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object QuestSoundEffects {
    private const val TAG = "QuestSoundEffects"
    private const val SAMPLE_RATE = 22050
    private val scope = CoroutineScope(Dispatchers.Default)

    var isSfxEnabled: Boolean = true

    private fun safeLog(tag: String, msg: String) {
        try {
            android.util.Log.e(tag, msg)
        } catch (_: Throwable) {
            System.err.println("[$tag] $msg")
        }
    }

    /**
     * Plays a cheerful, copyright-free celebratory chime arpeggio
     * when the player selects the correct answer (C6 -> E6 -> G6 -> C7).
     */
    fun playCorrectAnswerSound() {
        if (!isSfxEnabled) return
        scope.launch {
            try {
                // Frequencies for a bright C major chord arpeggio
                val notes = doubleArrayOf(1046.50, 1318.51, 1567.98, 2093.00) // C6, E6, G6, C7
                val noteDurationSec = 0.09
                val noteSamples = (noteDurationSec * SAMPLE_RATE).toInt()
                val totalSamples = noteSamples * notes.size
                val pcmBuffer = ShortArray(totalSamples)

                for (nIndex in notes.indices) {
                    val freq = notes[nIndex]
                    val startIndex = nIndex * noteSamples
                    for (i in 0 until noteSamples) {
                        val t = i.toDouble() / SAMPLE_RATE
                        val env = exp(-t * 18.0) // quick bell decay
                        val sampleVal = sin(2.0 * PI * freq * t) * env
                        val pcmVal = (sampleVal.coerceIn(-0.95, 0.95) * Short.MAX_VALUE * 0.7).toInt()
                        pcmBuffer[startIndex + i] = pcmVal.toShort()
                    }
                }

                playPcmStream(pcmBuffer)
            } catch (e: Throwable) {
                safeLog(TAG, "Error playing correct answer sound: ${e.message}")
            }
        }
    }

    /**
     * Plays a special high-energy streak chime when the player maintains a combo streak.
     */
    fun playStreakBonusSound() {
        if (!isSfxEnabled) return
        scope.launch {
            try {
                // Rising pentatonic sparkle
                val notes = doubleArrayOf(1174.66, 1318.51, 1567.98, 1760.00, 2093.00, 2349.32)
                val noteDurationSec = 0.06
                val noteSamples = (noteDurationSec * SAMPLE_RATE).toInt()
                val totalSamples = noteSamples * notes.size
                val pcmBuffer = ShortArray(totalSamples)

                for (nIndex in notes.indices) {
                    val freq = notes[nIndex]
                    val startIndex = nIndex * noteSamples
                    for (i in 0 until noteSamples) {
                        val t = i.toDouble() / SAMPLE_RATE
                        val env = exp(-t * 22.0)
                        val sampleVal = (sin(2.0 * PI * freq * t) + 0.3 * sin(4.0 * PI * freq * t)) * env
                        val pcmVal = (sampleVal.coerceIn(-0.95, 0.95) * Short.MAX_VALUE * 0.75).toInt()
                        pcmBuffer[startIndex + i] = pcmVal.toShort()
                    }
                }

                playPcmStream(pcmBuffer)
            } catch (e: Throwable) {
                safeLog(TAG, "Error playing streak sound: ${e.message}")
            }
        }
    }

    /**
     * Plays a subtle low-frequency gentle thump for incorrect answers (A3 -> E3).
     */
    fun playIncorrectAnswerSound() {
        if (!isSfxEnabled) return
        scope.launch {
            try {
                val notes = doubleArrayOf(220.00, 164.81) // A3 -> E3
                val noteDurationSec = 0.12
                val noteSamples = (noteDurationSec * SAMPLE_RATE).toInt()
                val totalSamples = noteSamples * notes.size
                val pcmBuffer = ShortArray(totalSamples)

                for (nIndex in notes.indices) {
                    val freq = notes[nIndex]
                    val startIndex = nIndex * noteSamples
                    for (i in 0 until noteSamples) {
                        val t = i.toDouble() / SAMPLE_RATE
                        val env = exp(-t * 12.0)
                        val sampleVal = sin(2.0 * PI * freq * t) * env
                        val pcmVal = (sampleVal.coerceIn(-0.95, 0.95) * Short.MAX_VALUE * 0.45).toInt()
                        pcmBuffer[startIndex + i] = pcmVal.toShort()
                    }
                }

                playPcmStream(pcmBuffer)
            } catch (e: Throwable) {
                safeLog(TAG, "Error playing incorrect answer sound: ${e.message}")
            }
        }
    }

    /**
     * Plays an epic royal fanfare when a quiz session is completed!
     * 100% procedural non-copyright synthesizer composition.
     */
    fun playQuestCompletedFanfare() {
        if (!isSfxEnabled) return
        scope.launch {
            try {
                // Victory Fanfare: C5 -> E5 -> G5 -> C6 (held) -> E6 sparkle
                val notes = doubleArrayOf(
                    523.25, 659.25, 783.99, 1046.50, 1318.51, 1567.98, 2093.00
                )
                val durations = doubleArrayOf(0.12, 0.12, 0.12, 0.25, 0.15, 0.15, 0.50)
                var totalSamples = 0
                for (d in durations) totalSamples += (d * SAMPLE_RATE).toInt()

                val pcmBuffer = ShortArray(totalSamples)
                var offset = 0

                for (nIndex in notes.indices) {
                    val freq = notes[nIndex]
                    val dur = durations[nIndex]
                    val noteSamples = (dur * SAMPLE_RATE).toInt()

                    for (i in 0 until noteSamples) {
                        val t = i.toDouble() / SAMPLE_RATE
                        val decayRate = if (nIndex == notes.lastIndex) 3.5 else 9.0
                        val env = exp(-t * decayRate)
                        // Rich brass & bell harmony
                        val fundamental = sin(2.0 * PI * freq * t)
                        val overtone1 = 0.4 * sin(4.0 * PI * freq * t)
                        val overtone2 = 0.2 * sin(6.0 * PI * freq * t)
                        val mixed = (fundamental + overtone1 + overtone2) * env
                        val pcmVal = (mixed.coerceIn(-0.95, 0.95) * Short.MAX_VALUE * 0.75).toInt()
                        pcmBuffer[offset + i] = pcmVal.toShort()
                    }
                    offset += noteSamples
                }

                playPcmStream(pcmBuffer)
            } catch (e: Throwable) {
                safeLog(TAG, "Error playing victory fanfare: ${e.message}")
            }
        }
    }

    private fun playPcmStream(buffer: ShortArray) {
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            if (minBufferSize <= 0) return

            val sfxTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(maxOf(minBufferSize, buffer.size * 2))
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            sfxTrack.play()
            sfxTrack.write(buffer, 0, buffer.size)
            // Stop and release after write completes in stream mode
            scope.launch {
                try {
                    val sleepMs = ((buffer.size.toDouble() / SAMPLE_RATE) * 1000).toLong() + 100
                    kotlinx.coroutines.delay(sleepMs)
                    sfxTrack.stop()
                    sfxTrack.release()
                } catch (_: Throwable) {}
            }
        } catch (e: Throwable) {
            safeLog(TAG, "playPcmStream error: ${e.message}")
        }
    }
}
