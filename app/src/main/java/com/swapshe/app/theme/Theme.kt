package com.swapshe.app.theme

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

private val LightColorScheme = lightColorScheme(
    primary = PureBlack,
    onPrimary = Color.White,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = MinimalInk,
    secondary = DarkCharcoal,
    onSecondary = Color.White,
    tertiary = PureBlack,
    onTertiary = Color.White,
    background = Color.White,
    onBackground = MinimalInk,
    surface = Color.White,
    onSurface = MinimalInk,
    surfaceVariant = BackgroundSubtle,
    onSurfaceVariant = MinimalMuted,
    outline = MinimalOutline,
    outlineVariant = ElectricBorderSubtle,
    error = ActiveIconRed,
    onError = Color.White,
    errorContainer = ErrorBg,
    onErrorContainer = ActiveIconRed
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimarySoft,
    onPrimary = DarkBackground,
    primaryContainer = Primary,
    onPrimaryContainer = Color.White,
    secondary = PrimarySoft,
    onSecondary = DarkBackground,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceAlt,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = DarkBorder,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorBg,
    onErrorContainer = ErrorRed
)

@Composable
fun SwapSheTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
