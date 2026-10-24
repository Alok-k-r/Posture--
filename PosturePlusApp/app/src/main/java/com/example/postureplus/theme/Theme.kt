package com.example.postureplus.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PostureColorScheme =
  lightColorScheme(
    primary = AccentTerracotta,
    onPrimary = TextOnDark,
    primaryContainer = AccentTerracottaLight,
    onPrimaryContainer = AccentTerracotta,
    secondary = AccentAmber,
    onSecondary = TextOnDark,
    tertiary = AccentSage,
    onTertiary = TextOnDark,
    background = BgCanvas,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceTinted,
    onSurfaceVariant = TextSecondary,
  )

@Composable
fun PosturePlusTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = PostureColorScheme,
    typography = Typography,
    content = content
  )
}
