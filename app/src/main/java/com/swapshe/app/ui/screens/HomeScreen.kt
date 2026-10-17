package com.swapshe.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Tune
import com.swapshe.app.ui.components.VirtualFitStudioSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.R
import com.swapshe.app.data.MockData
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.BodyMeasurements
import com.swapshe.app.ui.components.CategoryPill
import com.swapshe.app.ui.components.FitAssistantBottomSheet
import com.swapshe.app.ui.components.ProductCard
import com.swapshe.app.ui.components.SecondaryButton
import com.swapshe.app.ui.components.TextLinkButton
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProduct: (String) -> Unit,
    onNavigateToExplore: (category: String?) -> Unit,
    onNavigateToWishlist: () -> Unit
) {
    val repository = SwapSheRepository.instance
    val products by repository.products.collectAsState()
    val user by repository.currentUser.collectAsState()
    val notifications by repository.notifications.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedSize by remember { mutableStateOf("All") }
    var showNotificationsSheet by remember { mutableStateOf(false) }
    var showFitAssistant by remember { mutableStateOf(false) }
    var showVirtualFitStudio by remember { mutableStateOf(false) }
    var customFitAppliedInfo by remember { mutableStateOf<String?>(null) }

    val filteredProducts = remember(products, selectedCategory, selectedSize) {
        products.filter { product ->
            val matchCategory = selectedCategory == "All" || product.category.equals(selectedCategory, ignoreCase = true)
            val matchSize = if (selectedSize == "All") {
                true
            } else {
                product.size.equals(selectedSize, ignoreCase = true) || product.size.equals("Free", ignoreCase = true)
            }
            matchCategory && matchSize
        }
    }

    if (showNotificationsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showNotificationsSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = SurfaceWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Thông báo của bạn",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                notifications.forEach { notif ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (notif.isRead) BackgroundSubtle.copy(alpha = 0.5f) else WarmCream,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clickable {
                                repository.markNotificationAsRead(notif.id)
                                showNotificationsSheet = false
                                if (notif.targetProductId != null) {
                                    onNavigateToProduct(notif.targetProductId)
                                }
                            }
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = notif.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = notif.timestamp,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            Text(
                                text = notif.message,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
    ) {
        // App Header
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "SwapShe Logo",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                    )
                    Column {
                        Text(
                            text = "SWAPSHE",
                            fontFamily = FontFamily.Serif,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = if (user.isGuest) "Welcome Guest • Khám phá đồ đẹp" else "Xin chào, ${user.name}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { showNotificationsSheet = true }
                    ) {
                        Box {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Notifications",
                                tint = Primary,
                                modifier = Modifier.size(24.dp)
                            )
                            val unreadCount = notifications.count { !it.isRead }
                            if (unreadCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(PrimarySoft, CircleShape)
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Search Bar Trigger
        item(span = { GridItemSpan(2) }) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToExplore(null) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Tìm kiếm váy vintage, cardigan, blazer...",
                        color = TextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }
        }

        // Editorial Promotional Banner (Wireframe 6.4)
        item(span = { GridItemSpan(2) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 7f)
                    .clip(RoundedCornerShape(18.dp))
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=800&q=80",
                    contentDescription = "Vintage Collection",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xD9523243), Color(0x33523243))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "VINTAGE EDITORIAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySoft,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Kho Báu Secondhand",
                        fontFamily = FontFamily.Serif,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = "Mỗi món đồ chỉ có 1 chiếc duy nhất.",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Category Pills
        item(span = { GridItemSpan(2) }) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(MockData.categories) { cat ->
                    CategoryPill(
                        category = cat,
                        isSelected = selectedCategory == cat,
                        onClick = { selectedCategory = cat }
                    )
                }
            }
        }

        // Size Filter & Smart Fit Assistant Tool (Height, Weight, Measurements & Undertone)
        item(span = { GridItemSpan(2) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Straighten,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Chọn Size vừa vặn:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // 2D Virtual Fit Studio Button
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Primary,
                                modifier = Modifier.clickable { showVirtualFitStudio = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Checkroom,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Thử Đồ 2D",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            // Smart Fit Assistant Button
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = PrimarySoft15,
                                border = BorderStroke(1.dp, Primary.copy(alpha = 0.45f)),
                                modifier = Modifier.clickable { showFitAssistant = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Tune,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Tính Size",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Primary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Size chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val sizeList = listOf("All", "XS", "S", "M", "L", "XL", "Free")
                        items(sizeList) { sz ->
                            val isSelected = selectedSize == sz
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Primary else BackgroundSubtle,
                                border = BorderStroke(1.dp, if (isSelected) Primary else BorderColor),
                                modifier = Modifier.clickable {
                                    selectedSize = sz
                                    if (sz == "All") customFitAppliedInfo = null
                                }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                ) {
                                    Text(
                                        text = if (sz == "All") "Tất cả" else if (sz == "Free") "Freesize" else "Size $sz",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else TextPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Active Personalized Fit Badge
                    if (customFitAppliedInfo != null && selectedSize != "All") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PrimarySoft15,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = customFitAppliedInfo ?: "",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Primary,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "✕ Bỏ lọc",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary,
                                    modifier = Modifier
                                        .padding(start = 6.dp)
                                        .clickable {
                                            selectedSize = "All"
                                            customFitAppliedInfo = null
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Picked For You
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Gợi ý cho bạn",
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (selectedSize != "All")
                            "Đang lọc ${filteredProducts.size} món vừa vặn Size $selectedSize & ${user.skinTone.substringBefore(" (")}"
                        else if (user.stylePreferences.isNotEmpty())
                            "Phù hợp gu ${user.stylePreferences.first()} & Size ${user.sizePreference ?: "S"}"
                        else "Bộ sưu tập trending hôm nay",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                TextLinkButton(
                    text = "Xem tất cả",
                    onClick = { onNavigateToExplore(null) }
                )
            }
        }

        // Empty state if 0 products match
        if (filteredProducts.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceWhite,
                    border = BorderStroke(1.dp, BorderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Straighten,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Chưa có món đồ Size $selectedSize trong danh mục này",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Thử chọn Tất cả size hoặc đổi sang danh mục thời trang khác nhé!",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                        )
                        SecondaryButton(
                            text = "Xem tất cả kích cỡ",
                            onClick = {
                                selectedSize = "All"
                                selectedCategory = "All"
                                customFitAppliedInfo = null
                            }
                        )
                    }
                }
            }
        }

        // 2-Column Virtualized Product Grid
        items(filteredProducts, key = { it.id }) { product ->
            val matchBadge = when {
                user.sizePreference != null && product.size.equals(user.sizePreference, ignoreCase = true) -> "Hợp 95% số đo"
                user.stylePreferences.any { it.equals(product.style, ignoreCase = true) } -> "Hợp 90% gu bạn"
                product.grade == com.swapshe.app.model.SwapSheGrade.GRADE_S -> "Tuyển chọn S"
                else -> null
            }
            ProductCard(
                product = product,
                onClick = { onNavigateToProduct(product.id) },
                onLikeToggle = { repository.toggleLike(product.id) },
                matchBadgeText = matchBadge,
                modifier = Modifier.animateItem()
            )
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showFitAssistant) {
        FitAssistantBottomSheet(
            initialHeightCm = user.bodyMeasurements?.heightCm ?: 158,
            initialWeightKg = user.bodyMeasurements?.weightKg ?: 48,
            initialBustCm = user.bodyMeasurements?.bustCm ?: 84,
            initialWaistCm = user.bodyMeasurements?.waistCm ?: 64,
            initialHipsCm = user.bodyMeasurements?.hipsCm ?: 90,
            initialSkinTone = user.skinTone,
            onDismissRequest = { showFitAssistant = false },
            onApply = { size, heightCm, weightKg, bustCm, waistCm, hipsCm, skinTone ->
                repository.updatePreferences(
                    size = size,
                    measurements = BodyMeasurements(
                        heightCm = heightCm,
                        weightKg = weightKg,
                        bustCm = bustCm,
                        waistCm = waistCm,
                        hipsCm = hipsCm
                    ),
                    skinTone = skinTone
                )
                selectedSize = size
                customFitAppliedInfo = "Chuẩn dáng: Size $size (${heightCm}cm • ${weightKg}kg • ${skinTone.substringBefore(" (")})"
                showFitAssistant = false
            }
        )
    }

    if (showVirtualFitStudio) {
        VirtualFitStudioSheet(
            initialProduct = products.firstOrNull() ?: MockData.products.first(),
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
