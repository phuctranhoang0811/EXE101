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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import com.swapshe.app.ui.components.ProductRatingNotesModal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.ui.responsive.responsiveContainerWidth
import coil.compose.AsyncImage
import com.swapshe.app.data.MockData
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.Order
import com.swapshe.app.model.OrderStatus
import com.swapshe.app.ui.components.OrderTimelineView
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.ErrorBg
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.SuccessBg
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.theme.WarningAmber
import com.swapshe.app.theme.WarningBg
import java.text.DecimalFormat

import androidx.lifecycle.viewmodel.compose.viewModel
import com.swapshe.app.ui.viewmodels.OrdersViewModel

/* --------------------------------------------------------------------------
   F18: MY ORDERS SCREEN
   -------------------------------------------------------------------------- */
@Composable
fun MyOrdersScreen(
    onNavigateBack: () -> Unit,
    onNavigateToOrderDetail: (String) -> Unit,
    viewModel: OrdersViewModel = viewModel(factory = OrdersViewModel.provideFactory())
) {
    val orders by viewModel.orders.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val filteredOrders by viewModel.filteredOrders.collectAsState()
    val tabs = listOf("Tất cả", "Đang xử lý", "Hoàn tất")
    val formatter = DecimalFormat("#,###")

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "Đơn mua của tôi",
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceWhite,
            contentColor = Primary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Primary
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { viewModel.onTabSelect(index) },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        // Animated Orders list
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
            },
            label = "ordersTabContent"
        ) { tabIndex ->
            val tabOrders = when (tabIndex) {
                1 -> orders.filter { it.status == OrderStatus.PAID || it.status == OrderStatus.PREPARING || it.status == OrderStatus.SHIPPED || it.status == OrderStatus.DELIVERED }
                2 -> orders.filter { it.status == OrderStatus.COMPLETED }
                else -> orders
            }

            if (tabOrders.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(top = 48.dp)
                ) {
                    Text(text = "Chưa có đơn hàng nào trong mục này", fontSize = 14.sp, color = TextMuted)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(tabOrders, key = { it.id }) { order ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .clickable { onNavigateToOrderDetail(order.id) }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = order.internalCode,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        modifier = Modifier.weight(1f, fill = false),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    val (statusBg, statusColor) = when (order.status) {
                                        OrderStatus.COMPLETED -> SuccessBg to SuccessGreen
                                        OrderStatus.DISPUTED -> ErrorBg to ErrorRed
                                        else -> WarningBg to WarningAmber
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(statusBg, RoundedCornerShape(100.dp))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = order.status.label,
                                            color = statusColor,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }

                                HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 10.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = order.item.images.firstOrNull() ?: "",
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(54.dp).clip(RoundedCornerShape(8.dp))
                                    )
                                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                                        Text(
                                            text = order.item.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Shop: ${order.item.seller.shopName}",
                                            fontSize = 11.sp,
                                            color = TextMuted,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Tổng: ${formatter.format(order.total)}đ",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Primary,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                    Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/* --------------------------------------------------------------------------
   F19: ORDER DETAIL / TRACKING SCREEN (with F20 Review Dialog)
   -------------------------------------------------------------------------- */
@Composable
fun OrderDetailScreen(
    orderId: String,
    onNavigateBack: () -> Unit,
    onNavigateToReport: (String, String) -> Unit = { _, _ -> },
    onNavigateToDisputeDetail: (String) -> Unit = {},
    viewModel: OrdersViewModel = viewModel(factory = OrdersViewModel.provideFactory())
) {
    val orders by viewModel.orders.collectAsState()
    val order = orders.find { it.id == orderId } ?: orders.firstOrNull() ?: MockData.initialOrders.first()

    val formatter = DecimalFormat("#,###")
    val showReviewDialog by viewModel.showReviewDialog.collectAsState()
    val reviewRating by viewModel.reviewRating.collectAsState()
    val reviewComment by viewModel.reviewComment.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // App Bar
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
                text = "Chi tiết đơn hàng",
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            // Demo Action: Fast forward status for interactive testing
            IconButton(onClick = { viewModel.advanceOrderStatus(order.id) }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Advance Demo State", tint = Primary)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Escrow 48h Protection Banner (Flow G & Flow D)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = com.swapshe.app.theme.EscrowBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, com.swapshe.app.theme.EscrowBlue.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(com.swapshe.app.theme.EscrowBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Filled.Info, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Column(modifier = Modifier.padding(start = 10.dp).weight(1f)) {
                        Text(
                            text = if (order.status == OrderStatus.COMPLETED)
                                "Tiền đã giải ngân thành công cho Seller"
                            else
                                "🛡️ Ví Escrow đang tạm giữ ${formatter.format(order.subtotal)}đ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.swapshe.app.theme.EscrowBlue
                        )
                        Text(
                            text = if (order.status == OrderStatus.DELIVERED)
                                "Thời hạn khiếu nại / trả hàng còn lại: 47 giờ 25 phút. Tiền sẽ chỉ chuyển cho Seller sau thời gian này."
                            else if (order.status == OrderStatus.DISPUTED)
                                "Đang có khiếu nại phát sinh. Tiền trong ví Ký quỹ đang ĐÓNG BĂNG để bảo vệ bạn."
                            else
                                "Bảo hiểm giao dịch an toàn 48h bởi SwapShe.",
                            fontSize = 11.sp,
                            color = com.swapshe.app.theme.EscrowBlue.copy(alpha = 0.85f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            // Order Code & Status Header
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Mã đơn: ${order.internalCode}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            order.trackingCode?.let {
                                Text(text = "Mã vận đơn (GHN): $it", fontSize = 12.sp, color = TextMuted)
                            }
                        }

                        val (statusBg, statusColor) = when (order.status) {
                            OrderStatus.COMPLETED -> SuccessBg to SuccessGreen
                            OrderStatus.DISPUTED -> ErrorBg to ErrorRed
                            else -> WarningBg to WarningAmber
                        }
                        Box(
                            modifier = Modifier
                                .background(statusBg, RoundedCornerShape(100.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = order.status.label, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 14.dp))

                    // Timeline
                    Text(
                        text = "Tiến trình giao vận",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OrderTimelineView(events = order.timeline)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Item card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = order.item.images.firstOrNull() ?: "",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp))
                    )
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(text = order.item.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(text = "Shop: ${order.item.seller.shopName} • Size ${order.item.size}", fontSize = 11.sp, color = TextMuted)
                        Text(text = "${formatter.format(order.item.price)}đ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delivery Address Info
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Địa chỉ nhận hàng", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "${order.shippingAddress.fullName} • ${order.shippingAddress.phone}", fontSize = 13.sp, color = TextPrimary)
                    Text(text = order.shippingAddress.fullAddressText, fontSize = 12.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment summary
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Thanh toán", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Tiền hàng", fontSize = 12.sp, color = TextSecondary)
                        Text(text = "${formatter.format(order.subtotal)}đ", fontSize = 12.sp, color = TextPrimary)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Phí vận chuyển", fontSize = 12.sp, color = TextSecondary)
                        Text(text = "${formatter.format(order.shippingFee)}đ", fontSize = 12.sp, color = TextPrimary)
                    }
                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Tổng cộng", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "${formatter.format(order.total)}đ", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Primary)
                    }
                }
            }

            // Existing Review badge if reviewed
            if (order.review != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceWhite,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Đánh giá của bạn", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            repeat(order.review.rating) {
                                Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                            }
                        }
                        if (order.review.comment.isNotBlank()) {
                            Text(text = order.review.comment, fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom Actions based on Order status (BR-09 & Flow D)
        Surface(
            color = SurfaceWhite,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                when (order.status) {
                    OrderStatus.DELIVERED -> {
                        PrimaryButton(
                            text = "✓ Đã nhận được hàng (Confirm Received)",
                            onClick = { viewModel.confirmDelivery(order.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SecondaryButton(
                            text = "⚠️ Khiếu nại / Trả hàng (Dispute 48h)",
                            onClick = { onNavigateToReport("ORDER", order.id) }
                        )
                    }
                    OrderStatus.COMPLETED -> {
                        if (order.review == null) {
                            PrimaryButton(
                                text = "⭐ Viết đánh giá người bán (Review Order)",
                                onClick = { viewModel.openReviewDialog() }
                            )
                        } else {
                            PrimaryButton(
                                text = "Đơn hàng đã hoàn tất",
                                enabled = false,
                                onClick = {}
                            )
                        }
                    }
                    OrderStatus.DISPUTED -> {
                        PrimaryButton(
                            text = "🔍 Xem chi tiết Khiếu nại & Thương lượng",
                            onClick = { onNavigateToDisputeDetail(order.id) }
                        )
                    }
                    else -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            SecondaryButton(
                                text = "Báo cáo sự cố",
                                onClick = { onNavigateToReport("ORDER", order.id) },
                                modifier = Modifier.weight(1f)
                            )
                            PrimaryButton(
                                text = "Đang vận chuyển",
                                enabled = false,
                                onClick = {},
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }

    // F20: Review Order Dialog (Clean Minimalist Rating & Notes Modal)
    ProductRatingNotesModal(
        isVisible = showReviewDialog,
        productTitle = order.item.title,
        imageUrl = order.item.images.firstOrNull() ?: "",
        initialRating = reviewRating.toFloat(),
        initialNotes = reviewComment,
        onDismiss = { viewModel.closeReviewDialog() },
        onSave = { rating, notes ->
            viewModel.submitReview(order.id, rating, notes)
        }
    )
}
