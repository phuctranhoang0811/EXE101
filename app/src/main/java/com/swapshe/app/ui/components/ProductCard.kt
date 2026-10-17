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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.AutoAwesome
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
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.swapshe.app.model.ConditionType
import com.swapshe.app.model.Product
import com.swapshe.app.model.ProductStatus
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
                            .background(Color(0xD944343C), RoundedCornerShape(6.dp))
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
                        color = Color(0xEB44343C),
                        modifier = Modifier.align(Alignment.BottomStart)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
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
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = product.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "${formatter.format(product.price)}đ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    if (product.originalPrice != null) {
                        Text(
                            text = "${formatter.format(product.originalPrice)}đ",
                            fontSize = 11.sp,
                            color = TextMuted,
                            textDecoration = TextDecoration.LineThrough,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
