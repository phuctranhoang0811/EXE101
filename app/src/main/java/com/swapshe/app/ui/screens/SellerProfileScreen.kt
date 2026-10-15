package com.swapshe.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.MockData
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.theme.WarningAmber
import com.swapshe.app.ui.components.ProductCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SellerProfileScreen(
    sellerId: String,
    onNavigateBack: () -> Unit,
    onNavigateToProduct: (String) -> Unit
) {
    val repository = SwapSheRepository.instance
    val products by repository.products.collectAsState()

    val seller = if (sellerId == MockData.defaultSellerAn.id) {
        MockData.defaultSellerAn
    } else {
        MockData.defaultSellerLinh
    }

    val sellerListings = remember(products, seller.id) { products.filter { it.seller.id == seller.id } }
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "Hồ sơ người bán",
                fontFamily = FontFamily.Serif,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            IconButton(onClick = {
                repository.showToast("Đã ghi nhận báo cáo hồ sơ người bán.")
            }) {
                Icon(imageVector = Icons.Outlined.Flag, contentDescription = "Report", tint = TextMuted)
            }
        }

        // Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Seller Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                AsyncImage(
                    model = seller.avatarUrl,
                    contentDescription = seller.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                )

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = seller.shopName,
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (seller.isVerified) {
                            Icon(
                                imageVector = Icons.Outlined.Shield,
                                contentDescription = "Verified",
                                tint = Primary,
                                modifier = Modifier
                                    .padding(start = 6.dp)
                                    .size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = "${seller.name} • ${seller.location}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(14.dp))
                        Text(
                            text = " ${seller.rating} (${seller.reviewCount} đánh giá) • ${seller.successfulTransactions} đã bán",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                }
            }

            Text(
                text = seller.bio,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 12.dp))

            // Segmented Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val tab0Bg by animateColorAsState(if (selectedTab == 0) Primary else SurfaceWhite, tween(200), label = "tab0Bg")
                val tab0Text by animateColorAsState(if (selectedTab == 0) Color.White else TextPrimary, tween(200), label = "tab0Text")
                val tab1Bg by animateColorAsState(if (selectedTab == 1) Primary else SurfaceWhite, tween(200), label = "tab1Bg")
                val tab1Text by animateColorAsState(if (selectedTab == 1) Color.White else TextPrimary, tween(200), label = "tab1Text")

                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = tab0Bg,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clickable { selectedTab = 0 }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Tủ đồ (${sellerListings.size})",
                            color = tab0Text,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = tab1Bg,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clickable { selectedTab = 1 }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Đánh giá (${seller.reviews.size})",
                            color = tab1Text,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Tab Content
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(150)) },
                label = "tabContent"
            ) { tabIndex ->
                if (tabIndex == 0) {
                    FlowRow(
                        maxItemsInEachRow = 2,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        sellerListings.forEach { product ->
                            ProductCard(
                                product = product,
                                onClick = { onNavigateToProduct(product.id) },
                                onLikeToggle = { repository.toggleLike(product.id) },
                                modifier = Modifier.fillMaxWidth(0.478f)
                            )
                        }
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        seller.reviews.forEach { rev ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = SurfaceWhite,
                                shadowElevation = 1.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            AsyncImage(
                                                model = rev.reviewerAvatar,
                                                contentDescription = rev.reviewerName,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                            )
                                            Column(modifier = Modifier.padding(start = 10.dp)) {
                                                Text(
                                                    text = rev.reviewerName,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = TextPrimary
                                                )
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    repeat(rev.rating) {
                                                        Icon(
                                                            imageVector = Icons.Filled.Star,
                                                            contentDescription = null,
                                                            tint = WarningAmber,
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        Text(
                                            text = rev.date,
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    }

                                    Text(
                                        text = rev.comment,
                                        fontSize = 13.sp,
                                        color = TextSecondary,
                                        lineHeight = 18.sp,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .background(BackgroundSubtle, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Đã mua: ${rev.productName}",
                                            fontSize = 11.sp,
                                            color = TextMuted,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
