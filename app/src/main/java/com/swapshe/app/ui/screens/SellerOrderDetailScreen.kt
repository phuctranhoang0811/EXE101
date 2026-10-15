package com.swapshe.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.swapshe.app.model.OrderStatus
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.ui.components.OrderTimelineView
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SellerOrderDetailScreen(
    orderId: String,
    onNavigateBack: () -> Unit,
    onNavigateToReport: (String, String) -> Unit
) {
    val repository = SwapSheRepository.instance
    val sellerOrders by repository.sellerOrders.collectAsState()
    val order = sellerOrders.find { it.id == orderId }

    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)

    if (order == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmCream)
                .statusBarsPadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Không tìm thấy đơn hàng bán này.", color = TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(text = "Quay lại", onClick = onNavigateBack)
        }
        return
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
                text = "Chi tiết đơn bán #${order.internalCode}",
                fontFamily = FontFamily.Serif,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Buyer Shipping Address
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                        Text(
                            text = " Thông tin người nhận hàng",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "${order.shippingAddress.fullName} • ${order.shippingAddress.phone}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text(text = order.shippingAddress.fullAddressText, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(top = 2.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Product snapshot
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = order.item.images.firstOrNull(),
                        contentDescription = order.item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(text = order.item.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Size: ${order.item.size} • ${order.item.condition.label}", fontSize = 12.sp, color = TextSecondary)
                        Text(text = "Giá bán: ${formatter.format(order.subtotal)}đ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Packing Checklist
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BackgroundSubtle,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "📦 Hướng dẫn đóng gói chuẩn SWAPSHE:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "• Giặt ủi sạch sẽ, kiểm tra kĩ cúc áo/khoá kéo trước khi gói.", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                    Text(text = "• Gói bằng hộp carton hoặc túi bọc bảo vệ chống ướt mưa.", fontSize = 11.sp, color = TextSecondary)
                    Text(text = "• Ghi rõ mã đơn #${order.internalCode} lên bề mặt kiện hàng.", fontSize = 11.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timeline
            Text(text = "Tiến trình đơn hàng:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            OrderTimelineView(events = order.timeline)

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom Actions based on Seller Status
        Surface(
            color = SurfaceWhite,
            shadowElevation = 8.dp,
            modifier = Modifier.navigationBarsPadding()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                when (order.status) {
                    OrderStatus.PLACED, OrderStatus.PAID -> {
                        PrimaryButton(
                            text = "✓ Xác nhận đã chuẩn bị xong hàng",
                            onClick = {
                                repository.updateSellerOrderStatus(order.id, OrderStatus.PREPARING)
                            }
                        )
                    }
                    OrderStatus.PREPARING -> {
                        PrimaryButton(
                            text = "🚚 Giao cho bưu tá GHN (Mô phỏng)",
                            onClick = {
                                repository.updateSellerOrderStatus(order.id, OrderStatus.SHIPPED)
                            }
                        )
                    }
                    OrderStatus.SHIPPED -> {
                        SecondaryButton(
                            text = "Mô phỏng bưu tá đã giao tới người mua",
                            onClick = {
                                repository.updateSellerOrderStatus(order.id, OrderStatus.DELIVERED)
                            }
                        )
                    }
                    else -> {
                        SecondaryButton(
                            text = "Báo cáo sự cố về đơn này",
                            onClick = {
                                onNavigateToReport("ORDER", order.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
