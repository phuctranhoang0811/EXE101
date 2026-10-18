package com.swapshe.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swapshe.app.ui.viewmodels.CheckoutViewModel
import com.swapshe.app.ui.viewmodels.OrdersViewModel
import com.swapshe.app.data.MockData
import com.swapshe.app.model.Order
import com.swapshe.app.model.ShippingAddress
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton
import com.swapshe.app.ui.components.SwapsheTextField
import com.swapshe.app.ui.components.TextLinkButton
import com.swapshe.app.ui.responsive.responsiveContainerWidth
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.ErrorBg
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft
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

/* --------------------------------------------------------------------------
   F14: SHIPPING ADDRESS (Checkout Step 1)
   -------------------------------------------------------------------------- */
@Composable
fun ShippingAddressScreen(
    onNavigateBack: () -> Unit,
    onContinueToReview: () -> Unit,
    viewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModel.provideFactory())
) {
    val product by viewModel.product.collectAsState()
    val savedAddress by viewModel.currentAddress.collectAsState()

    var fullName by remember { mutableStateOf(savedAddress.fullName) }
    var phone by remember { mutableStateOf(savedAddress.phone) }
    var province by remember { mutableStateOf(savedAddress.province) }
    var ward by remember { mutableStateOf(savedAddress.ward) }
    var detailAddress by remember { mutableStateOf(savedAddress.detailAddress) }

    var hasError by remember { mutableStateOf(false) }
    val formatter = DecimalFormat("#,###")

    fun validate(): Boolean {
        return fullName.isNotBlank() && phone.isNotBlank() && province.isNotBlank() && detailAddress.isNotBlank()
    }

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
                .responsiveContainerWidth(720.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "Thanh toán (1/2: Địa chỉ giao hàng)",
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }

        LinearProgressIndicator(
            progress = { 0.5f },
            color = Primary,
            trackColor = BorderColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .responsiveContainerWidth(720.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Selected Product Summary Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = product.images.firstOrNull() ?: "",
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f)
                    ) {
                        Text(text = product.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(text = "Size ${product.size} • ${product.condition.label}", fontSize = 11.sp, color = TextMuted)
                        Text(text = "${formatter.format(product.price)}đ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Thông tin nhận hàng",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Vui lòng nhập chính xác để bên vận chuyển liên hệ trước khi giao.",
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
            )

            SwapsheTextField(
                value = fullName,
                onValueChange = { fullName = it; hasError = false },
                label = "Họ và tên người nhận",
                placeholder = "Nguyễn Hoàng My"
            )

            Spacer(modifier = Modifier.height(12.dp))

            SwapsheTextField(
                value = phone,
                onValueChange = { phone = it; hasError = false },
                label = "Số điện thoại",
                placeholder = "0987654321",
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(12.dp))

            SwapsheTextField(
                value = province,
                onValueChange = { province = it; hasError = false },
                label = "Tỉnh / Thành phố",
                placeholder = "TP. Hồ Chí Minh"
            )

            Spacer(modifier = Modifier.height(12.dp))

            SwapsheTextField(
                value = ward,
                onValueChange = { ward = it },
                label = "Quận / Huyện & Phường / Xã",
                placeholder = "Phường Bến Nghé, Quận 1"
            )

            Spacer(modifier = Modifier.height(12.dp))

            SwapsheTextField(
                value = detailAddress,
                onValueChange = { detailAddress = it; hasError = false },
                label = "Địa chỉ chi tiết (Số nhà, tên đường, tòa nhà)",
                placeholder = "120 Nguyễn Huệ, Căn hộ 4B"
            )

            if (hasError) {
                Text(
                    text = "Vui lòng điền đầy đủ các thông tin giao hàng bắt buộc.",
                    color = ErrorRed,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Delivery method
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.LocalShipping, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(text = "Giao hàng tiêu chuẩn (Standard Delivery)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(text = "Thời gian dự kiến 2–3 ngày làm việc", fontSize = 11.sp, color = TextSecondary)
                    }
                    Text(text = "25.000đ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom CTA
        Surface(
            color = SurfaceWhite,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .responsiveContainerWidth(720.dp)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                PrimaryButton(
                    text = "Tiếp tục sang thanh toán (Review & Pay) →",
                    onClick = {
                        if (viewModel.updateAddress(fullName, phone, province, ward, detailAddress)) {
                            onContinueToReview()
                        } else {
                            hasError = true
                        }
                    }
                )
            }
        }
    }
}

/* --------------------------------------------------------------------------
   F15: REVIEW & PAYMENT (Checkout Step 2)
   -------------------------------------------------------------------------- */
@Composable
fun ReviewPaymentScreen(
    onNavigateBack: () -> Unit,
    onEditAddress: () -> Unit,
    onProceedToMockPayment: () -> Unit,
    viewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModel.provideFactory())
) {
    val product by viewModel.product.collectAsState()
    val address by viewModel.currentAddress.collectAsState()

    val formatter = DecimalFormat("#,###")
    val shippingFee = 25000L
    val totalAmount = product.price + shippingFee

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
                .responsiveContainerWidth(720.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "Thanh toán (2/2: Xác nhận đơn hàng)",
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }

        LinearProgressIndicator(
            progress = { 1.0f },
            color = Primary,
            trackColor = BorderColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .responsiveContainerWidth(720.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Shipping Address Card with Edit link
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Địa chỉ nhận hàng", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            text = "Thay đổi",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary,
                            modifier = Modifier.clickable { onEditAddress() }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "${address.fullName} • ${address.phone}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    Text(text = address.fullAddressText, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(top = 2.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Item summary
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = product.images.firstOrNull() ?: "",
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(text = product.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(text = "Người bán: ${product.seller.shopName}", fontSize = 11.sp, color = TextMuted)
                        Text(text = "${formatter.format(product.price)}đ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Method Simulation Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.Payment, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(text = "Mock Payment Simulation", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Thanh toán mô phỏng an toàn • Không trừ tiền thật", fontSize = 11.sp, color = TextSecondary)
                    }
                    Text(text = "Chọn", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Primary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price Breakdown (Specification 4.1 & BR-07)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceWhite,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Chi tiết thanh toán", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Tiền sản phẩm", fontSize = 13.sp, color = TextSecondary)
                        Text(text = "${formatter.format(product.price)}đ", fontSize = 13.sp, color = TextPrimary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Phí vận chuyển (Standard)", fontSize = 13.sp, color = TextSecondary)
                        Text(text = "${formatter.format(shippingFee)}đ", fontSize = 13.sp, color = TextPrimary)
                    }

                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Tổng thanh toán", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "${formatter.format(totalAmount)}đ", fontFamily = FontFamily.SansSerif, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom CTA
        Surface(
            color = SurfaceWhite,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .responsiveContainerWidth(720.dp)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                PrimaryButton(
                    text = "Xác nhận & Đi đến Thanh toán mô phỏng (${formatter.format(totalAmount)}đ)",
                    onClick = onProceedToMockPayment
                )
            }
        }
    }
}

/* --------------------------------------------------------------------------
   F16: MOCK PAYMENT (Simulation Screen with Success & Failure branches)
   -------------------------------------------------------------------------- */
@Composable
fun MockPaymentScreen(
    onPaymentSuccess: (orderId: String) -> Unit,
    onNavigateBackToReview: () -> Unit,
    viewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModel.provideFactory())
) {
    val product by viewModel.product.collectAsState()
    val address by viewModel.currentAddress.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val failureError by viewModel.failureError.collectAsState()

    val formatter = DecimalFormat("#,###")
    val totalAmount = product.price + 25000L
    val attemptCode = remember { "PAY-ATTEMPT-${(100000..999999).random()}" }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .responsiveContainerWidth(580.dp)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onNavigateBackToReview) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "Mock Payment Simulation",
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prominent Simulation Banner (Specification BR-06)
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = WarningBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, WarningAmber),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                Icon(imageVector = Icons.Filled.Info, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(20.dp))
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        text = "Simulation — No Real Payment",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarningAmber
                    )
                    Text(
                        text = "Đây là môi trường thử nghiệm luồng mua hàng. Không có bất kỳ giao dịch trừ tiền thật nào phát sinh.",
                        fontSize = 12.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Payment Ticket Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                Text(text = "Mã phiên thanh toán", fontSize = 12.sp, color = TextMuted)
                Text(text = attemptCode, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Số tiền thanh toán", fontSize = 12.sp, color = TextMuted)
                Text(
                    text = "${formatter.format(totalAmount)}đ",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Món hàng: ${product.title} (Size ${product.size})",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Failure branch alert
        if (failureError != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ErrorBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.ErrorOutline, contentDescription = null, tint = ErrorRed)
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = "Thanh toán không thành công", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                        Text(text = failureError ?: "", fontSize = 12.sp, color = TextPrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Simulation Triggers
        if (isProcessing) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            ) {
                CircularProgressIndicator(color = Primary)
                Text(
                    text = "Đang kết nối cổng thanh toán mô phỏng...",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        } else {
            // Success Button
            PrimaryButton(
                text = "✓ Simulate Success (Thanh toán thành công)",
                onClick = {
                    viewModel.simulatePaymentSuccess { createdOrderId ->
                        onPaymentSuccess(createdOrderId)
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Failure Button (BR-08)
            SecondaryButton(
                text = "✕ Simulate Failure (Thanh toán thất bại)",
                onClick = {
                    viewModel.simulatePaymentFailure(
                        "Tài khoản mô phỏng không đủ hạn mức hoặc phiên thanh toán hết hạn. Bạn có thể nhấn Thử lại (Retry) hoặc quay về kiểm tra giỏ hàng."
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
}

/* --------------------------------------------------------------------------
   F17: ORDER CONFIRMATION (Order Success Screen)
   -------------------------------------------------------------------------- */
@Composable
fun OrderConfirmationScreen(
    orderId: String,
    onViewOrder: (String) -> Unit,
    onContinueShopping: () -> Unit,
    ordersViewModel: OrdersViewModel = viewModel(factory = OrdersViewModel.provideFactory())
) {
    val orders by ordersViewModel.orders.collectAsState()
    val order = orders.find { it.id == orderId } ?: orders.firstOrNull() ?: MockData.initialOrders.first()

    val formatter = DecimalFormat("#,###")

    val checkScale = remember { Animatable(0f) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        checkScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        contentVisible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .responsiveContainerWidth(580.dp)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.size(92.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .scale(pulseScale)
                    .background(SuccessGreen.copy(alpha = pulseAlpha), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .scale(checkScale.value)
                    .background(SuccessBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(38.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { 40 }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Đặt hàng thành công!",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Text(
                    text = "Mã đơn hàng: ${order.internalCode}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = "Người bán đã nhận được đơn và sẽ tiến hành đóng gói, bàn giao đơn vị vận chuyển.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceWhite,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = order.item.images.firstOrNull() ?: "",
                                contentDescription = order.item.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                                Text(text = order.item.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                Text(text = "Người bán: ${order.item.seller.shopName}", fontSize = 11.sp, color = TextMuted)
                                Text(text = "${formatter.format(order.item.price)}đ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary)
                            }
                        }

                        HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Tổng thanh toán:", fontSize = 13.sp, color = TextSecondary)
                            Text(text = "${formatter.format(order.total)}đ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Giao tới:", fontSize = 12.sp, color = TextMuted)
                            Text(text = "${order.shippingAddress.fullName} (${order.shippingAddress.province})", fontSize = 12.sp, color = TextPrimary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        PrimaryButton(
            text = "Xem tiến trình đơn (View Order) →",
            onClick = { onViewOrder(order.id) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SecondaryButton(
            text = "Tiếp tục khám phá (Continue Shopping)",
            onClick = onContinueShopping
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
}
