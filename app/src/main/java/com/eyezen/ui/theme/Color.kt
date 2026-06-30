package com.eyezen.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * EyeZen color definitions for light and dark themes.
 */

// Light theme colors
private val LightPrimary = Color(0xFF2E7D32)
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightPrimaryContainer = Color(0xFFB7F8B5)
private val LightOnPrimaryContainer = Color(0xFF002202)

private val LightSecondary = Color(0xFF386520)
private val LightOnSecondary = Color(0xFFFFFFFF)
private val LightSecondaryContainer = Color(0xFFB8ED9F)
private val LightOnSecondaryContainer = Color(0xFF0F2000)

private val LightTertiary = Color(0xFF4D6A4F)
private val LightOnTertiary = Color(0xFFFFFFFF)
private val LightTertiaryContainer = Color(0xFFCEF2D0)
private val LightOnTertiaryContainer = Color(0xFF06200C)

private val LightError = Color(0xFFB3261E)
private val LightOnError = Color(0xFFFFFFFF)
private val LightErrorContainer = Color(0xFFF9DEDC)
private val LightOnErrorContainer = Color(0xFF410E0B)

private val LightBackground = Color(0xFFFCFCFC)
private val LightOnBackground = Color(0xFF1C1C1C)
private val LightSurface = Color(0xFFFCFCFC)
private val LightOnSurface = Color(0xFF1C1C1C)
private val LightSurfaceVariant = Color(0xFFDEE4DB)
private val LightOnSurfaceVariant = Color(0xFF4F4F47)

// Dark theme colors
private val DarkPrimary = Color(0xFF9BF59F)
private val DarkOnPrimary = Color(0xFF003A06)
private val DarkPrimaryContainer = Color(0xFF1B5E1A)
private val DarkOnPrimaryContainer = Color(0xFFB7F8B5)

private val DarkSecondary = Color(0xFF9CDE84)
private val DarkOnSecondary = Color(0xFF1A3B09)
private val DarkSecondaryContainer = Color(0xFF264C14)
private val DarkOnSecondaryContainer = Color(0xFFB8ED9F)

private val DarkTertiary = Color(0xFFB2D5B5)
private val DarkOnTertiary = Color(0xFF1F3720)
private val DarkTertiaryContainer = Color(0xFF354E36)
private val DarkOnTertiaryContainer = Color(0xFFCEF2D0)

private val DarkError = Color(0xFFF2B8B5)
private val DarkOnError = Color(0xFF601410)
private val DarkErrorContainer = Color(0xFF8C1D18)
private val DarkOnErrorContainer = Color(0xFFF9DEDC)

private val DarkBackground = Color(0xFF1C1C1C)
private val DarkOnBackground = Color(0xFFE6E6E6)
private val DarkSurface = Color(0xFF1C1C1C)
private val DarkOnSurface = Color(0xFFE6E6E6)
private val DarkSurfaceVariant = Color(0xFF4F4F47)
private val DarkOnSurfaceVariant = Color(0xFFC9C7BF)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
)

/**
 * EyeZen theme composable.
 *
 * Applies Material Design 3 theme with support for:
 * - Light/dark mode
 * - Dynamic colors (Android 12+)
 */
@Composable
fun EyeZenTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            // Dynamic colors will be applied by Material3
            if (darkTheme) DarkColorScheme else LightColorScheme
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = EyeZenTypography,
        shapes = EyeZenShapes,
        content = content
    )
}
