package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SafeGuardDarkColorScheme = darkColorScheme(
    primary = MintEmerald,
    onPrimary = ObsidianBackground,
    primaryContainer = MintEmeraldDark,
    onPrimaryContainer = TextPrimary,
    secondary = CyberCyan,
    onSecondary = ObsidianBackground,
    secondaryContainer = RoyalBlue,
    onSecondaryContainer = TextPrimary,
    tertiary = AmberWarning,
    onTertiary = ObsidianBackground,
    error = ThreatRed,
    onError = Color.White,
    background = ObsidianBackground,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = CardSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)

@Composable
fun SafeGuardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SafeGuardDarkColorScheme,
        typography = Typography,
        content = content
    )
}
