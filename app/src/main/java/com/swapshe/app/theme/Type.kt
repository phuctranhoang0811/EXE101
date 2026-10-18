package com.swapshe.app.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

// Unified modern Sans-serif typography for clean minimal aesthetic
private val uiFont = FontFamily.SansSerif

val PlayfairFontFamily = uiFont // Unified to clean Sans-serif as per Guide section 4.1
val InterFontFamily = uiFont

private val DefaultLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextPrimary
    ),
    headlineSmall = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextPrimary
    ),
    titleLarge = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextPrimary
    ),
    titleSmall = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextPrimary
    ),
    bodyLarge = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextSecondary
    ),
    bodySmall = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextMuted
    ),
    labelLarge = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextPrimary
    ),
    labelMedium = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextSecondary
    ),
    labelSmall = TextStyle(
        fontFamily = uiFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.3.sp,
        lineHeightStyle = DefaultLineHeightStyle,
        color = TextMuted
    )
)

val SwapSheTypography = Typography
