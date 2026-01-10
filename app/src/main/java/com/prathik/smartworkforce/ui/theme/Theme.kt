package com.prathik.smartworkforce.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppColors {
    val Background = Color(0xFFF5F7FA) // Light gray background
    val CardWhite = Color.White
    val PrimaryBlue = Color(0xFF4A90E2) // Main blue
    val DarkText = Color(0xFF2C3E50) // Dark text
    val LightText = Color(0xFF7F8C8D) // Light text
    val GreenSuccess = Color(0xFF27AE60) // Approved/Success
    val OrangePending = Color(0xFFF39C12) // Pending/Warning
    val RedError = Color(0xFFE74C3C) // Rejected/Error
    val Green = Color(0xFF2ECC71)
    val Red = Color(0xFFE74C3C)
}

private val LightColorScheme = lightColorScheme(
    primary = AppColors.PrimaryBlue,
    secondary = AppColors.GreenSuccess,
    background = AppColors.Background,
    surface = AppColors.CardWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = AppColors.DarkText,
    onSurface = AppColors.DarkText
)

@Composable
fun SmartWorkforceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography, // Assumes a Typography.kt file exists
        content = content
    )
}
