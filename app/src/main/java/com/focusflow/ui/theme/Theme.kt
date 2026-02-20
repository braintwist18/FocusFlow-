package com.focusflow.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = SoftBeige,
    primaryContainer = SageGreen,
    onPrimaryContainer = ForestGreenDark,
    secondary = WoodBrown,
    onSecondary = SoftBeige,
    secondaryContainer = SoftBeige,
    onSecondaryContainer = WoodBrownDark,
    tertiary = SageGreen,
    onTertiary = ForestGreen,
    background = SoftBeige,
    onBackground = ForestGreenDark,
    surface = SoftBeige,
    onSurface = ForestGreenDark,
    surfaceVariant = SageGreen.copy(alpha = 0.3f),
    onSurfaceVariant = ForestGreen
)

private val DarkColorScheme = darkColorScheme(
    primary = SageGreen,
    onPrimary = ForestGreenDark,
    primaryContainer = ForestGreen,
    onPrimaryContainer = SoftBeige,
    secondary = WoodBrown,
    onSecondary = SoftBeige,
    secondaryContainer = WoodBrownDark,
    onSecondaryContainer = SoftBeige,
    tertiary = SageGreenDark,
    onTertiary = ForestGreenDark,
    background = ForestGreenDark,
    onBackground = SoftBeige,
    surface = ForestGreenDark,
    onSurface = SoftBeige,
    surfaceVariant = SageGreenDark.copy(alpha = 0.3f),
    onSurfaceVariant = SageGreen
)

@Composable
fun FocusFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode && view.context is Activity) {
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
