package com.swapshe.app.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// SWAPSHE MONOCHROME (BLACK & WHITE) MINIMAL DESIGN SYSTEM
// Style: Pure Black & White Minimal, Red accents for Toggles & Heart/Like
// ==========================================

// Primary Brand Colors - Pure Monochrome Deep Black
val PureBlack = Color(0xFF000000)
val JetBlack = Color(0xFF111111)
val DarkCharcoal = Color(0xFF1C1C1E)

// Global Primary Aliases -> Pure Black & White
val Primary = PureBlack
val PrimaryHover = JetBlack
val PrimaryDark = PureBlack
val PrimarySoft = DarkCharcoal
val PrimarySoft15 = Color(0x1A000000)
val PrimarySoft30 = Color(0x33000000)
val PrimaryContainer = Color(0xFFF2F2F7)

// Electric Lavender aliases mapped to Monochrome for backward compatibility
val ElectricLavender = PureBlack
val ElectricLavenderHover = JetBlack
val ElectricLavenderLight = DarkCharcoal
val ElectricLavenderSoft15 = Color(0x14000000)
val ElectricLavenderSoft30 = Color(0x28000000)
val ElectricLavenderSoft8 = Color(0x0A000000)

// Accent Lime & Pink mapped to Monochrome
val ElectricAccent = PureBlack
val ElectricAccentDark = JetBlack
val ElectricAccentSoft = Color(0x1A000000)
val AccentLime = PureBlack

val BlossomPink = PureBlack
val BlossomPinkSoft = Color(0xFFF2F2F7)
val BlossomPinkBg = Color(0xFFF8F8FA)

// Background & Surface Colors - Crisp Pure White & Subtle Minimal Grays
val MinimalBackground = Color(0xFFFFFFFF)        // Pure White background
val MinimalSurface = Color(0xFFFFFFFF)           // Pure White surface
val ElectricBackground = MinimalBackground
val ElectricSurface = MinimalSurface
val ElectricSurfaceAlt = Color(0xFFF5F5F7)
val SurfaceWhite = Color(0xFFFFFFFF)
val SurfaceAlt = Color(0xFFF5F5F7)
val WarmCream = Color(0xFFFFFFFF)                // Pure white for clean minimal aesthetic
val BackgroundSubtle = Color(0xFFF5F5F7)

// Text Colors - Pure Black & Neutral Gray
val MinimalInk = PureBlack                       // Main text primary: Pure Black
val MinimalMuted = Color(0xFF6E6E73)             // Subtitle/secondary: Apple Gray
val MinimalDisabledText = Color(0xFF8E8E93)      // Disabled text
val MinimalDisabledSurface = Color(0xFFE5E5EA)   // Disabled surface

val ElectricTextPrimary = MinimalInk
val ElectricTextSecondary = MinimalMuted
val ElectricTextMuted = Color(0xFF8E8E93)
val TextPrimary = MinimalInk
val TextSecondary = MinimalMuted
val TextMuted = Color(0xFF8E8E93)
val DisabledText = MinimalDisabledText
val DisabledSurface = MinimalDisabledSurface

// Borders & Outlines - Crisp Light Neutral Grays
val MinimalOutline = Color(0xFFE5E5EA)
val ElectricBorder = MinimalOutline
val ElectricBorderSubtle = Color(0xFFEFEFF4)
val BorderColor = MinimalOutline
val BorderSubtle = Color(0xFFEFEFF4)

// Semantic Indicators
val MinimalSuccess = Color(0xFF1E1E1E)
val SuccessGreen = Color(0xFF1E1E1E)
val SuccessBg = Color(0xFFF2F2F7)
val MinimalError = Color(0xFFFF0800)
val ErrorRed = Color(0xFFFF0800)
val ErrorBg = Color(0xFFFFEBEB)
val WarningAmber = Color(0xFF333333)
val WarningBg = Color(0xFFF2F2F7)

// Dark Theme Palette - Clean High-Contrast Dark
val DarkBackground = Color(0xFF000000)
val DarkSurface = Color(0xFF121212)
val DarkSurfaceAlt = Color(0xFF1E1E1E)
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFFB0B0B0)
val DarkTextMuted = Color(0xFF757575)
val DarkBorder = Color(0xFF2C2C2E)
val DarkPrimarySoft = Color(0xFFE5E5EA)

// Escrow & Trust Badges - Monochrome
val EscrowBlue = Color(0xFF1C1C1E)
val EscrowBg = Color(0xFFF2F2F7)
val GoldCrown = Color(0xFF1C1C1E)
val GoldBg = Color(0xFFF2F2F7)
val MintBadge = Color(0xFF1C1C1E)
val MintBadgeBg = Color(0xFFF2F2F7)

// User Request: Selected / Active Icon, Toggle & Heart Like Color -> VIBRANT RED
val ActiveIconRed = Color(0xFFFF0800)
val ActiveToggleRed = Color(0xFFFF0800)
val HeartRed = Color(0xFFFF0800)
