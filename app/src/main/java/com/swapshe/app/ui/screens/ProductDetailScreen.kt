package com.swapshe.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import com.swapshe.app.ui.components.AnimatedLikeButton
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.Product
import com.swapshe.app.model.ProductStatus
import com.swapshe.app.ui.components.AuthGateModal
import com.swapshe.app.ui.components.ConditionBadge
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.theme.WarningAmber
import java.text.DecimalFormat

@Composable
fun ProductDetailScreen(
    productId: String,
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onNavigateToSeller: (String) -> Unit,
    onNavigateToAuth: (isRegister: Boolean) -> Unit,
    onNavigateToSwapOffer: (String) -> Unit = {},
    onNavigateToReport: (String, String) -> Unit = { _, _ -> }
) {
    val repository = SwapSheRepository.instance
    val products by repository.products.collectAsState()
    val user by repository.currentUser.collectAsState()
    val product = products.find { it.id == productId } ?: products.first()

    var showAuthGate by remember { mutableStateOf(false) }

    val formatter = DecimalFormat("#,###")
    val pagerState = rememberPagerState(pageCount = { product.images.size })

    val isAvailable = product.status == ProductStatus.AVAILABLE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "Chi tiết sản phẩm",
                fontFamily = FontFamily.Serif,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onNavigateToReport("LISTING", product.id) }) {
                    Icon(
                        imageVector = Icons.Outlined.Flag,
                        contentDescription = "Report",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
                AnimatedLikeButton(
                    isLiked = product.isLiked,
                    onToggle = { repository.toggleLike(product.id) }
                )
            }
        }

        // Scrollable Body Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Photo Carousel (3:4 ratio)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .background(BackgroundSubtle)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = product.images[page],
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Animated Worm Pill Dot Indicators
                if (product.images.size > 1) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(product.images.size) { index ->
                            val isCurrent = pagerState.currentPage == index
                            val dotWidth by animateDpAsState(
                                targetValue = if (isCurrent) 22.dp else 6.dp,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                                label = "dotWidth"
                            )
                            val dotColor by animateColorAsState(
                                targetValue = if (isCurrent) Primary else Color.White.copy(alpha = 0.65f),
                                animationSpec = tween(200),
                                label = "dotColor"
                            )
                            Box(
                                modifier = Modifier
                                    .height(6.dp)
                                    .width(dotWidth)
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(dotColor)
                            )
                        }
                    }
                }
            }

            // Info Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.brand,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    ConditionBadge(condition = product.condition)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = product.title,
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                // Price display
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "${formatter.format(product.price)}đ",
                        fontFamily = FontFamily.Serif,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    if (product.originalPrice != null) {
                        Text(
                            text = "${formatter.format(product.originalPrice)}đ",
                            fontSize = 14.sp,
                            color = TextMuted,
                            textDecoration = TextDecoration.LineThrough,
                            modifier = Modifier.padding(start = 10.dp, bottom = 2.dp)
                        )
                    }
                }

                // Tags row
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Size ${product.size}", product.style, product.color).forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 18.dp))

                // Size & Fit / Measurements
                Text(
                    text = "Thông số thực tế (Size & Fit)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Được người bán đo tay trực tiếp trên sản phẩm:",
                    fontSize = 12.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceWhite,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        product.measurements.bust?.let {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Vòng ngực", fontSize = 11.sp, color = TextMuted)
                                Text(text = "$it cm", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }
                        product.measurements.length?.let {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Chiều dài", fontSize = 11.sp, color = TextMuted)
                                Text(text = "$it cm", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }
                        product.measurements.waist?.let {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Vòng eo", fontSize = 11.sp, color = TextMuted)
                                Text(text = "$it cm", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }
                    }
                }

                HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 18.dp))

                // Description
                Text(
                    text = "Mô tả từ người bán",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = product.description,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 6.dp)
                )

                HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 18.dp))

                // Seller Card Preview
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToSeller(product.seller.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = product.seller.avatarUrl,
                            contentDescription = product.seller.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )

                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .weight(1f)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = product.seller.shopName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                if (product.seller.isVerified) {
                                    Icon(
                                        imageVector = Icons.Outlined.Shield,
                                        contentDescription = "Verified",
                                        tint = Primary,
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .size(14.dp)
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = WarningAmber,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = " ${product.seller.rating} (${product.seller.successfulTransactions} đã bán)",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = null,
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Shipping info note
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceWhite, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocalShipping,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = " Đồng giá ship toàn quốc 25.000đ • Kiểm tra trước khi nhận",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Sticky Bottom CTA Bar
        Surface(
            color = SurfaceWhite,
            shadowElevation = 8.dp,
            modifier = Modifier.navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isAvailable) {
                    val statusText = if (product.status == ProductStatus.SOLD)
                        "Sản phẩm đã bán (Sold Out)" else "Đang có người giữ chỗ (Reserved)"
                    PrimaryButton(
                        text = statusText,
                        enabled = false,
                        onClick = {}
                    )
                } else {
                    if (product.canSwap) {
                        SecondaryButton(
                            text = "Đề xuất đổi đồ 🔄",
                            onClick = {
                                if (user.isGuest) {
                                    showAuthGate = true
                                } else {
                                    onNavigateToSwapOffer(product.id)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    PrimaryButton(
                        text = "Mua ngay",
                        onClick = {
                            if (user.isGuest) {
                                repository.setPendingCheckout(product)
                                showAuthGate = true
                            } else {
                                repository.setPendingCheckout(product)
                                onNavigateToCheckout()
                            }
                        },
                        modifier = Modifier.weight(if (product.canSwap) 1f else 2f)
                    )
                }
            }
        }
    }

    // Auth Gate Modal for Guest
    AuthGateModal(
        isVisible = showAuthGate,
        onDismiss = { showAuthGate = false },
        onLoginClick = {
            showAuthGate = false
            onNavigateToAuth(false)
        },
        onRegisterClick = {
            showAuthGate = false
            onNavigateToAuth(true)
        }
    )
}
