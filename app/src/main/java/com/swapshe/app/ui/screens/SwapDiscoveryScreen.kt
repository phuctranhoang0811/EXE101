package com.swapshe.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.Product
import com.swapshe.app.model.ProductStatus
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SwapDiscoveryScreen(
    onNavigateToProduct: (String) -> Unit,
    onNavigateToSwapRequests: () -> Unit
) {
    val repository = SwapSheRepository.instance
    val user by repository.currentUser.collectAsState()
    val products by repository.products.collectAsState()
    val swapOffers by repository.swapOffers.collectAsState()

    // Filter items that have canSwap = true, available, and not owned by current user
    val swappableItems = products.filter {
        it.canSwap && it.status == ProductStatus.AVAILABLE && it.ownerId != user.id
    }

    val pendingCount = swapOffers.count { it.status == com.swapshe.app.model.SwapOfferStatus.PENDING }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
            ) {
                Text(
                    text = "Đổi đồ 1-1",
                    fontFamily = FontFamily.Serif,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Trao đổi ngang giá trị • Không bù tiền",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 1
                )
            }

            // Shortcut to Swap Requests Inbox - sleek, compact pill badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = PrimarySoft15,
                border = androidx.compose.foundation.BorderStroke(1.dp, Primary.copy(alpha = 0.35f)),
                modifier = Modifier.clickable { onNavigateToSwapRequests() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.SwapHoriz,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Yêu cầu",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                    if (pendingCount > 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Primary
                        ) {
                            Text(
                                text = "$pendingCount",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = androidx.compose.ui.graphics.Color.White,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }

        // Swappable Grid
        if (swappableItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Filled.SwapHoriz, contentDescription = null, tint = TextMuted, modifier = Modifier.size(56.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Hiện chưa có món đồ nào mở chế độ đổi đồ", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Bạn có thể đăng đồ lên My Closet và bật tính năng Swap nhé!", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(swappableItems, key = { it.id }) { product ->
                    SwapProductGridCard(
                        product = product,
                        onClick = { onNavigateToProduct(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SwapProductGridCard(
    product: Product,
    onClick: () -> Unit
) {
    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = product.images.firstOrNull(),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                )

                // Swap Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Primary.copy(alpha = 0.92f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SwapHoriz,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Sẵn sàng Swap",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = product.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = "Size ${product.size} • ${product.condition.label}",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ước tính: ${formatter.format(product.price)}đ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                    Text(
                        text = product.seller.shopName,
                        fontSize = 10.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
