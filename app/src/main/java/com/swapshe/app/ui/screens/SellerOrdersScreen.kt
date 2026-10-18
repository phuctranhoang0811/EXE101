package com.swapshe.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.Order
import com.swapshe.app.model.OrderStatus
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.theme.WarningAmber
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SellerOrdersScreen(
    onNavigateBack: () -> Unit,
    onNavigateToOrderDetail: (String) -> Unit
) {
    val repository = SwapSheRepository.instance
    val sellerOrders by repository.sellerOrders.collectAsState()

    val tabs = listOf("Tất cả", "Chờ chuẩn bị", "Đang giao", "Hoàn thành")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val filteredOrders = when (selectedTabIndex) {
        1 -> sellerOrders.filter { it.status == OrderStatus.PREPARING || it.status == OrderStatus.PAID || it.status == OrderStatus.PLACED }
        2 -> sellerOrders.filter { it.status == OrderStatus.SHIPPED || it.status == OrderStatus.DELIVERED }
        3 -> sellerOrders.filter { it.status == OrderStatus.COMPLETED }
        else -> sellerOrders
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "Quản lý đơn bán",
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = SurfaceWhite,
            contentColor = Primary,
            edgePadding = 16.dp,
            divider = { HorizontalDivider(color = BorderColor) }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTabIndex == index) Primary else TextSecondary
                        )
                    }
                )
            }
        }

        if (filteredOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Outlined.ShoppingBag, contentDescription = null, tint = TextMuted, modifier = Modifier.size(56.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Chưa có đơn bán nào ở mục này", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text(text = "Khi có người mua đồ từ tủ của bạn, đơn hàng sẽ hiển thị tại đây.", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredOrders, key = { it.id }) { order ->
                    SellerOrderCard(
                        order = order,
                        onClick = { onNavigateToOrderDetail(order.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SellerOrderCard(
    order: Order,
    onClick: () -> Unit
) {
    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.internalCode,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f, fill = false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Status chip
                val (bg, txt, clr) = when (order.status) {
                    OrderStatus.PLACED, OrderStatus.PAID -> Triple(WarningAmber.copy(alpha = 0.15f), "Đã thanh toán • Chờ gửi", WarningAmber)
                    OrderStatus.PREPARING -> Triple(Primary.copy(alpha = 0.12f), "Đang chuẩn bị hàng", Primary)
                    OrderStatus.SHIPPED -> Triple(SuccessGreen.copy(alpha = 0.15f), "Đang vận chuyển", SuccessGreen)
                    OrderStatus.DELIVERED -> Triple(SuccessGreen.copy(alpha = 0.15f), "Đã giao đến người mua", SuccessGreen)
                    OrderStatus.COMPLETED -> Triple(SuccessGreen.copy(alpha = 0.15f), "Giao dịch hoàn tất", SuccessGreen)
                    OrderStatus.CANCELLED -> Triple(ErrorRed.copy(alpha = 0.12f), "Đã hủy", ErrorRed)
                    OrderStatus.DISPUTED -> Triple(ErrorRed.copy(alpha = 0.15f), "Có khiếu nại", ErrorRed)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = bg
                ) {
                    Text(
                        text = txt,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = clr,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = order.item.images.firstOrNull(),
                    contentDescription = order.item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                    Text(
                        text = order.item.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Người mua: ${order.shippingAddress.fullName}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = "Thu về: ${formatter.format(order.subtotal)}đ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted)
            }
        }
    }
}
