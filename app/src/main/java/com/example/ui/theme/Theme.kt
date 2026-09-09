package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = McEmerald,
    onPrimary = McObsidian,
    primaryContainer = McEmeraldDark,
    onPrimaryContainer = McEmeraldLight,
    secondary = McGold,
    onSecondary = McObsidian,
    secondaryContainer = McSurfaceVariant,
    onSecondaryContainer = McGold,
    tertiary = McDiamond,
    onTertiary = McObsidian,
    background = McObsidian,
    onBackground = McTextPrimary,
    surface = McDeepSlate,
    onSurface = McTextPrimary,
    surfaceVariant = McSurfaceVariant,
    onSurfaceVariant = McTextSecondary,
    outline = McCardStroke,
  )

private val LightColorScheme = DarkColorScheme // Pojav launcher is primarily a deep dark gaming theme

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
