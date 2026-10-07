package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppScreen
import com.example.ui.QuestViewModel
import com.example.ui.components.FloatingKeyboardBackground
import com.example.ui.components.SpotifyExpandedPlayerDialog
import com.example.ui.components.SpotifyMiniPlayerBar
import com.example.ui.screens.CreatorProfileScreen
import com.example.ui.screens.GeminiChatScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LembagaEmasScreen
import com.example.ui.screens.QuestPlayScreen
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: QuestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: QuestViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val questPlayState by viewModel.questPlayState.collectAsStateWithLifecycle()
    val userStats by viewModel.userStats.collectAsStateWithLifecycle()
    val playerState by viewModel.audioPlayerState.collectAsStateWithLifecycle()
    val showMusicSheet by viewModel.showMusicSheet.collectAsStateWithLifecycle()
    val searchQuery by viewModel.institutionSearchQuery.collectAsStateWithLifecycle()
    val categoryFilter by viewModel.institutionCategoryFilter.collectAsStateWithLifecycle()
    val selectedInstitution by viewModel.selectedInstitutionDetail.collectAsStateWithLifecycle()

    // Handle system back navigation to Home if on sub-screens
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateTo(AppScreen.HOME)
    }

    // Expanded Spotify Dialog
    if (showMusicSheet) {
        SpotifyExpandedPlayerDialog(
            playerState = playerState,
            onDismiss = { viewModel.setShowMusicSheet(false) },
            onTogglePlay = { viewModel.togglePlayPause() },
            onNext = { viewModel.nextTrack() },
            onPrev = { viewModel.previousTrack() },
            onTrackSelect = { viewModel.playTrack(it) },
            onVolumeChange = { viewModel.setVolume(it) },
            onSeek = { viewModel.seekTo(it) }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Animated Floating Keyboard Background (Layered behind everything)
        // Satisfies: "Dan Kasih Logo Keyboard,DLL yg melayang layang di latar belakang Quest Tersebut"
        FloatingKeyboardBackground(
            modifier = Modifier.fillMaxSize(),
            alpha = 0.40f
        )

        // 2. Main Scaffold Content
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = CyberNavyDark.copy(alpha = 0.45f), // Semi-transparent to let keyboards float through!
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                // Persistent Spotify-style mini player docked at the bottom
                // Satisfies: "serta Musik Yg Spotify Yg ga hak cipta ambilnnya"
                Box(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
                    SpotifyMiniPlayerBar(
                        playerState = playerState,
                        onTogglePlay = { viewModel.togglePlayPause() },
                        onNext = { viewModel.nextTrack() },
                        onExpand = { viewModel.setShowMusicSheet(true) }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    AppScreen.HOME -> {
                        HomeScreen(
                            viewModel = viewModel,
                            userStats = userStats,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    AppScreen.QUEST_PLAY -> {
                        QuestPlayScreen(
                            viewModel = viewModel,
                            playState = questPlayState,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    AppScreen.LEMBAGA_EMAS -> {
                        LembagaEmasScreen(
                            viewModel = viewModel,
                            searchQuery = searchQuery,
                            categoryFilter = categoryFilter,
                            selectedInstitution = selectedInstitution,
                            institutions = viewModel.getFilteredInstitutions(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    AppScreen.GEMINI_CHAT -> {
                        val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
                        val isChatLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()
                        val chatInputText by viewModel.chatInputText.collectAsStateWithLifecycle()
                        GeminiChatScreen(
                            viewModel = viewModel,
                            messages = messages,
                            isLoading = isChatLoading,
                            inputText = chatInputText,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    AppScreen.CREATOR_PROFILE -> {
                        CreatorProfileScreen(
                            viewModel = viewModel,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
