package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBluePrimary
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.CyberNavySurface
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FadllyGoldBlueOrangeBrush
import com.example.ui.theme.GeminiGoldBrush
import com.example.ui.theme.LembagaEmasBrush
import com.example.ui.theme.NeonOrangePrimary
import com.example.ui.theme.PureGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VibrantOrange

/**
 * Header and signature components satisfying:
 * 1. "nama Fadlly Tulisan Fadlly Yg Berwarna Emas Campuran Biru Oranye"
 * 2. "Lembaga Pembuat Sebagai pembuat Quest"
 * 3. "Sumbernya Dari Gemini Tulisan Gemini Yg berwarna Emas"
 * 4. "Lembaga Emas Sebagai Pencarian Lembaga Dunia"
 * 5. Floating keyboard indicators
 */

@Composable
fun FadllyBrandingHeader(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        CyberNavySurface.copy(alpha = 0.95f),
                        Color(0xFF132A4D).copy(alpha = 0.95f),
                        CyberNavyDark.copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = FadllyGoldBlueOrangeBrush,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onProfileClick() }
            .padding(16.dp)
            .testTag("fadlly_branding_header")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Creator: Fadlly with stylized Gold-Blue-Orange gradient
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Brush.sweepGradient(listOf(PureGold, VibrantOrange, ElectricCyan, PureGold)))
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(CyberNavyDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Keyboard,
                            contentDescription = "Fadlly Creator Icon",
                            tint = PureGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "KREASI UTAMA",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonOrangePrimary,
                            letterSpacing = 1.2.sp
                        )
                        // "Fadlly" written in Gold mixed with Blue and Orange
                        Text(
                            text = "Fadlly",
                            style = TextStyle(
                                brush = FadllyGoldBlueOrangeBrush,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }

                // Official Guild Badge: "Lembaga Pembuat"
                LembagaPembuatBadge()
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sub-row: Source Gemini (Gold) & Lembaga Emas Dunia
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "Sumbernya Dari Gemini" with Gemini in Gold
                GeminiSourceBadge()

                // "Lembaga Emas" Tag
                LembagaEmasPill()
            }
        }
    }
}

@Composable
fun LembagaPembuatBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0F264A))
            .border(1.dp, ElectricCyan.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .testTag("badge_lembaga_pembuat")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = "Lembaga Pembuat Icon",
                tint = ElectricCyan,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = "Lembaga Pembuat",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Pembuat Resmi Quest",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    color = ElectricCyan
                )
            }
        }
    }
}

@Composable
fun GeminiSourceBadge(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF161F38))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = "Gemini AI Icon",
            tint = PureGold,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "Sumber: ",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )
        // "Gemini" written in radiant Gold
        Text(
            text = "Gemini",
            style = TextStyle(
                brush = GeminiGoldBrush,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
            )
        )
    }
}

@Composable
fun LembagaEmasPill(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF261D08))
            .border(1.dp, PureGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.MilitaryTech,
            contentDescription = "Lembaga Emas Icon",
            tint = PureGold,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "Lembaga Emas",
            style = TextStyle(
                brush = LembagaEmasBrush,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
