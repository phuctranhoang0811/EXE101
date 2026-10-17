package com.swapshe.app.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted

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
    onShowPlaceholderToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    val items = listOf(
        NavItem(Screen.Home.route, "Home", Icons.Filled.Home),
        NavItem(Screen.Explore.route, "Explore", Icons.Filled.Search),
        NavItem(Screen.MyCloset.route, "Sell", Icons.Filled.Add, isHeroButton = true),
        NavItem(Screen.SwapDiscovery.route, "Swap", Icons.Filled.SwapHoriz),
        NavItem(Screen.Profile.route, "Profile", Icons.Filled.Person)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = BorderColor, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                val interactionSource = remember(item.route) { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()

                val itemScale by animateFloatAsState(
                    targetValue = if (isPressed) 0.90f else (if (isSelected) 1.05f else 1.0f),
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
                    label = "tabScale"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
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
                    if (item.isHeroButton) {
                        // Center Floating Hero Sell Button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .scale(itemScale)
                                .padding(horizontal = 4.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .shadow(6.dp, CircleShape, spotColor = Primary.copy(alpha = 0.4f))
                                    .background(Primary, CircleShape)
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                text = item.title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    } else {
                        val activeColor by animateColorAsState(
                            targetValue = if (isSelected) Primary else TextMuted,
                            animationSpec = tween(180),
                            label = "activeColor"
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .scale(itemScale)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) PrimarySoft15 else Color.Transparent)
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = activeColor,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = item.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = activeColor,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
