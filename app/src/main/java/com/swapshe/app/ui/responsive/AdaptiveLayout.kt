package com.swapshe.app.ui.responsive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowWidthClass {
    Compact,   // Phones in portrait (< 600dp)
    Medium,    // Foldables, small tablets, phones in landscape (600dp - 839dp)
    Expanded   // Tablets, desktop displays (>= 840dp)
}

enum class WindowHeightClass {
    Compact,   // Phones in landscape (< 480dp)
    Medium,    // Standard portrait devices (480dp - 899dp)
    Expanded   // Tall tablets & displays (>= 900dp)
}

data class WindowSizeInfo(
    val widthClass: WindowWidthClass,
    val heightClass: WindowHeightClass,
    val widthDp: Dp,
    val heightDp: Dp
) {
    val isWideScreen: Boolean get() = widthClass != WindowWidthClass.Compact
    val isTabletOrLandscape: Boolean get() = widthDp >= 600.dp
    val isExpandedScreen: Boolean get() = widthClass == WindowWidthClass.Expanded
    val isCompactPhone: Boolean get() = widthClass == WindowWidthClass.Compact
    val isLandscapePhone: Boolean get() = widthDp >= 600.dp && heightDp < 480.dp

    fun productGridColumns(): Int = when (widthClass) {
        WindowWidthClass.Compact -> 2
        WindowWidthClass.Medium -> 3
        WindowWidthClass.Expanded -> if (widthDp >= 1200.dp) 5 else 4
    }
}

val LocalWindowSizeInfo = compositionLocalOf {
    WindowSizeInfo(
        widthClass = WindowWidthClass.Compact,
        heightClass = WindowHeightClass.Medium,
        widthDp = 360.dp,
        heightDp = 640.dp
    )
}

@Composable
fun rememberWindowSizeInfo(): WindowSizeInfo {
    val configuration = LocalConfiguration.current
    val widthDp = configuration.screenWidthDp.dp
    val heightDp = configuration.screenHeightDp.dp

    return remember(widthDp, heightDp) {
        val widthClass = when {
            widthDp < 600.dp -> WindowWidthClass.Compact
            widthDp < 840.dp -> WindowWidthClass.Medium
            else -> WindowWidthClass.Expanded
        }
        val heightClass = when {
            heightDp < 480.dp -> WindowHeightClass.Compact
            heightDp < 900.dp -> WindowHeightClass.Medium
            else -> WindowHeightClass.Expanded
        }
        WindowSizeInfo(widthClass, heightClass, widthDp, heightDp)
    }
}

@Composable
fun ProvideWindowSizeInfo(
    content: @Composable () -> Unit
) {
    val windowSizeInfo = rememberWindowSizeInfo()
    CompositionLocalProvider(LocalWindowSizeInfo provides windowSizeInfo) {
        content()
    }
}

fun Modifier.responsiveContentWidth(maxWidth: Dp = 680.dp): Modifier = this
    .fillMaxWidth()
    .wrapContentWidth(Alignment.CenterHorizontally)
    .widthIn(max = maxWidth)

fun Modifier.responsiveContainerWidth(maxWidth: Dp = 1200.dp): Modifier = this
    .fillMaxWidth()
    .wrapContentWidth(Alignment.CenterHorizontally)
    .widthIn(max = maxWidth)
