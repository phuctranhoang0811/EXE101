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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.SavedSearch
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.ErrorBg
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.ProductCard
import java.text.NumberFormat
import java.util.Locale

@Composable
fun WishlistScreen(
    onNavigateToProduct: (String) -> Unit,
    onExploreClick: () -> Unit
) {
    val repository = SwapSheRepository.instance
    val products by repository.products.collectAsState()
    val savedSearches by repository.savedSearches.collectAsState()
    val wishlistItems = remember(products) { products.filter { it.isLiked } }

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Đồ đã thả tim (Saved Items), 1: Bộ lọc tìm kiếm đã lưu (Saved Searches)

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
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tủ đồ yêu thích",
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Tabs (Flow E - Smart Wishlist)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceWhite,
            contentColor = Primary,
            divider = { HorizontalDivider(color = BorderColor) }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "Món đã thích (${wishlistItems.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "Bộ lọc đã lưu (${savedSearches.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }

        if (selectedTab == 0) {
            // Tab 1: Saved Items
            if (wishlistItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(ErrorBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tủ đồ yêu thích còn trống",
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Khi tìm thấy món đồ secondhand vừa mắt, bấm vào trái tim để lưu lại theo dõi.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
                    )

                    PrimaryButton(
                        text = "Khám phá đồ đẹp ngay",
                        onClick = onExploreClick
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(wishlistItems, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onNavigateToProduct(product.id) },
                            onLikeToggle = { repository.toggleLike(product.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        } else {
            // Tab 2: Saved Searches (Flow E: Smart Wishlist matching)
            if (savedSearches.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Chưa lưu bộ lọc tìm kiếm nào",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Khi khám phá đồ, bạn có thể lưu các tiêu chí size/gu để nhận thông báo tự động khi có hàng mới.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )
                    PrimaryButton(
                        text = "Khám phá & Lưu bộ lọc",
                        onClick = onExploreClick
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimarySoft15,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✨ Tính năng Smart Match: Khi người bán đăng đồ đúng tiêu chí size, gu thời trang hoặc mức giá dưới đây, hệ thống sẽ gửi thông báo đến bạn ngay.",
                                fontSize = 11.sp,
                                color = Primary,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    items(savedSearches, key = { it.id }) { search ->
                        SavedSearchCard(
                            savedSearch = search,
                            onToggleAlert = { repository.toggleSavedSearchAlert(search.id) },
                            onDelete = { repository.removeSavedSearch(search.id) },
                            onClick = onExploreClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedSearchCard(
    savedSearch: SavedSearch,
    onToggleAlert: () -> Unit,
    onDelete: () -> Unit,
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
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(PrimarySoft15, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = savedSearch.query.ifBlank { "Bộ lọc tùy chỉnh" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                val details = listOfNotNull(
                    savedSearch.style?.let { "Gu $it" },
                    savedSearch.size?.let { "Size $it" },
                    savedSearch.maxPrice?.let { "< ${formatter.format(it)}đ" }
                ).joinToString(" • ")

                Text(
                    text = details.ifBlank { "Tất cả đồ mới" },
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = "Đã lưu ngày ${savedSearch.createdAt}",
                    fontSize = 10.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            IconButton(onClick = onToggleAlert) {
                Icon(
                    imageVector = if (savedSearch.notifyNewItem) Icons.Filled.NotificationsActive else Icons.Filled.NotificationsNone,
                    contentDescription = "Alert toggle",
                    tint = if (savedSearch.notifyNewItem) Primary else TextMuted
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.DeleteOutline,
                    contentDescription = "Delete",
                    tint = ErrorRed,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
