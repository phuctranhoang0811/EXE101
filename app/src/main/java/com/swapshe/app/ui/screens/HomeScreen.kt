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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import com.swapshe.app.ui.responsive.rememberWindowSizeInfo
import com.swapshe.app.ui.responsive.responsiveContainerWidth
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.swapshe.app.theme.ActiveIconRed
import com.swapshe.app.theme.ElectricLavender
import com.swapshe.app.theme.ElectricLavenderSoft15
import com.swapshe.app.theme.ElectricLavenderSoft8
import com.swapshe.app.theme.ElectricAccent
import com.swapshe.app.theme.ElectricBackground
import com.swapshe.app.theme.ElectricSurface
import com.swapshe.app.theme.ElectricTextPrimary
import com.swapshe.app.theme.ElectricTextSecondary
import com.swapshe.app.theme.ElectricTextMuted
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.ElectricBorder
import com.swapshe.app.theme.ElectricBorderSubtle

import androidx.lifecycle.viewmodel.compose.viewModel
import com.swapshe.app.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onNavigateToProduct: (String) -> Unit,
    onNavigateToExplore: (category: String?) -> Unit,
    onNavigateToWishlist: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.provideFactory())
) {
    val products by viewModel.products.collectAsState()
    val user by viewModel.user.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()

    val selectedCategory = uiState.selectedCategory
    val selectedSize = uiState.selectedSize
    val customFitAppliedInfo = uiState.customFitAppliedInfo

    if (uiState.showNotificationsSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleNotificationsSheet(false) },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .responsiveContainerWidth(600.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Thông báo của bạn",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                notifications.forEach { notif ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (notif.isRead) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clickable {
                                viewModel.markNotificationAsRead(notif.id)
                                viewModel.toggleNotificationsSheet(false)
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
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = notif.timestamp,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline,
                                    softWrap = false
                                )
                            }
                            Text(
                                text = notif.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    val windowSizeInfo = rememberWindowSizeInfo()
    val gridColumns = windowSizeInfo.productGridColumns()

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            start = if (windowSizeInfo.isWideScreen) 24.dp else 16.dp,
            end = if (windowSizeInfo.isWideScreen) 24.dp else 16.dp,
            top = 8.dp,
            bottom = 96.dp
        ),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .responsiveContainerWidth(1280.dp)
    ) {
        // App Header (F08 Header)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "SwapShe Logo",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    )
                    Column {
                        Text(
                            text = "SWAPSHE",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = if (user.isGuest) "Welcome Guest • Khám phá đồ đẹp" else "Xin chào, ${user.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = { viewModel.toggleNotificationsSheet(true) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        val unreadCount = notifications.count { !it.isRead }
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(ActiveIconRed, CircleShape)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }
            }
        }

        // Search Bar Trigger (Clean Minimal Surface)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = BackgroundSubtle,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToExplore(null) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Tìm kiếm váy vintage, cardigan, blazer...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }

        // Editorial Promotional Banner (Wireframe 6.4)
        item(span = { GridItemSpan(maxLineSpan) }) {
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
                                colors = listOf(
                                    Color(0xCC000000),
                                    Color(0x80000000),
                                    Color(0x20000000)
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "VINTAGE EDITORIAL",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = ElectricTextPrimary,
                            letterSpacing = 1.2.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = "Kho Báu Secondhand",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = "Mỗi món đồ chỉ có 1 chiếc duy nhất.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Category Pills
        item(span = { GridItemSpan(maxLineSpan) }) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(MockData.categories) { cat ->
                    CategoryPill(
                        category = cat,
                        isSelected = selectedCategory == cat,
                        onClick = { viewModel.onCategorySelect(cat) }
                    )
                }
            }
        }

        // Size Filter & Smart Fit Assistant Tool (Clean, Minimal, Integrated)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chọn size vừa vặn",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 2D Virtual Fit Studio Button (Clean pill)
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.clickable { viewModel.toggleVirtualFitStudio(true) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Checkroom,
                                    contentDescription = null,
                                    tint = ElectricTextPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Thử Đồ 2D",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricTextPrimary
                                )
                            }
                        }

                        // Smart Fit Assistant Button (Clean pill)
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = BackgroundSubtle,
                            modifier = Modifier.clickable { viewModel.toggleFitAssistant(true) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Tune,
                                    contentDescription = null,
                                    tint = ElectricTextPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Tính Size",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricTextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Size chips in clean minimal horizontal scroll
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val sizeList = listOf("All", "XS", "S", "M", "L", "XL", "Free")
                    items(sizeList) { sz ->
                        val isSelected = selectedSize == sz
                        val chipBg by animateColorAsState(
                            targetValue = if (isSelected) MaterialTheme.colorScheme.primary else BackgroundSubtle,
                            animationSpec = tween(220),
                            label = "sizeChipBg"
                        )
                        val chipTextCol by animateColorAsState(
                            targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            animationSpec = tween(220),
                            label = "sizeChipText"
                        )
                        val chipScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.05f else 1.0f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label = "sizeChipScale"
                        )

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = chipBg,
                            modifier = Modifier
                                .scale(chipScale)
                                .clickable {
                                    viewModel.onSizeSelect(sz)
                                }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = if (sz == "All") "Tất cả" else if (sz == "Free") "Freesize" else "Size $sz",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = chipTextCol
                                )
                            }
                        }
                    }
                }

                // Active Personalized Fit Badge
                AnimatedVisibility(
                    visible = customFitAppliedInfo != null && selectedSize != "All",
                    enter = fadeIn(tween(220)) + expandVertically(),
                    exit = fadeOut(tween(180)) + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BackgroundSubtle,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = customFitAppliedInfo ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "✕ Bỏ lọc",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .padding(start = 6.dp)
                                        .clickable {
                                            viewModel.clearFitFilter()
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Picked For You
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Text(
                        text = "Gợi ý cho bạn",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (selectedSize != "All")
                            "Đang lọc ${filteredProducts.size} món vừa vặn Size $selectedSize & ${user.skinTone.substringBefore(" (")}"
                        else if (user.stylePreferences.isNotEmpty())
                            "Phù hợp gu ${user.stylePreferences.first()} & Size ${user.sizePreference ?: "S"}"
                        else "Bộ sưu tập trending hôm nay",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
            item(span = { GridItemSpan(maxLineSpan) }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Chưa có món đồ Size $selectedSize trong danh mục này",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Thử chọn Tất cả size hoặc đổi sang danh mục thời trang khác nhé!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                        )
                        SecondaryButton(
                            text = "Xem tất cả kích cỡ",
                            onClick = {
                                viewModel.clearFitFilter()
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
                onLikeToggle = { viewModel.toggleLike(product.id) },
                matchBadgeText = matchBadge,
                modifier = Modifier.animateItem()
            )
        }
    }

    if (uiState.showFitAssistant) {
        FitAssistantBottomSheet(
            initialHeightCm = user.bodyMeasurements?.heightCm ?: 158,
            initialWeightKg = user.bodyMeasurements?.weightKg ?: 48,
            initialBustCm = user.bodyMeasurements?.bustCm ?: 84,
            initialWaistCm = user.bodyMeasurements?.waistCm ?: 64,
            initialHipsCm = user.bodyMeasurements?.hipsCm ?: 90,
            initialSkinTone = user.skinTone,
            onDismissRequest = { viewModel.toggleFitAssistant(false) },
            onApply = { size, heightCm, weightKg, bustCm, waistCm, hipsCm, skinTone ->
                viewModel.applyCustomFit(size, heightCm, weightKg, bustCm, waistCm, hipsCm, skinTone)
            }
        )
    }

    if (uiState.showVirtualFitStudio) {
        VirtualFitStudioSheet(
            initialProduct = products.firstOrNull() ?: MockData.products.first(),
            allProducts = products,
            userHeightCm = user.bodyMeasurements?.heightCm ?: 158,
            userBustCm = user.bodyMeasurements?.bustCm ?: 84,
            userWaistCm = user.bodyMeasurements?.waistCm ?: 66,
            userHipsCm = user.bodyMeasurements?.hipsCm ?: 90,
            onDismissRequest = { viewModel.toggleVirtualFitStudio(false) },
            onApplyMeasurements = { h, b, w, hp ->
                viewModel.applyVirtualFitMeasurements(h, b, w, hp)
            }
        )
    }
}
