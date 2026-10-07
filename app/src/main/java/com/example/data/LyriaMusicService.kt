package com.example.data

import android.content.Context
import android.util.Base64
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class GeneratedMusicTrack(
    val title: String,
    val prompt: String,
    val model: String,
    val durationLabel: String,
    val audioFilePath: String?,
    val isSampleFallback: Boolean = false
)

object LyriaMusicService {
    private const val TAG = "LyriaMusicService"
    const val MODEL_CLIP = "lyria-3-clip-preview" // for short clips up to 30s
    const val MODEL_PRO = "lyria-3-pro-preview"   // for full-length tracks

    private fun safeLog(tag: String, msg: String) {
        try {
            android.util.Log.e(tag, msg)
        } catch (_: Throwable) {
            System.err.println("[$tag] $msg")
        }
    }

    suspend fun generateMusic(
        context: Context?,
        prompt: String,
        useFullLengthPro: Boolean = false
    ): Result<GeneratedMusicTrack> = withContext(Dispatchers.IO) {
        val selectedModel = if (useFullLengthPro) MODEL_PRO else MODEL_CLIP
        val durationLabel = if (useFullLengthPro) "Full Track (~2:30)" else "Klip Musik (30 Detik)"
        val apiKey = BuildConfig.GEMINI_API_KEY

        if (apiKey.isNullOrEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // Intelligent fallback: generates procedural synthesized audio file in cacheDir
            val fallbackPath = generateProceduralSampleFile(context, prompt)
            return@withContext Result.success(
                GeneratedMusicTrack(
                    title = "AI Lyria: ${prompt.take(24)}...",
                    prompt = prompt,
                    model = selectedModel,
                    durationLabel = durationLabel,
                    audioFilePath = fallbackPath,
                    isSampleFallback = true
                )
            )
        }

        try {
            val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/$selectedModel:generateContent?key=$apiKey"
            val url = URL(endpoint)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 60000
            conn.readTimeout = 60000

            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseModalities", JSONArray().apply {
                        put("AUDIO")
                    })
                })
            }

            OutputStreamWriter(conn.outputStream).use { writer ->
                writer.write(requestJson.toString())
                writer.flush()
            }

            if (conn.responseCode == 200) {
                BufferedReader(InputStreamReader(conn.inputStream)).use { reader ->
                    val responseStr = reader.readText()
                    val responseJson = JSONObject(responseStr)
                    val candidates = responseJson.optJSONArray("candidates")
                    val firstCandidate = candidates?.optJSONObject(0)
                    val content = firstCandidate?.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")

                    var audioBase64: String? = null
                    var mimeType = "audio/mp3"

                    if (parts != null) {
                        for (i in 0 until parts.length()) {
                            val part = parts.getJSONObject(i)
                            if (part.has("inlineData")) {
                                val inlineData = part.getJSONObject("inlineData")
                                audioBase64 = inlineData.optString("data")
                                mimeType = inlineData.optString("mimeType", "audio/mp3")
                                break
                            }
                        }
                    }

                    if (!audioBase64.isNullOrEmpty() && context != null) {
                        val extension = if (mimeType.contains("wav")) "wav" else "mp3"
                        val audioFile = File(context.cacheDir, "lyria_${System.currentTimeMillis()}.$extension")
                        val bytes = Base64.decode(audioBase64, Base64.DEFAULT)
                        FileOutputStream(audioFile).use { it.write(bytes) }

                        return@withContext Result.success(
                            GeneratedMusicTrack(
                                title = "Lyria AI: ${prompt.take(24)}...",
                                prompt = prompt,
                                model = selectedModel,
                                durationLabel = durationLabel,
                                audioFilePath = audioFile.absolutePath,
                                isSampleFallback = false
                            )
                        )
                    }
                }
            }

            // Fallback if API returned text or non-audio
            val fallbackPath = generateProceduralSampleFile(context, prompt)
            Result.success(
                GeneratedMusicTrack(
                    title = "Lyria AI: ${prompt.take(24)}...",
                    prompt = prompt,
                    model = selectedModel,
                    durationLabel = durationLabel,
                    audioFilePath = fallbackPath,
                    isSampleFallback = true
                )
            )
        } catch (e: Throwable) {
            safeLog(TAG, "Lyria generation exception: ${e.message}")
            val fallbackPath = generateProceduralSampleFile(context, prompt)
            Result.success(
                GeneratedMusicTrack(
                    title = "Lyria AI: ${prompt.take(24)}...",
                    prompt = prompt,
                    model = selectedModel,
                    durationLabel = durationLabel,
                    audioFilePath = fallbackPath,
                    isSampleFallback = true
                )
            )
        }
    }

    private fun generateProceduralSampleFile(context: Context?, prompt: String): String? {
        if (context == null) return null
        return try {
            val audioFile = File(context.cacheDir, "lyria_preview_${System.currentTimeMillis()}.wav")
            val sampleRate = 22050
            val durationSec = 10
            val totalSamples = sampleRate * durationSec
            val pcm = ShortArray(totalSamples)

            val baseFreq = when {
                prompt.contains("cyber", ignoreCase = true) || prompt.contains("synth", ignoreCase = true) -> 130.81
                prompt.contains("lofi", ignoreCase = true) || prompt.contains("chill", ignoreCase = true) -> 146.83
                else -> 174.61
            }

            for (i in 0 until totalSamples) {
                val t = i.toDouble() / sampleRate
                val beat = (t * 2.0) % 1.0
                val bass = Math.sin(2.0 * Math.PI * baseFreq * t)
                val melody = Math.sin(2.0 * Math.PI * (baseFreq * 2.5) * t) * 0.4
                val kick = Math.sin(2.0 * Math.PI * 60.0 * t) * Math.exp(-beat * 8.0) * 0.6
                val mix = (bass * 0.4 + melody + kick).coerceIn(-0.9, 0.9)
                pcm[i] = (mix * Short.MAX_VALUE).toInt().toShort()
            }

            // Write simple WAV RIFF header
            FileOutputStream(audioFile).use { fos ->
                val byteRate = sampleRate * 2
                val dataSize = totalSamples * 2
                val totalSize = 36 + dataSize
                val header = ByteArray(44)
                header[0] = 'R'.code.toByte(); header[1] = 'I'.code.toByte(); header[2] = 'F'.code.toByte(); header[3] = 'F'.code.toByte()
                header[4] = (totalSize and 0xff).toByte(); header[5] = ((totalSize shr 8) and 0xff).toByte()
                header[6] = ((totalSize shr 16) and 0xff).toByte(); header[7] = ((totalSize shr 24) and 0xff).toByte()
                header[8] = 'W'.code.toByte(); header[9] = 'A'.code.toByte(); header[10] = 'V'.code.toByte(); header[11] = 'E'.code.toByte()
                header[12] = 'f'.code.toByte(); header[13] = 'm'.code.toByte(); header[14] = 't'.code.toByte(); header[15] = ' '.code.toByte()
                header[16] = 16; header[17] = 0; header[18] = 0; header[19] = 0 // subchunk1size (16 for PCM)
                header[20] = 1; header[21] = 0 // AudioFormat (1 = PCM)
                header[22] = 1; header[23] = 0 // NumChannels (1 = Mono)
                header[24] = (sampleRate and 0xff).toByte(); header[25] = ((sampleRate shr 8) and 0xff).toByte()
                header[26] = ((sampleRate shr 16) and 0xff).toByte(); header[27] = ((sampleRate shr 24) and 0xff).toByte()
                header[28] = (byteRate and 0xff).toByte(); header[29] = ((byteRate shr 8) and 0xff).toByte()
                header[30] = ((byteRate shr 16) and 0xff).toByte(); header[31] = ((byteRate shr 24) and 0xff).toByte()
                header[32] = 2; header[33] = 0 // block align (1 * 16 / 8 = 2)
                header[34] = 16; header[35] = 0 // bits per sample
                header[36] = 'd'.code.toByte(); header[37] = 'a'.code.toByte(); header[38] = 't'.code.toByte(); header[39] = 'a'.code.toByte()
                header[40] = (dataSize and 0xff).toByte(); header[41] = ((dataSize shr 8) and 0xff).toByte()
                header[42] = ((dataSize shr 16) and 0xff).toByte(); header[43] = ((dataSize shr 24) and 0xff).toByte()
                fos.write(header)

                val bytes = ByteArray(dataSize)
                for (j in 0 until totalSamples) {
                    val v = pcm[j].toInt()
                    bytes[j * 2] = (v and 0xff).toByte()
                    bytes[j * 2 + 1] = ((v shr 8) and 0xff).toByte()
                }
                fos.write(bytes)
            }
            audioFile.absolutePath
        } catch (e: Throwable) {
            safeLog(TAG, "Error writing fallback sample: ${e.message}")
            null
        }
    }
}
