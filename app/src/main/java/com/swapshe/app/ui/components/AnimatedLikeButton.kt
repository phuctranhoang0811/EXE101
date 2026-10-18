package com.swapshe.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.PrimarySoft
import com.swapshe.app.theme.TextPrimary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedLikeButton(
    isLiked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    iconSize: Dp = 19.dp
) {
    val scaleAnim = remember { Animatable(1f) }
    val burstProgress = remember { Animatable(0f) }

    LaunchedEffect(isLiked) {
        if (isLiked) {
            scaleAnim.animateTo(
                targetValue = 1.35f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            scaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy)
            )
        } else {
            scaleAnim.animateTo(targetValue = 0.85f, animationSpec = tween(100))
            scaleAnim.animateTo(targetValue = 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
    }

    LaunchedEffect(isLiked) {
        if (isLiked) {
            burstProgress.snapTo(0f)
            burstProgress.animateTo(1f, animationSpec = tween(400))
        } else {
            burstProgress.snapTo(0f)
        }
    }

    val haptic = LocalHapticFeedback.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.92f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggle()
                }
            )
    ) {
        if (burstProgress.value > 0f && burstProgress.value < 1f) {
            Canvas(modifier = Modifier.size(size)) {
                val center = Offset(size.toPx() / 2, size.toPx() / 2)
                val progress = burstProgress.value
                val maxRadius = size.toPx() * 0.75f
                val currentRadius = maxRadius * progress
                val alpha = (1f - progress).coerceIn(0f, 1f)

                if (progress < 0.65f) {
                    drawCircle(
                        color = ErrorRed.copy(alpha = alpha * 0.45f),
                        radius = currentRadius,
                        center = center,
                        style = Stroke(width = (2.dp.toPx() * (1f - progress)).coerceAtLeast(0.5f))
                    )
                }

                val dotCount = 6
                val dotRadius = (2.2.dp.toPx() * (1f - progress)).coerceAtLeast(0.5f)
                val particleDistance = currentRadius * 1.15f

                for (i in 0 until dotCount) {
                    val angle = Math.toRadians((i * (360.0 / dotCount)).toDouble())
                    val x = center.x + (particleDistance * cos(angle)).toFloat()
                    val y = center.y + (particleDistance * sin(angle)).toFloat()
                    val dotColor = if (i % 2 == 0) ErrorRed.copy(alpha = alpha) else PrimarySoft.copy(alpha = alpha)
                    drawCircle(color = dotColor, radius = dotRadius, center = Offset(x, y))
                }
            }
        }

        Icon(
            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Wishlist",
            tint = if (isLiked) com.swapshe.app.theme.ActiveIconRed else TextPrimary,
            modifier = Modifier
                .size(iconSize)
                .scale(scaleAnim.value)
        )
    }
}
