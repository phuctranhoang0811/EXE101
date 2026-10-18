package com.swapshe.app.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.MinimalInk
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextSecondary

import com.swapshe.app.theme.ActiveIconRed

data class NavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val isHeroButton: Boolean = false,
    val isPlaceholder: Boolean = false,
    val placeholderMsg: String? = null
)

@Composable
fun BottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onShowPlaceholderToast: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(Screen.Home.route, "Home", Icons.Outlined.Home),
        NavItem(Screen.Explore.route, "Shop", Icons.Outlined.ShoppingBag),
        NavItem(Screen.SwapDiscovery.route, "Swap", Icons.Filled.SwapHoriz),
        NavItem(Screen.MyCloset.route, "Sell", Icons.Filled.Add, isHeroButton = true),
        NavItem(Screen.Profile.route, "Profile", Icons.Outlined.Person)
    )

    val selectedIndex = remember(currentRoute) {
        val idx = items.indexOfFirst { item ->
            when (item.route) {
                Screen.Home.route -> currentRoute == Screen.Home.route
                Screen.Explore.route -> currentRoute == Screen.Explore.route
                Screen.SwapDiscovery.route -> currentRoute in listOf(
                    Screen.SwapDiscovery.route,
                    Screen.SwapRequests.route,
                    Screen.CreateSwapOffer.route,
                    Screen.SwapDetail.route
                )
                Screen.MyCloset.route -> currentRoute in listOf(
                    Screen.MyCloset.route,
                    Screen.CreateEditListing.route,
                    Screen.SellerOrders.route,
                    Screen.SellerOrderDetail.route
                )
                Screen.Profile.route -> currentRoute in listOf(
                    Screen.Profile.route,
                    Screen.MyOrders.route,
                    Screen.Wallet.route,
                    Screen.Ekyc.route
                )
                else -> currentRoute == item.route
            }
        }
        if (idx >= 0) idx else 0
    }

    val glassShape = RoundedCornerShape(32.dp)

    // Ultra-refined Glassmorphism frosted acrylic translucency (Monochrome)
    val glassBackgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xD9FFFFFF), // 85% frosted white top rim
            Color(0x66FFFFFF), // 40% translucent core revealing content beneath
            Color(0x8CFFFFFF)  // 55% crisp monochrome frosted base
        )
    )

    // Specular light refraction on bevel border (crystal glass rim reflection)
    val glassBorderBrush = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.95f),   // Crisp specular top edge reflection
            Color.White.copy(alpha = 0.35f),   // Subdued mid glass diffusion
            Color(0x20000000),                 // Subtle neutral rim refraction
            Color.White.copy(alpha = 0.65f)    // Lower lip light catch
        )
    )

    // Curved mirror reflection sheen across upper glass surface
    val glassGleamBrush = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.55f),
            Color.White.copy(alpha = 0.12f),
            Color.Transparent
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = glassShape,
            color = Color.Transparent,
            shadowElevation = 20.dp,
            border = BorderStroke(1.2.dp, glassBorderBrush),
            modifier = Modifier
                .widthIn(min = 310.dp, max = 345.dp)
                .height(62.dp)
                .clip(glassShape)
                .background(glassBackgroundBrush)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Specular reflection shimmer layer on top half
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(glassGleamBrush)
                )

                // Glass lens & navigation row
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    val tabWidth = maxWidth / items.size

                    // Smooth Sliding Frosted Glass Lens with physics damping
                    val animatedIndicatorOffset by animateDpAsState(
                        targetValue = tabWidth * selectedIndex,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        ),
                        label = "slidingGlassLens"
                    )

                    // Sliding glass pill lens
                    Box(
                        modifier = Modifier
                            .offset(x = animatedIndicatorOffset)
                            .width(tabWidth)
                            .fillMaxHeight()
                            .padding(vertical = 6.dp, horizontal = 3.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.85f),
                                        Color.White.copy(alpha = 0.40f)
                                    )
                                )
                            )
                            .border(
                                width = 1.2.dp,
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 1.0f),
                                        Color.White.copy(alpha = 0.30f)
                                    )
                                ),
                                shape = RoundedCornerShape(22.dp)
                            )
                    )

                    // Navigation Items Row
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items.forEachIndexed { index, item ->
                            val isSelected = index == selectedIndex
                            val interactionSource = remember(item.route) { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()

                            val itemScale by animateFloatAsState(
                                targetValue = if (isPressed) 0.85f else (if (isSelected) 1.15f else 1.0f),
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                ),
                                label = "tabScale"
                            )

                            val activeColor by animateColorAsState(
                                targetValue = if (isSelected) Color(0xFF18181B) else Color(0xFF8E8E93),
                                animationSpec = tween(200),
                                label = "activeColor"
                            )

                                    val haptic = LocalHapticFeedback.current
                                    Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .scale(itemScale)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onNavigate(item.route)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (item.isHeroButton) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                if (isSelected) Color(0xFF18181B) else Color(0xFF70707D),
                                                CircleShape
                                            )
                                    ) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                } else {
                                    val iconVector = when {
                                        isSelected && item.title == "Shop" -> Icons.Filled.ShoppingBag
                                        isSelected && item.title == "Home" -> Icons.Filled.Home
                                        isSelected && item.title == "Profile" -> Icons.Filled.Person
                                        else -> item.icon
                                    }
                                    Icon(
                                        imageVector = iconVector,
                                        contentDescription = item.title,
                                        tint = activeColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigationRail(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onShowPlaceholderToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(Screen.Home.route, "Home", Icons.Filled.Home),
        NavItem(Screen.Explore.route, "Explore", Icons.Filled.Search),
        NavItem(Screen.MyCloset.route, "Sell", Icons.Filled.Add, isHeroButton = true),
        NavItem(Screen.SwapDiscovery.route, "Swap", Icons.Filled.SwapHoriz),
        NavItem(Screen.Profile.route, "Profile", Icons.Filled.Person)
    )

    Row(
        modifier = modifier
            .fillMaxHeight()
            .background(SurfaceWhite)
    ) {
        Column(
            modifier = Modifier
                .width(84.dp)
                .fillMaxHeight()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top Mini Logo
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.swapshe.app.R.drawable.logo),
                    contentDescription = "SwapShe Logo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                Text(
                    text = "SWAPSHE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    letterSpacing = 1.sp
                )

                androidx.compose.material3.HorizontalDivider(
                    color = BorderColor,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                )

                // Nav items
                items.forEach { item ->
                    val isSelected = currentRoute == item.route
                    val interactionSource = remember(item.route) { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()

                    val itemScale by animateFloatAsState(
                        targetValue = if (isPressed) 0.90f else (if (isSelected) 1.05f else 1.0f),
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "railItemScale"
                    )

                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .scale(itemScale)
                            .background(Color.Transparent)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                if (item.isPlaceholder) {
                                    item.placeholderMsg?.let { onShowPlaceholderToast(it) }
                                } else {
                                    onNavigate(item.route)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val activeColor by animateColorAsState(
                            targetValue = if (isSelected) ActiveIconRed else TextSecondary,
                            animationSpec = tween(180),
                            label = "railActiveColor"
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier.size(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (item.isHeroButton) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(if (isSelected) ActiveIconRed else MinimalInk, CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title,
                                        tint = activeColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = activeColor,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
        }

        androidx.compose.material3.VerticalDivider(color = BorderColor, thickness = 1.dp)
    }
}

