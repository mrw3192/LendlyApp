package com.example.lendlyapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// ─── Figma-token–based colour schemes ─────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(
    primary = FigmaNeonGreen,
    onPrimary = OnPrimaryGreen,
    primaryContainer = FigmaOliveSeed,
    background = FigmaDarkBg,
    surface = FigmaOliveSeed,
    onBackground = FigmaDarkText,
    onSurface = FigmaDarkText,
)

private val LightColorScheme = lightColorScheme(
    primary = FigmaNeonGreen,
    onPrimary = OnPrimaryGreen,
    primaryContainer = FigmaLightSurface,
    background = FigmaLightBg,
    surface = FigmaLightSurface,
    onBackground = FigmaLightText,
    onSurface = FigmaLightText,
)

@Composable
fun LendlyAppTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Set to false so Figma tokens are always used (no Android 12 dynamic colour override).
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
