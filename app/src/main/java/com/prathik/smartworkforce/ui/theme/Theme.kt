package com.prathik.smartworkforce.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object AppColors {
    // Background and Surface
    val Background @Composable get() = MaterialTheme.colorScheme.background
    val CardWhite @Composable get() = MaterialTheme.colorScheme.surface
    
    // Main Brand Colors (Mapped to Green)
    val PrimaryBlue @Composable get() = MaterialTheme.colorScheme.primary
    
    // Text Colors
    val DarkText @Composable get() = MaterialTheme.colorScheme.onSurface
    val LightText @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
    
    // Semantic Colors
    val GreenSuccess = Color(0xFF2E7D32)
    val OrangePending = Color(0xFFFBC02D)
    val RedError = Color(0xFFD32F2F)
    val Green = Color(0xFF4CAF50)
    val Red = Color(0xFFF44336)
}

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimaryDark,
    onPrimary = GreenOnPrimaryDark,
    primaryContainer = GreenPrimaryContainerDark,
    onPrimaryContainer = GreenOnPrimaryContainerDark,
    secondary = GreenSecondaryDark,
    onSecondary = GreenOnSecondaryDark,
    secondaryContainer = GreenSecondaryContainerDark,
    onSecondaryContainer = GreenOnSecondaryContainerDark,
    background = GreenBackgroundDark,
    onBackground = GreenOnBackgroundDark,
    surface = GreenSurfaceDark,
    onSurface = GreenOnSurfaceDark,
    error = GreenError,
    onError = GreenOnError
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = GreenOnPrimary,
    primaryContainer = GreenPrimaryContainer,
    onPrimaryContainer = GreenOnPrimaryContainer,
    secondary = GreenSecondary,
    onSecondary = GreenOnSecondary,
    secondaryContainer = GreenSecondaryContainer,
    onSecondaryContainer = GreenOnSecondaryContainer,
    background = GreenBackgroundLight,
    onBackground = GreenOnBackgroundLight,
    surface = GreenSurfaceLight,
    onSurface = GreenOnSurfaceLight,
    error = GreenError,
    onError = GreenOnError
)

@Composable
fun SmartWorkforceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
