package com.swapshe.app.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.swapshe.app.model.ConditionType
import com.swapshe.app.model.Product
import com.swapshe.app.model.ProductStatus
import com.swapshe.app.theme.ActiveIconRed
import com.swapshe.app.theme.AccentLime
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.ErrorBg
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.SuccessBg
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.WarningAmber
import com.swapshe.app.theme.WarningBg
import java.text.DecimalFormat

@Composable
fun ConditionBadge(
    condition: ConditionType,
    grade: com.swapshe.app.model.SwapSheGrade? = null,
    modifier: Modifier = Modifier
) {
    val (bg, textCol) = when (condition) {
        ConditionType.LIKE_NEW -> SuccessBg to SuccessGreen
        ConditionType.EXCELLENT -> Color(0xFFEBF3F7) to Color(0xFF4A7A96)
        ConditionType.GOOD -> WarningBg to WarningAmber
        ConditionType.FAIR -> ErrorBg to ErrorRed
    }

    Box(
        modifier = modifier
            .background(bg, RoundedCornerShape(100.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        val gradePrefix = if (grade != null) "${grade.code} • " else ""
        Text(
            text = "$gradePrefix${condition.label}",
            color = textCol,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onLikeToggle: () -> Unit,
    modifier: Modifier = Modifier,
    matchBadgeText: String? = null
) {
    val formatter = DecimalFormat("#,###")
    val isSold = product.status == ProductStatus.SOLD
    val isReserved = product.status == ProductStatus.RESERVED

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "cardScale"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPressed) 1.dp else 2.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .scale(cardScale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Column {
            // Image with 3:4 ratio & shimmer background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .background(shimmerBrush())
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.images.firstOrNull() ?: "")
                        .crossfade(true)
                        .build(),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Status Tag or Condition Badge
                if (isSold) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(TextPrimary.copy(alpha = 0.88f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "SOLD OUT",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (isReserved) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(WarningBg, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "RESERVED",
                            color = WarningAmber,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    ConditionBadge(
                        condition = product.condition,
                        grade = product.grade,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    )
                }

                // Animated Heart Wishlist Button with Particle Burst
                AnimatedLikeButton(
                    isLiked = product.isLiked,
                    onToggle = onLikeToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                )

                // Match percentage badge (Flow E: Fit Assistant / Style match)
                if (matchBadgeText != null && !isSold) {
                    Surface(
                        shape = RoundedCornerShape(topEnd = 8.dp),
                        color = Primary,
                        modifier = Modifier.align(Alignment.BottomStart)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = AccentLime,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = matchBadgeText,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Info Details
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "${product.brand} • Size ${product.size}",
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.title,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "${formatter.format(product.price)}đ",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        softWrap = false
                    )
                    if (product.originalPrice != null) {
                        Text(
                            text = "${formatter.format(product.originalPrice)}đ",
                            fontSize = 11.sp,
                            color = TextMuted,
                            textDecoration = TextDecoration.LineThrough,
                            maxLines = 1,
                            softWrap = false,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Horizontal Special Offer Product Card matching the modern minimal fashion look from screenshot.
 */
@Composable
fun ProductOfferCard(
    product: Product,
    onClick: () -> Unit,
    onLikeToggle: () -> Unit,
    onShopNow: () -> Unit = onClick,
    onVirtualFit: () -> Unit = {},
    isHighlighted: Boolean = false,
    quantityMultiplier: String = "x2",
    modifier: Modifier = Modifier
) {
    val priceFormatted = "$${product.price / 1000},00"
    val haptic = LocalHapticFeedback.current

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF8F8FA),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEFEFF2)),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top Section: Thumbnail (Left) + Details (Center) + Heart & Multiplier (Right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Product Thumbnail in white rounded container
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    modifier = Modifier.size(86.dp)
                ) {
                    AsyncImage(
                        model = product.images.firstOrNull() ?: "",
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Center info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF18181B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Size : ${product.size}",
                        fontSize = 12.sp,
                        color = Color(0xFF8E8E93)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = priceFormatted,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF18181B)
                    )
                }

                // Right: Heart Button in Circle + Quantity
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                color = if (product.isLiked) ActiveIconRed.copy(alpha = 0.12f) else Color.White,
                                shape = CircleShape
                            )
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onLikeToggle()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Like",
                            tint = if (product.isLiked) ActiveIconRed else Color(0xFFB0B0B8),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = quantityMultiplier,
                        fontSize = 12.sp,
                        color = Color(0xFF8E8E93),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Section: Estimate Total (Left) + Shop Now & Circle Button (Right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Estimate Total
                Column {
                    Text(
                        text = "Estimate Total",
                        fontSize = 11.sp,
                        color = Color(0xFF8E8E93)
                    )
                    Text(
                        text = priceFormatted,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF18181B)
                    )
                }

                // Shop Now + Circle Try-on Button
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = if (isHighlighted) Color(0xFF18181B) else Color(0xFFF0F0F3),
                        modifier = Modifier.clickable(onClick = onShopNow)
                    ) {
                        Text(
                            text = "Shop Now",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isHighlighted) Color.White else Color(0xFF55555C),
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = if (isHighlighted) Color(0xFF18181B) else Color(0xFFF0F0F3),
                                shape = CircleShape
                            )
                            .clickable(onClick = onVirtualFit),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Checkroom,
                            contentDescription = "Try-on",
                            tint = if (isHighlighted) Color.White else Color(0xFF55555C),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}
