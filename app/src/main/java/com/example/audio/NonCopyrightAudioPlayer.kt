package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

data class MusicTrack(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSeconds: Int,
    val mood: String,
    val licenseNotice: String,
    val baseBpm: Double,
    val trackType: Int // 0: LoFi, 1: Synthwave, 2: 8-Bit, 3: Ambient, 4: DJ TikTok Jedag-Jedug
)

data class PlayerState(
    val currentTrack: MusicTrack,
    val isPlaying: Boolean = false,
    val currentPositionSeconds: Int = 0,
    val volume: Float = 0.85f,
    val isShuffle: Boolean = false,
    val isRepeat: Boolean = true,
    val visualizerFrequencies: List<Float> = List(16) { 0.2f }
)

object NonCopyrightAudioPlayer {
    private const val TAG = "NonCopyrightAudioPlayer"
    private const val SAMPLE_RATE = 22050

    val playlist = listOf(
        // DJ TikTok Terbaru 2023 Tracks from User Request Video
        MusicTrack(
            id = "track_dj_1",
            title = "01. DJ Malam Pagi x Hamil Duluan",
            artist = "DJ TikTok Viral 2023 (Quest Mix)",
            album = "DJ TikTok Terbaru 2023",
            durationSeconds = 215,
            mood = "Jedag-Jedug Hype",
            licenseNotice = "100% Bebas Hak Cipta (Royalty-Free Procedural Remix)",
            baseBpm = 130.0,
            trackType = 4
        ),
        MusicTrack(
            id = "track_dj_2",
            title = "02. DJ Karna Su Sayang",
            artist = "DJ TikTok Viral 2023 (Bass Drop)",
            album = "DJ TikTok Terbaru 2023",
            durationSeconds = 205,
            mood = "Melodi Manis & Bass Mantap",
            licenseNotice = "100% Bebas Hak Cipta (Royalty-Free Procedural Remix)",
            baseBpm = 126.0,
            trackType = 4
        ),
        MusicTrack(
            id = "track_dj_3",
            title = "03. DJ Dumes",
            artist = "DJ TikTok Viral 2023 (Koplo Bounce)",
            album = "DJ TikTok Terbaru 2023",
            durationSeconds = 195,
            mood = "Koplo Jedag-Jedug",
            licenseNotice = "100% Bebas Hak Cipta (Royalty-Free Procedural Remix)",
            baseBpm = 132.0,
            trackType = 4
        ),
        MusicTrack(
            id = "track_dj_4",
            title = "04. DJ Kisinan",
            artist = "DJ TikTok Viral 2023 (Slowed Bass)",
            album = "DJ TikTok Terbaru 2023",
            durationSeconds = 185,
            mood = "Galau Jedag-Jedug",
            licenseNotice = "100% Bebas Hak Cipta (Royalty-Free Procedural Remix)",
            baseBpm = 128.0,
            trackType = 4
        ),
        MusicTrack(
            id = "track_dj_5",
            title = "05. DJ Takdir Tuhan Takkan Salah",
            artist = "DJ TikTok Viral 2023 (Emosional Beat)",
            album = "DJ TikTok Terbaru 2023",
            durationSeconds = 210,
            mood = "Emosional & Semangat",
            licenseNotice = "100% Bebas Hak Cipta (Royalty-Free Procedural Remix)",
            baseBpm = 125.0,
            trackType = 4
        ),
        MusicTrack(
            id = "track_dj_6",
            title = "06. DJ Tak Ingin Lagi",
            artist = "DJ TikTok Viral 2023 (Club Mix)",
            album = "DJ TikTok Terbaru 2023",
            durationSeconds = 190,
            mood = "Enerjik & Ceria",
            licenseNotice = "100% Bebas Hak Cipta (Royalty-Free Procedural Remix)",
            baseBpm = 130.0,
            trackType = 4
        ),
        MusicTrack(
            id = "track_dj_7",
            title = "07. DJ Aku Bukan Dia",
            artist = "DJ TikTok Viral 2023 (Jedag Jedug)",
            album = "DJ TikTok Terbaru 2023",
            durationSeconds = 180,
            mood = "Deep Bass Reverb",
            licenseNotice = "100% Bebas Hak Cipta (Royalty-Free Procedural Remix)",
            baseBpm = 128.0,
            trackType = 4
        ),

        // Original Fadlly & Lembaga Emas Cyber Themes
        MusicTrack(
            id = "track_1",
            title = "Bisa Menjawab?! Lo-Fi Cyber",
            artist = "Fadlly Beats x Lembaga Pembuat",
            album = "Quest Essentials Vol. 1",
            durationSeconds = 184,
            mood = "Santai & Fokus",
            licenseNotice = "100% Bebas Hak Cipta (Royalty-Free / CC0) - Aman untuk Streaming",
            baseBpm = 82.0,
            trackType = 0
        ),
        MusicTrack(
            id = "track_2",
            title = "Lembaga Emas Synth Odyssey",
            artist = "Lembaga Emas Audio Lab",
            album = "Pencarian Lembaga Dunia",
            durationSeconds = 210,
            mood = "Epik & Futuristik",
            licenseNotice = "100% Bebas Hak Cipta (Royalty-Free) - Dibuat untuk Game Quest",
            baseBpm = 115.0,
            trackType = 1
        ),
        MusicTrack(
            id = "track_3",
            title = "Gemini Quantum Flow",
            artist = "Gemini Sound Synthesis & Fadlly",
            album = "AI Intelligence Horizon",
            durationSeconds = 168,
            mood = "Cerdas & Energik",
            licenseNotice = "100% Bebas Hak Cipta (Creative Commons Zero)",
            baseBpm = 96.0,
            trackType = 2
        ),
        MusicTrack(
            id = "track_4",
            title = "Mechanical Keyboard Tap Groove",
            artist = "Fadlly Studios",
            album = "Keyboard Aesthetic Waves",
            durationSeconds = 195,
            mood = "Arcade Retro",
            licenseNotice = "100% Bebas Hak Cipta (No Copyright Music)",
            baseBpm = 120.0,
            trackType = 3
        )
    )

    private val _playerState = MutableStateFlow(
        PlayerState(currentTrack = playlist[0], isPlaying = false)
    )
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var audioTrack: AudioTrack? = null
    private var synthJob: Job? = null
    private var timelineJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    private fun safeLog(tag: String, msg: String) {
        try {
            android.util.Log.e(tag, msg)
        } catch (_: Throwable) {
            System.err.println("[$tag] $msg")
        }
    }

    init {
        try {
            initAudioTrack()
        } catch (_: Throwable) {
            // Ignored on mock unit test JVM
        }
    }

    private fun initAudioTrack() {
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            if (minBufferSize <= 0) return
            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(minBufferSize * 4)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()
        } catch (e: Throwable) {
            safeLog(TAG, "AudioTrack init error: ${e.message}")
        }
    }

    fun togglePlayPause() {
        if (_playerState.value.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun play() {
        _playerState.value = _playerState.value.copy(isPlaying = true)
        startSynthesizer()
        startTimelineCounter()
    }

    fun pause() {
        _playerState.value = _playerState.value.copy(isPlaying = false)
        synthJob?.cancel()
        timelineJob?.cancel()
        try {
            audioTrack?.pause()
            audioTrack?.flush()
        } catch (e: Exception) {
            safeLog(TAG, "Audio pause error: ${e.message}")
        }
    }

    fun playTrack(track: MusicTrack) {
        pause()
        _playerState.value = _playerState.value.copy(
            currentTrack = track,
            currentPositionSeconds = 0,
            isPlaying = true
        )
        play()
    }

    fun nextTrack() {
        val currentIndex = playlist.indexOfFirst { it.id == _playerState.value.currentTrack.id }
        val nextIndex = if (_playerState.value.isShuffle) {
            (playlist.indices).filter { it != currentIndex }.randomOrNull() ?: 0
        } else {
            (currentIndex + 1) % playlist.size
        }
        playTrack(playlist[nextIndex])
    }

    fun previousTrack() {
        val currentIndex = playlist.indexOfFirst { it.id == _playerState.value.currentTrack.id }
        val prevIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
        playTrack(playlist[prevIndex])
    }

    fun seekTo(seconds: Int) {
        val clamped = seconds.coerceIn(0, _playerState.value.currentTrack.durationSeconds)
        _playerState.value = _playerState.value.copy(currentPositionSeconds = clamped)
    }

    fun setVolume(volume: Float) {
        val clamped = volume.coerceIn(0f, 1f)
        _playerState.value = _playerState.value.copy(volume = clamped)
        try {
            audioTrack?.setVolume(clamped)
        } catch (e: Exception) {
            safeLog(TAG, "Set volume error: ${e.message}")
        }
    }

    fun toggleShuffle() {
        _playerState.value = _playerState.value.copy(isShuffle = !_playerState.value.isShuffle)
    }

    fun toggleRepeat() {
        _playerState.value = _playerState.value.copy(isRepeat = !_playerState.value.isRepeat)
    }

    private fun startTimelineCounter() {
        timelineJob?.cancel()
        timelineJob = scope.launch {
            while (isActive && _playerState.value.isPlaying) {
                delay(1000)
                val current = _playerState.value.currentPositionSeconds
                val duration = _playerState.value.currentTrack.durationSeconds
                if (current + 1 >= duration) {
                    if (_playerState.value.isRepeat) {
                        _playerState.value = _playerState.value.copy(currentPositionSeconds = 0)
                    } else {
                        nextTrack()
                    }
                } else {
                    _playerState.value = _playerState.value.copy(currentPositionSeconds = current + 1)
                }
            }
        }
    }

    private fun startSynthesizer() {
        synthJob?.cancel()
        synthJob = scope.launch(Dispatchers.Default) {
            try {
                if (audioTrack == null || audioTrack?.state != AudioTrack.STATE_INITIALIZED) {
                    initAudioTrack()
                }
                audioTrack?.play()
                audioTrack?.setVolume(_playerState.value.volume)

                val bufferSize = 2048
                val audioBuffer = ShortArray(bufferSize)
                var sampleIndex: Long = 0

                val track = _playerState.value.currentTrack
                val bpm = track.baseBpm
                val beatDuration = (60.0 / bpm) * SAMPLE_RATE

                // Musical scales for procedural music generation:
                val frequencies = when (track.trackType) {
                    0 -> doubleArrayOf(146.83, 174.61, 196.00, 220.00, 261.63, 293.66, 349.23, 440.00) // LoFi Dm
                    1 -> doubleArrayOf(130.81, 164.81, 196.00, 246.94, 261.63, 329.63, 392.00, 523.25) // Synthwave C
                    2 -> doubleArrayOf(220.00, 246.94, 277.18, 329.63, 370.00, 440.00, 554.37, 659.25) // 8-Bit A
                    3 -> doubleArrayOf(174.61, 220.00, 261.63, 329.63, 392.00, 440.00, 523.25, 659.25) // Ambient F
                    else -> doubleArrayOf(130.81, 146.83, 164.81, 196.00, 220.00, 246.94, 261.63, 293.66) // DJ TikTok Jedag-Jedug (C, D, E, G, A, B, C)
                }

                while (isActive && _playerState.value.isPlaying) {
                    val trackType = _playerState.value.currentTrack.trackType
                    for (i in 0 until bufferSize) {
                        val currentSample = sampleIndex + i
                        val beatPos = (currentSample % beatDuration) / beatDuration
                        val measurePos = ((currentSample / beatDuration).toInt()) % 16

                        // Bassline frequency
                        val bassFreq = frequencies[measurePos % frequencies.size] / 2.0
                        val bassWave = when (trackType) {
                            4 -> {
                                // Punchy square/sawtooth bass slap for Indonesian DJ Jedag-Jedug
                                val phase = (bassFreq * currentSample / SAMPLE_RATE) % 1.0
                                (if (phase > 0.5) 0.5 else -0.5) * (1.0 - (beatPos * 0.5))
                            }
                            else -> sin(2.0 * PI * bassFreq * currentSample / SAMPLE_RATE)
                        }

                        // Melody note
                        val noteIndex = (measurePos * 3 + (beatPos * 4).toInt()) % frequencies.size
                        val melodyFreq = frequencies[noteIndex]
                        val melodyWave = when (trackType) {
                            2 -> { // 8-bit square wave
                                val phase = (melodyFreq * currentSample / SAMPLE_RATE) % 1.0
                                if (phase > 0.5) 0.6 else -0.6
                            }
                            4 -> { // Bright pluck lead for DJ TikTok
                                val pluckEnv = (1.0 - (beatPos * 2.0).coerceIn(0.0, 1.0))
                                sin(2.0 * PI * melodyFreq * currentSample / SAMPLE_RATE) * pluckEnv
                            }
                            else -> sin(2.0 * PI * melodyFreq * currentSample / SAMPLE_RATE)
                        }

                        // Drum kick: punchier with sub-bass for DJ TikTok
                        val kickEnv = (1.0 - beatPos).coerceIn(0.0, 1.0)
                        val kickFreq = if (trackType == 4) 65.0 + 110.0 * kickEnv else 55.0 + 80.0 * kickEnv
                        val kickWave = sin(2.0 * PI * kickFreq * currentSample / SAMPLE_RATE) * (kickEnv * kickEnv)

                        // Hi-hat / Clap
                        val hiHat = when (trackType) {
                            4 -> {
                                // Off-beat open hi-hat / clap on beatPos around 0.5
                                if (beatPos in 0.45..0.55 || beatPos < 0.06) (Math.random() - 0.5) * 0.25 else 0.0
                            }
                            else -> if (beatPos < 0.08) (Math.random() - 0.5) * 0.15 else 0.0
                        }

                        // Mix components
                        val mixed = when (trackType) {
                            4 -> (bassWave * 0.32 + melodyWave * 0.28 + kickWave * 0.42 + hiHat)
                            else -> (bassWave * 0.28 + melodyWave * 0.22 + kickWave * 0.35 + hiHat)
                        }
                        val clamped = mixed.coerceIn(-0.95, 0.95)
                        audioBuffer[i] = (clamped * Short.MAX_VALUE).toInt().toShort()
                    }

                    audioTrack?.write(audioBuffer, 0, bufferSize)
                    sampleIndex += bufferSize

                    // Update visualizer frequency bars dynamically
                    val pseudoFreqs = List(16) { barIndex ->
                        val phase = (sampleIndex.toDouble() / SAMPLE_RATE) * (if (trackType == 4) 4.5 else 3.0) + barIndex * 0.4
                        (0.2f + 0.75f * ((sin(phase).toFloat() + 1f) / 2f) * (1f - (barIndex * 0.03f))).coerceIn(0.1f, 0.98f)
                    }
                    _playerState.value = _playerState.value.copy(visualizerFrequencies = pseudoFreqs)
                }
            } catch (e: Exception) {
                safeLog(TAG, "Audio synthesis error: ${e.message}")
            }
        }
    }
}
