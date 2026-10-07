package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = CyberNavyDark,
    primaryContainer = CyberBluePrimary,
    onPrimaryContainer = TextPrimary,
    secondary = NeonOrangePrimary,
    onSecondary = CyberNavyDark,
    secondaryContainer = SoftOrangeContainer,
    onSecondaryContainer = VibrantOrange,
    tertiary = PureGold,
    onTertiary = CyberNavyDark,
    tertiaryContainer = SoftGoldContainer,
    onTertiaryContainer = RadiantGold,
    background = CyberNavyDark,
    onBackground = TextPrimary,
    surface = CyberNavySurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberNavyElevated,
    onSurfaceVariant = TextSecondary,
    outline = VibrantOrange.copy(alpha = 0.5f),
    outlineVariant = ElectricCyan.copy(alpha = 0.3f)
)

private val LightColorScheme = DarkColorScheme // Quest Cyber Theme is designed with rich dark blue & neon orange

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep vivid Blue-Orange-Gold aesthetic consistent
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = CyberNavyDark.toArgb()
            window.navigationBarColor = CyberNavyDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
