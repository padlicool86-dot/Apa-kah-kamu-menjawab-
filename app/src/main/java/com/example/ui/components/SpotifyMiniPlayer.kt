package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.MusicTrack
import com.example.audio.NonCopyrightAudioPlayer
import com.example.audio.PlayerState
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.CyberNavyElevated
import com.example.ui.theme.CyberNavySurface
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FadllyGoldBlueOrangeBrush
import com.example.ui.theme.NeonOrangePrimary
import com.example.ui.theme.PureGold
import com.example.ui.theme.QuestSuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibrantOrange

@Composable
fun SpotifyMiniPlayerBar(
    playerState: PlayerState,
    onTogglePlay: () -> Unit,
    onNext: () -> Unit,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onExpand() }
            .testTag("spotify_mini_player_bar"),
        color = Color(0xFF0F1E36).copy(alpha = 0.96f),
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Brush.horizontalGradient(listOf(ElectricCyan.copy(alpha = 0.6f), NeonOrangePrimary.copy(alpha = 0.6f)))
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Mini progress indicator bar on top
            val progress = if (playerState.currentTrack.durationSeconds > 0) {
                playerState.currentPositionSeconds.toFloat() / playerState.currentTrack.durationSeconds.toFloat()
            } else 0f

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp),
                color = VibrantOrange,
                trackColor = Color(0x3300B4D8)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Vinyl / Album Icon
                VinylDiscIcon(isPlaying = playerState.isPlaying)

                Spacer(modifier = Modifier.width(10.dp))

                // Track Info & Animated Wave
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = playerState.currentTrack.title,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Royalty Free",
                            tint = QuestSuccessGreen,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = playerState.currentTrack.artist,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "• Bebas Hak Cipta",
                            color = PureGold,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Mini sound visualizer bars
                SoundwaveMiniVisualizer(
                    isPlaying = playerState.isPlaying,
                    frequencies = playerState.visualizerFrequencies
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Play / Pause Button
                IconButton(
                    onClick = onTogglePlay,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(VibrantOrange)
                        .testTag("play_pause_button")
                ) {
                    Icon(
                        imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                        tint = CyberNavyDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Next Track Button
                IconButton(
                    onClick = onNext,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("next_track_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Track",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VinylDiscIcon(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "VinylSpin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "DiscRotation"
    )

    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color(0xFF111111))
            .border(1.5.dp, VibrantOrange, CircleShape)
            .rotate(if (isPlaying) rotation else 0f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(ElectricCyan),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
            )
        }
    }
}

@Composable
fun SoundwaveMiniVisualizer(
    isPlaying: Boolean,
    frequencies: List<Float>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(20.dp)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val bars = frequencies.take(5)
        bars.forEachIndexed { index, freq ->
            val barHeight = if (isPlaying) (freq * 18).coerceIn(4f, 18f).dp else 4.dp
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(barHeight)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (index % 2 == 0) ElectricCyan else NeonOrangePrimary
                    )
            )
        }
    }
}

/**
 * Full Spotify-inspired Player Sheet / Dialog
 */
@Composable
fun SpotifyExpandedPlayerDialog(
    playerState: PlayerState,
    onDismiss: () -> Unit,
    onTogglePlay: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onTrackSelect: (MusicTrack) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSeek: (Int) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF142B4E),
                            CyberNavyDark
                        )
                    )
                )
                .border(1.5.dp, FadllyGoldBlueOrangeBrush, RoundedCornerShape(24.dp))
                .padding(20.dp)
                .testTag("spotify_expanded_player_dialog")
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SPOTIFY STYLE NON-COPYRIGHT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibrantOrange,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Musik Bebas Hak Cipta",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Big Glowing Album Artwork
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0077B6),
                                    Color(0xFFFF6B35),
                                    Color(0xFFFFD700)
                                )
                            )
                        )
                        .padding(3.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(CyberNavySurface),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Cover Art",
                            tint = PureGold,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "QUEST AUDIO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = ElectricCyan,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "BY FADLLY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = VibrantOrange
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title & Artist
                Text(
                    text = playerState.currentTrack.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = playerState.currentTrack.artist,
                    fontSize = 13.sp,
                    color = ElectricCyan,
                    modifier = Modifier.padding(top = 2.dp)
                )

                // License guarantee pill
                Row(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x2210B981))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = QuestSuccessGreen,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = playerState.currentTrack.licenseNotice,
                        fontSize = 10.sp,
                        color = QuestSuccessGreen,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Timeline Scrubber
                val curSec = playerState.currentPositionSeconds
                val durSec = playerState.currentTrack.durationSeconds
                val curTimeStr = String.format("%d:%02d", curSec / 60, curSec % 60)
                val durTimeStr = String.format("%d:%02d", durSec / 60, durSec % 60)

                Slider(
                    value = curSec.toFloat(),
                    onValueChange = { onSeek(it.toInt()) },
                    valueRange = 0f..durSec.toFloat(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = VibrantOrange,
                        activeTrackColor = VibrantOrange,
                        inactiveTrackColor = Color(0x3300B4D8)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = curTimeStr, fontSize = 11.sp, color = TextSecondary)
                    Text(text = durTimeStr, fontSize = 11.sp, color = TextSecondary)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Playback Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { NonCopyrightAudioPlayer.toggleShuffle() }) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Shuffle",
                            tint = if (playerState.isShuffle) VibrantOrange else TextSecondary
                        )
                    }

                    IconButton(onClick = onPrev) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous",
                            tint = TextPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(
                        onClick = onTogglePlay,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(listOf(PureGold, VibrantOrange))
                            )
                    ) {
                        Icon(
                            imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                            tint = CyberNavyDark,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(onClick = onNext) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next",
                            tint = TextPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(onClick = { NonCopyrightAudioPlayer.toggleRepeat() }) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "Repeat",
                            tint = if (playerState.isRepeat) ElectricCyan else TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Volume slider
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Volume",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = playerState.volume,
                        onValueChange = onVolumeChange,
                        valueRange = 0f..1f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricCyan,
                            activeTrackColor = ElectricCyan,
                            inactiveTrackColor = Color(0x3300B4D8)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tracklist Selection
                Text(
                    text = "Daftar Musik (Termasuk DJ TikTok Viral 2023):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureGold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    items(NonCopyrightAudioPlayer.playlist) { track ->
                        val isSelected = track.id == playerState.currentTrack.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable { onTrackSelect(track) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFF1B3B6F) else CyberNavyElevated.copy(alpha = 0.6f)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, PureGold) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = null,
                                        tint = if (isSelected) PureGold else ElectricCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = track.title,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) PureGold else TextPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${track.artist} • ${track.mood}",
                                            fontSize = 10.sp,
                                            color = TextSecondary,
                                            maxLines = 1
                                        )
                                    }
                                }

                                if (isSelected && playerState.isPlaying) {
                                    SoundwaveMiniVisualizer(
                                        isPlaying = true,
                                        frequencies = playerState.visualizerFrequencies
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
