package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core Blue Theme (Deep Midnight, Cyber Blue, Electric Cyan)
val CyberNavyDark = Color(0xFF070E1E)
val CyberNavySurface = Color(0xFF0F1C36)
val CyberNavyElevated = Color(0xFF16274B)
val CyberBluePrimary = Color(0xFF0077B6)
val ElectricCyan = Color(0xFF00B4D8)
val NeonCyanGlow = Color(0xFF90E0EF)

// Vibrant Orange Theme (Flame, Neon, Sunset)
val NeonOrangePrimary = Color(0xFFFF6B35)
val VibrantOrange = Color(0xFFFF8500)
val SunsetOrange = Color(0xFFFF9E00)
val SoftOrangeContainer = Color(0x33FF6B35)

// Golden Accents (Lembaga Emas & Gemini)
val PureGold = Color(0xFFFFD700)
val RadiantGold = Color(0xFFFFC107)
val AmberGold = Color(0xFFFFA000)
val SoftGoldContainer = Color(0x33FFD700)

// Text & Surfaces
val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Status Colors
val QuestSuccessGreen = Color(0xFF10B981)
val QuestErrorRed = Color(0xFFEF4444)
val QuestPurpleAccent = Color(0xFF8B5CF6)

// Custom Gradients requested by user:
// 1. Fadlly text gradient: Gold mixed with Blue and Orange
val FadllyGoldBlueOrangeBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFD700), // Pure Gold
        Color(0xFFFF8500), // Vibrant Orange
        Color(0xFF00B4D8), // Electric Cyan
        Color(0xFFFF6B35), // Neon Orange
        Color(0xFFFFD700)  // Golden Tail
    )
)

// 2. Gemini Golden Brush
val GeminiGoldBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFE082),
        Color(0xFFFFD700),
        Color(0xFFFFB300),
        Color(0xFFFFE082)
    )
)

// 3. Lembaga Emas Brush
val LembagaEmasBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFF9C4),
        Color(0xFFFFD700),
        Color(0xFFFF8F00),
        Color(0xFFFFD700)
    )
)

// 4. Blue-Orange Cyber Quest Card Brush
val BlueOrangeQuestBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFF0A2246),
        Color(0xFF1B3B6F),
        Color(0x33FF6B35)
    )
)
