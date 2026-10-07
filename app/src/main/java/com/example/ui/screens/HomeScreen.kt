package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AppScreen
import com.example.ui.QuestViewModel
import com.example.ui.UserStats
import com.example.ui.components.FadllyBrandingHeader
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
fun HomeScreen(
    viewModel: QuestViewModel,
    userStats: UserStats,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Mandatory Branding Header: Fadlly (Gold+Blue+Orange), Lembaga Pembuat, Gemini (Gold), Lembaga Emas
        item {
            Spacer(modifier = Modifier.height(8.dp))
            FadllyBrandingHeader(
                onProfileClick = { viewModel.navigateTo(AppScreen.CREATOR_PROFILE) }
            )
        }

        // 2. Hero Banner: Quest "Bisa Kah Kamu Menjawab?!"
        item {
            HeroQuestBanner(
                onStartClick = { viewModel.startQuest("Semua Quest") }
            )
        }

        // 3. Scoreboard & Trivia Tracking Dashboard (Total score, correct, incorrect, accuracy)
        item {
            UserStatsOverviewCard(stats = userStats)
        }

        // 4. Feature Highlight: "Lembaga Emas - Pencarian Lembaga Dunia"
        item {
            LembagaEmasSearchFeatureCard(
                onExploreClick = { viewModel.navigateTo(AppScreen.LEMBAGA_EMAS) }
            )
        }

        // 5. Feature Highlight: "Gemini AI Chatbot - Quest Master"
        item {
            GeminiChatFeatureCard(
                onChatClick = { viewModel.navigateTo(AppScreen.GEMINI_CHAT) }
            )
        }

        // 6. Quest Categories
        item {
            Text(
                text = "Pilih Kategori Quest:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        item {
            QuestCategoryItem(
                title = "Keyboard & Teknologi",
                subtitle = "Shortcut, Sejarah Mesin Tik, Switch Mekanikal & Hardware",
                icon = Icons.Default.Keyboard,
                accentColor = ElectricCyan,
                badge = "Floating Logo Theme",
                onClick = { viewModel.startQuest("Keyboard & Teknologi") }
            )
        }

        item {
            QuestCategoryItem(
                title = "Lembaga Emas & Lembaga Dunia",
                subtitle = "PBB, NASA, CERN, WHO, UNESCO & Warisan Peradaban",
                icon = Icons.Default.Public,
                accentColor = PureGold,
                badge = "Lembaga Emas",
                onClick = { viewModel.startQuest("Lembaga Emas & Dunia") }
            )
        }

        item {
            QuestCategoryItem(
                title = "Kecerdasan Buatan & Gemini",
                subtitle = "Model Gemini Multimodal, Uji Turing & Jaringan Saraf",
                icon = Icons.Default.AutoAwesome,
                accentColor = RadiantGold,
                badge = "Sumber: Gemini",
                onClick = { viewModel.startQuest("Kecerdasan Buatan & Gemini") }
            )
        }

        item {
            QuestCategoryItem(
                title = "Tantangan Quest Fadlly",
                subtitle = "Teka-teki Logika Khusus dari Sang Kreator & Lembaga Pembuat",
                icon = Icons.Default.Terminal,
                accentColor = NeonOrangePrimary,
                badge = "Master Fadlly",
                onClick = { viewModel.startQuest("Tantangan Quest Fadlly") }
            )
        }

        item {
            QuestCategoryItem(
                title = "DJ TikTok & Musik Viral 2023",
                subtitle = "Malam Pagi, Karna Su Sayang, Dumes, Kisinan & Tren Viral",
                icon = Icons.Default.MusicNote,
                accentColor = VibrantOrange,
                badge = "DJ TikTok 2023",
                onClick = { viewModel.startQuest("DJ TikTok & Musik Viral") }
            )
        }

        // Space for bottom Spotify mini player
        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
fun HeroQuestBanner(
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(
                1.5.dp,
                Brush.linearGradient(listOf(ElectricCyan, NeonOrangePrimary, PureGold)),
                RoundedCornerShape(22.dp)
            )
            .testTag("hero_quest_banner"),
        colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
        shape = RoundedCornerShape(22.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Background Hero Image
            Image(
                painter = painterResource(id = R.drawable.quest_hero_banner_1791378247180),
                contentDescription = "Quest Banner Art",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Gradient Overlay for Readability
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                CyberNavyDark.copy(alpha = 0.85f),
                                CyberNavyDark.copy(alpha = 0.98f)
                            )
                        )
                    )
            )

            // Content on top of banner
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .align(Alignment.BottomStart)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonOrangePrimary)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "OFFICIAL QUEST",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = CyberNavyDark
                        )
                    }

                    Text(
                        text = "Karya Fadlly x Lembaga Pembuat",
                        fontSize = 11.sp,
                        color = PureGold,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // The Main Quest Title
                Text(
                    text = "Bisa Kah Kamu Menjawab?!",
                    style = TextStyle(
                        brush = Brush.horizontalGradient(
                            listOf(Color.White, ElectricCyan, RadiantGold, NeonOrangePrimary)
                        ),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                )

                Text(
                    text = "Tantang wawasanmu tentang Keyboard, Lembaga Dunia, dan Kecerdasan AI Gemini!",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                // Start Quest Button
                Button(
                    onClick = onStartClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("start_quest_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VibrantOrange
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Mulai Quest",
                        tint = CyberNavyDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mulai Quest Sekarang",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CyberNavyDark
                    )
                }
            }
        }
    }
}

/**
 * Scoreboard & Trivia Tracking Dashboard
 * Satisfies: "Implement a scoring system that tracks correct and incorrect trivia answers, displaying the player's total score in the UI."
 */
@Composable
fun UserStatsOverviewCard(stats: UserStats) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("user_stats_overview_card"),
        colors = CardDefaults.cardColors(containerColor = CyberNavyElevated.copy(alpha = 0.92f)),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.2.dp,
            Brush.horizontalGradient(listOf(ElectricCyan, VibrantOrange, PureGold))
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Main Top Row: Total Score Highlight
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(PureGold, VibrantOrange)))
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(CyberNavyDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = "Total Skor",
                            tint = PureGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "TOTAL SKOR PEMAIN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureGold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${stats.totalScore} Poin",
                            style = TextStyle(
                                brush = FadllyGoldBlueOrangeBrush,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            ),
                            modifier = Modifier.testTag("player_total_score_text")
                        )
                    }
                }

                // Rank Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF132A4D))
                        .border(1.dp, ElectricCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stats.masterRankTitle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sub-metrics row: Correct, Incorrect, Accuracy, Golden Coins
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CyberNavySurface)
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Correct Answers Tracker
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Benar",
                            tint = QuestSuccessGreen,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "Benar", fontSize = 10.sp, color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${stats.totalCorrectAnswers}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = QuestSuccessGreen,
                        modifier = Modifier.testTag("correct_answers_count_text")
                    )
                }

                Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0x22FFFFFF)))

                // Incorrect Answers Tracker
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Salah",
                            tint = QuestErrorRed,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "Salah", fontSize = 10.sp, color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${stats.totalIncorrectAnswers}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = QuestErrorRed,
                        modifier = Modifier.testTag("incorrect_answers_count_text")
                    )
                }

                Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0x22FFFFFF)))

                // Accuracy Percentage Tracker
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrackChanges,
                            contentDescription = "Akurasi",
                            tint = ElectricCyan,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "Akurasi", fontSize = 10.sp, color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${stats.accuracyPercentage}%",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = ElectricCyan,
                        modifier = Modifier.testTag("accuracy_percentage_text")
                    )
                }

                Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0x22FFFFFF)))

                // Lembaga Coins
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = "Koin",
                            tint = PureGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "Koin", fontSize = 10.sp, color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${stats.goldenCoins}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = PureGold
                    )
                }
            }
        }
    }
}

@Composable
fun LembagaEmasSearchFeatureCard(
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onExploreClick() }
            .testTag("lembaga_emas_feature_card"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B0E)),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, PureGold.copy(alpha = 0.8f)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(PureGold, VibrantOrange)))
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(CyberNavyDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = "Pencarian Lembaga Dunia",
                    tint = PureGold,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "LEMBAGA EMAS",
                        style = TextStyle(
                            brush = LembagaEmasBrush,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x33FFD700))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PORTAL GLOBAL",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureGold
                        )
                    }
                }
                Text(
                    text = "Pencarian Lembaga Dunia Terverifikasi",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = "Jelajahi PBB, NASA, CERN, WHO, UNESCO & mainkan quest spesifiknya!",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Buka",
                tint = PureGold,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Gemini Chatbot Feature Banner
 */
@Composable
fun GeminiChatFeatureCard(
    onChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onChatClick() }
            .testTag("gemini_chat_feature_card"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D38)),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            Brush.horizontalGradient(listOf(PureGold, ElectricCyan))
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Brush.sweepGradient(listOf(PureGold, VibrantOrange, ElectricCyan, PureGold)))
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(CyberNavyDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QuestionAnswer,
                    contentDescription = "Chatbot Gemini",
                    tint = PureGold,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "GEMINI AI CHATBOT",
                        style = TextStyle(
                            brush = GeminiGoldBrush,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x3300B4D8))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "QUEST MASTER",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                    }
                }
                Text(
                    text = "Konsultasi Trivia & Tanya Jawab AI",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = "Diskusikan sejarah keyboard, minta teka-teki logika, atau bedah fakta lembaga dunia!",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Buka Chatbot",
                tint = PureGold,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun QuestCategoryItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    badge: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("category_card_${title.replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = CyberNavyElevated.copy(alpha = 0.9f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(accentColor.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Main",
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
