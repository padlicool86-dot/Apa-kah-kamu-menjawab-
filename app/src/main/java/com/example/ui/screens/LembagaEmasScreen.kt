package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.WorldInstitution
import com.example.ui.AppScreen
import com.example.ui.QuestViewModel
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.CyberNavyElevated
import com.example.ui.theme.CyberNavySurface
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.LembagaEmasBrush
import com.example.ui.theme.NeonOrangePrimary
import com.example.ui.theme.PureGold
import com.example.ui.theme.QuestSuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibrantOrange

@Composable
fun LembagaEmasScreen(
    viewModel: QuestViewModel,
    searchQuery: String,
    categoryFilter: String,
    selectedInstitution: WorldInstitution?,
    institutions: List<WorldInstitution>,
    modifier: Modifier = Modifier
) {
    // Detail Dialog
    if (selectedInstitution != null) {
        InstitutionDetailDialog(
            institution = selectedInstitution,
            onDismiss = { viewModel.selectInstitutionDetail(null) },
            onStartInstitutionQuest = {
                viewModel.selectInstitutionDetail(null)
                viewModel.startQuest("Lembaga Emas & Dunia")
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Top Header with back button & title
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("lembaga_emas_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali ke Beranda",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = PureGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "LEMBAGA EMAS",
                            style = TextStyle(
                                brush = LembagaEmasBrush,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                    Text(
                        text = "Pencarian Lembaga Dunia & Fakta Sejarah",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // 2. Search Input Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateInstitutionSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("institution_search_input"),
                placeholder = {
                    Text(text = "Cari Lembaga Dunia (e.g. PBB, NASA, CERN, WHO...)", fontSize = 13.sp, color = TextSecondary)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari",
                        tint = PureGold
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateInstitutionSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Hapus", tint = TextSecondary)
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PureGold,
                    unfocusedBorderColor = Color(0x4400B4D8),
                    focusedContainerColor = CyberNavySurface,
                    unfocusedContainerColor = CyberNavySurface,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )
        }

        // 3. Category Filter Chips
        item {
            val categories = listOf("Semua", "Diplomasi", "Sains", "Fisika", "Kesehatan", "Pendidikan", "Ekonomi", "Riset")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = cat == categoryFilter
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.updateInstitutionCategoryFilter(cat) },
                        label = {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) CyberNavyDark else TextPrimary
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PureGold,
                            containerColor = CyberNavyElevated
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) PureGold else Color(0x3300B4D8)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // 4. Results Count & Banner
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lembaga Terdaftar (${institutions.size}):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan
                )

                Text(
                    text = "Terverifikasi Lembaga Emas",
                    fontSize = 10.sp,
                    color = PureGold,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 5. Institution Cards
        items(institutions) { institution ->
            InstitutionCard(
                institution = institution,
                onClick = { viewModel.selectInstitutionDetail(institution) }
            )
        }

        // Space for bottom Spotify player
        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
fun InstitutionCard(
    institution: WorldInstitution,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag("institution_card_${institution.id}"),
        colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
        border = androidx.compose.foundation.BorderStroke(
            1.2.dp,
            Brush.horizontalGradient(listOf(PureGold.copy(alpha = 0.7f), Color(0x3300B4D8)))
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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
                            .background(Color(0xFF261D08))
                            .border(1.dp, PureGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CorporateFare,
                            contentDescription = null,
                            tint = PureGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = institution.shortName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = PureGold
                        )
                        Text(
                            text = "Berdiri: ${institution.foundedYear}",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Golden badge title
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x33FFD700))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = institution.goldenBadgeTitle,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = PureGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = institution.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = institution.description,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 16.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "HQ",
                        tint = ElectricCyan,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = institution.headquarter,
                        fontSize = 10.sp,
                        color = ElectricCyan
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Buka Profil",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VibrantOrange
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = VibrantOrange,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InstitutionDetailDialog(
    institution: WorldInstitution,
    onDismiss: () -> Unit,
    onStartInstitutionQuest: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, LembagaEmasBrush, RoundedCornerShape(24.dp))
                .testTag("institution_detail_dialog"),
            colors = CardDefaults.cardColors(containerColor = CyberNavySurface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = PureGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = institution.shortName,
                            style = TextStyle(
                                brush = LembagaEmasBrush,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }

                Text(
                    text = institution.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = ElectricCyan
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = institution.description,
                    fontSize = 12.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Key facts box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1E38)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "📍 Markas: ${institution.headquarter}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "🌐 Jangkauan: ${institution.memberCountOrReach}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "🏆 Gelar Emas: ${institution.goldenBadgeTitle}",
                            fontSize = 11.sp,
                            color = PureGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Pencapaian Bersejarah:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureGold
                )

                institution.keyAchievements.forEach { ach ->
                    Text(
                        text = "• $ach",
                        fontSize = 11.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Play quest for this institution
                Button(
                    onClick = onStartInstitutionQuest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("start_institution_quest_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = CyberNavyDark)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mulai Quest Lembaga Ini",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberNavyDark
                    )
                }
            }
        }
    }
}
