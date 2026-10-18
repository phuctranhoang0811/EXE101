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
import com.swapshe.app.ui.responsive.rememberWindowSizeInfo
import com.swapshe.app.ui.responsive.responsiveContainerWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarBorder
import com.swapshe.app.model.BodyMeasurements
import com.swapshe.app.ui.components.ProductRatingNotesModal
import com.swapshe.app.ui.components.VirtualFitStudioSheet
import androidx.compose.material3.HorizontalDivider
import java.text.DecimalFormat
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swapshe.app.ui.viewmodels.ProductDetailViewModel
import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.di.AppContainer
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
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.theme.WarningAmber
import androidx.compose.material.icons.filled.ChatBubbleOutline
import com.swapshe.app.model.EkycStatus

@Composable
fun ProductDetailScreen(
    productId: String,
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onNavigateToSeller: (String) -> Unit,
    onNavigateToAuth: (isRegister: Boolean) -> Unit,
    onNavigateToSwapOffer: (String) -> Unit = {},
    onNavigateToReport: (String, String) -> Unit = { _, _ -> },
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToEkyc: () -> Unit = {},
    viewModel: ProductDetailViewModel = viewModel(factory = ProductDetailViewModel.provideFactory(productId))
) {
    val repository: ISwapSheRepository = AppContainer.repository
    val products by viewModel.products.collectAsState()
    val user by viewModel.user.collectAsState()
    val productFromVm by viewModel.product.collectAsState()
    val product = productFromVm ?: products.find { it.id == productId } ?: products.first()

    var showAuthGate by remember { mutableStateOf(false) }
    var showVirtualFitStudio by remember { mutableStateOf(false) }
    var showRatingNotesModal by remember { mutableStateOf(false) }

    val productRatingsAndNotes by repository.productRatingsAndNotes.collectAsState()
    val savedRatingNotes = productRatingsAndNotes[product.id]

    val formatter = DecimalFormat("#,###")
    val pagerState = rememberPagerState(pageCount = { product.images.size })
    val isAvailable = product.status == ProductStatus.AVAILABLE
    val windowSizeInfo = rememberWindowSizeInfo()

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
                .responsiveContainerWidth(1280.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "Chi tiết sản phẩm",
                fontFamily = FontFamily.SansSerif,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { showRatingNotesModal = true }) {
                    Icon(
                        imageVector = if (savedRatingNotes != null) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Rating & Notes",
                        tint = if (savedRatingNotes != null) Color(0xFF18181B) else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
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
                    onToggle = { viewModel.toggleLike(product.id) }
                )
            }
        }

        if (windowSizeInfo.isWideScreen) {
            // Tablet / Foldable / Landscape 2-Column Split View
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .responsiveContainerWidth(1280.dp)
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Left Column: Carousel & Reassurance
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProductPhotoCarousel(
                        product = product,
                        pagerState = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 480.dp)
                            .aspectRatio(3f / 4f)
                            .clip(RoundedCornerShape(20.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 480.dp)
                            .background(SurfaceWhite, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.Shield, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                        Text(
                            text = " Bảo chứng SwapShe Escrow 48h • Đổi trả nếu sai mô tả",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Right Column: Details & Actions
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .verticalScroll(rememberScrollState())
                ) {
                    ProductInfoDetails(
                        product = product,
                        user = user,
                        savedRatingNotes = savedRatingNotes,
                        formatter = formatter,
                        repository = repository,
                        onNavigateToSeller = onNavigateToSeller,
                        onShowRatingNotesModal = { showRatingNotesModal = true },
                        onShowVirtualFitStudio = { showVirtualFitStudio = true }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceWhite,
                        shadowElevation = 3.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ProductActionRow(
                            product = product,
                            user = user,
                            isAvailable = isAvailable,
                            onNavigateToChat = onNavigateToChat,
                            onNavigateToSwapOffer = onNavigateToSwapOffer,
                            onNavigateToCheckout = onNavigateToCheckout,
                            onNavigateToEkyc = onNavigateToEkyc,
                            onShowAuthGate = { showAuthGate = true },
                            repository = repository
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        } else {
            // Compact Phone Single Column View
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                ProductPhotoCarousel(
                    product = product,
                    pagerState = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                )

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
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        // SwapShe Grade (Flow I)
                        Box(
                            modifier = Modifier
                                .background(PrimarySoft15, RoundedCornerShape(100.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${product.grade.code}: ${product.grade.percentage}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }
                        ConditionBadge(condition = product.condition)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = product.title,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                // Price & Fit Score (Flow E: Smart Match)
                val fitScore = remember(user, product) { repository.calculateFitScore(user, product) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${formatter.format(product.price)}đ",
                            fontFamily = FontFamily.SansSerif,
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

                    // Smart Match Fit Score Pill (Flow E)
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = com.swapshe.app.theme.SuccessBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, com.swapshe.app.theme.SuccessGreen)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎯 Hợp $fitScore% số đo",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.swapshe.app.theme.SuccessGreen
                            )
                        }
                    }
                }

                // Transparent Flaw Warning (Flow I)
                if (product.flaws.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = com.swapshe.app.theme.WarningBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, com.swapshe.app.theme.WarningAmber),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                            Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = com.swapshe.app.theme.WarningAmber, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Lưu ý khuyết điểm từ Người bán: ${product.flaws.joinToString(" • ")}",
                                fontSize = 11.sp,
                                color = TextPrimary,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                }

                // Escrow Protection Banner (Flow G)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = com.swapshe.app.theme.EscrowBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.swapshe.app.theme.EscrowBlue.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.Shield, contentDescription = null, tint = com.swapshe.app.theme.EscrowBlue, modifier = Modifier.size(16.dp))
                        Text(
                            text = "🛡️ Bảo vệ bởi Ví Ký Quỹ SwapShe Escrow: Tạm giữ tiền 48h sau khi nhận, đổi trả 100% nếu phát hiện lỗi sai mô tả.",
                            fontSize = 11.sp,
                            color = com.swapshe.app.theme.EscrowBlue,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }

                // Minimalist Rating & Notes Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { showRatingNotesModal = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (savedRatingNotes != null) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = null,
                                tint = if (savedRatingNotes != null) Color(0xFF18181B) else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                            Column(modifier = Modifier.padding(start = 10.dp)) {
                                Text(
                                    text = if (savedRatingNotes != null) "Đánh giá & Ghi chú (${String.format(java.util.Locale.US, "%.1f", savedRatingNotes.first)}★)" else "Đánh giá & Ghi chú (Rating & Notes)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = savedRatingNotes?.second?.ifBlank { null } ?: "Chạm để đánh giá sao và lưu ghi chú cá nhân",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }
                        }
                        Text(
                            text = if (savedRatingNotes != null) "Sửa" else "Đánh giá",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary
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

                Spacer(modifier = Modifier.height(10.dp))

                // Virtual Fit Studio 2D Mannequin Trigger Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimarySoft15,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Primary.copy(alpha = 0.45f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showVirtualFitStudio = true }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Primary,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Straighten,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Phòng Thử Đồ 2D & Thước Đo Vừa Vặn",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Ướm lên Mannequin, quét nhiệt độ chật/rộng & đo gấu áo",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
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
            modifier = Modifier.fillMaxWidth()
        ) {
            ProductActionRow(
                product = product,
                user = user,
                isAvailable = isAvailable,
                onNavigateToChat = onNavigateToChat,
                onNavigateToSwapOffer = onNavigateToSwapOffer,
                onNavigateToCheckout = onNavigateToCheckout,
                onNavigateToEkyc = onNavigateToEkyc,
                onShowAuthGate = { showAuthGate = true },
                repository = repository
            )
        }
    }
}

    // Clean Minimalist Rating & Notes Modal
    ProductRatingNotesModal(
        isVisible = showRatingNotesModal,
        productTitle = product.title,
        imageUrl = product.images.firstOrNull() ?: "",
        initialRating = savedRatingNotes?.first ?: 0f,
        initialNotes = savedRatingNotes?.second ?: "",
        onDismiss = { showRatingNotesModal = false },
        onSave = { rating, notes ->
            viewModel.saveProductRatingAndNotes(product.id, rating, notes)
            showRatingNotesModal = false
        }
    )

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

    if (showVirtualFitStudio) {
        VirtualFitStudioSheet(
            initialProduct = product,
            allProducts = products,
            userHeightCm = user.bodyMeasurements?.heightCm ?: 158,
            userBustCm = user.bodyMeasurements?.bustCm ?: 84,
            userWaistCm = user.bodyMeasurements?.waistCm ?: 66,
            userHipsCm = user.bodyMeasurements?.hipsCm ?: 90,
            onDismissRequest = { showVirtualFitStudio = false },
            onApplyMeasurements = { h, b, w, hp ->
                repository.updatePreferences(
                    measurements = BodyMeasurements(
                        heightCm = h,
                        weightKg = user.bodyMeasurements?.weightKg ?: 48,
                        bustCm = b,
                        waistCm = w,
                        hipsCm = hp
                    )
                )
            }
        )
    }
}

@Composable
private fun ProductPhotoCarousel(
    product: Product,
    pagerState: androidx.compose.foundation.pager.PagerState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
}

@Composable
private fun ProductInfoDetails(
    product: Product,
    user: com.swapshe.app.model.User,
    savedRatingNotes: Pair<Float, String>?,
    formatter: DecimalFormat,
    repository: ISwapSheRepository,
    onNavigateToSeller: (String) -> Unit,
    onShowRatingNotesModal: () -> Unit,
    onShowVirtualFitStudio: () -> Unit
) {
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
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(PrimarySoft15, RoundedCornerShape(100.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${product.grade.code}: ${product.grade.percentage}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }
                ConditionBadge(condition = product.condition)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = product.title,
            fontFamily = FontFamily.SansSerif,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        val fitScore = remember(user, product) { repository.calculateFitScore(user, product) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${formatter.format(product.price)}đ",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                if (product.originalPrice != null) {
                    Text(
                        text = "${formatter.format(product.originalPrice)}đ",
                        fontSize = 14.sp,
                        color = TextMuted,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                        modifier = Modifier.padding(start = 10.dp, bottom = 2.dp)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(100.dp),
                color = com.swapshe.app.theme.SuccessBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, com.swapshe.app.theme.SuccessGreen)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎯 Hợp $fitScore% số đo",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.swapshe.app.theme.SuccessGreen
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SurfaceWhite,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .clickable { onShowRatingNotesModal() }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = if (savedRatingNotes != null) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = if (savedRatingNotes != null) Color(0xFFF59E0B) else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (savedRatingNotes != null) "Đánh giá của bạn: ${savedRatingNotes.first} ⭐" else "Thêm đánh giá & ghi chú riêng tư",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (savedRatingNotes != null && savedRatingNotes.second.isNotBlank())
                                "\"${savedRatingNotes.second}\"" else "Chỉ bạn nhìn thấy ghi chú này",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
                Text(
                    text = if (savedRatingNotes != null) "Sửa" else "Đánh giá",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )
            }
        }

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

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = PrimarySoft15,
            border = androidx.compose.foundation.BorderStroke(1.dp, Primary.copy(alpha = 0.45f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onShowVirtualFitStudio() }
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Primary,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Straighten,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Phòng Thử Đồ 2D & Thước Đo Vừa Vặn",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Ướm lên Mannequin, quét nhiệt độ chật/rộng & đo gấu áo",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 18.dp))

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

@Composable
private fun ProductActionRow(
    product: Product,
    user: com.swapshe.app.model.User,
    isAvailable: Boolean,
    onNavigateToChat: (String, String) -> Unit,
    onNavigateToSwapOffer: (String) -> Unit,
    onNavigateToCheckout: () -> Unit,
    onNavigateToEkyc: () -> Unit,
    onShowAuthGate: () -> Unit,
    repository: ISwapSheRepository
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
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
            Surface(
                shape = CircleShape,
                color = PrimarySoft15,
                modifier = Modifier
                    .size(46.dp)
                    .clickable {
                        onNavigateToChat(product.seller.shopName, product.id)
                    }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.ChatBubbleOutline,
                        contentDescription = "Chat với Người bán",
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (product.canSwap) {
                SecondaryButton(
                    text = "Đổi đồ ⇄",
                    onClick = {
                        if (user.isGuest) {
                            onShowAuthGate()
                        } else if (user.trustProfile.status != EkycStatus.VERIFIED) {
                            repository.showToast("🛡️ Cần hoàn tất eKYC trước khi gửi đề nghị đổi đồ!")
                            onNavigateToEkyc()
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
                        onShowAuthGate()
                    } else {
                        repository.setPendingCheckout(product)
                        onNavigateToCheckout()
                    }
                },
                modifier = Modifier.weight(if (product.canSwap) 1.2f else 2f)
            )
        }
    }
}

