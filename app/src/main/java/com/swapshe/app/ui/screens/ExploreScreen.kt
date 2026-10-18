package com.swapshe.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swapshe.app.data.MockData
import com.swapshe.app.model.ConditionType
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.ui.components.CategoryPill
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.ProductOfferCard
import com.swapshe.app.ui.components.SecondaryButton
import com.swapshe.app.ui.components.SizeChip
import com.swapshe.app.ui.responsive.rememberWindowSizeInfo
import com.swapshe.app.ui.responsive.responsiveContainerWidth
import com.swapshe.app.ui.responsive.responsiveContentWidth
import com.swapshe.app.ui.viewmodels.ExploreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    initialCategory: String? = null,
    onNavigateToProduct: (String) -> Unit,
    onBack: () -> Unit = {},
    viewModel: ExploreViewModel = viewModel(factory = ExploreViewModel.provideFactory(initialCategory))
) {
    val windowSizeInfo = rememberWindowSizeInfo()
    val user by viewModel.user.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val resultList by viewModel.resultList.collectAsState()

    val searchQuery = uiState.searchQuery
    val selectedCategory = uiState.selectedCategory
    val sortOrder = uiState.sortOrder
    val isFilterSheetOpen = uiState.isFilterSheetOpen
    val filterSize = uiState.filterSize
    val filterCondition = uiState.filterCondition
    val filterSwapOnly = uiState.filterSwapOnly

    var isSearchExpanded by remember { mutableStateOf(false) }
    var showInfoSheet by remember { mutableStateOf(false) }

    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val infoSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        // Top Bar: Back (Circular) - "Products" (Center) - Info (Circular)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFF5F5F7), CircleShape)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF18181B),
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "Products",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF18181B)
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFF5F5F7), CircleShape)
                    .clickable { showInfoSheet = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    tint = Color(0xFF18181B),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Section Title: "Special Offers" + "Don't miss out on our exclusive deals." + "View All"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Special Offers",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF18181B)
                )
                Text(
                    text = "Don't miss out on our exclusive deals.",
                    fontSize = 13.sp,
                    color = Color(0xFF8E8E93),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Text(
                text = if (isSearchExpanded) "Thu gọn" else "View All",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8E8E93),
                modifier = Modifier
                    .clickable { isSearchExpanded = !isSearchExpanded }
                    .padding(start = 12.dp)
            )
        }

        // Expandable Filter & Search Controls (Revealed when tapping "View All")
        AnimatedVisibility(
            visible = isSearchExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    placeholder = { Text("Tìm theo tên, hãng, style...", color = TextMuted, fontSize = 13.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = TextMuted)
                    },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear", tint = TextMuted)
                            }
                        }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = BorderColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${resultList.size} sản phẩm",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (filterSize != null || filterCondition != null || filterSwapOnly) Primary else SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                            modifier = Modifier.clickable { viewModel.toggleFilterSheet(true) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.FilterList,
                                    contentDescription = "Filter",
                                    tint = if (filterSize != null || filterCondition != null || filterSwapOnly) Color.White else Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Filter",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (filterSize != null || filterCondition != null || filterSwapOnly) Color.White else TextPrimary,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                            modifier = Modifier.clickable { viewModel.onSortOrderCycle() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SwapVert,
                                    contentDescription = "Sort",
                                    tint = Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                val sortLabel = when (sortOrder) {
                                    "price_asc" -> "Giá: Thấp → Cao"
                                    "price_desc" -> "Giá: Cao → Thấp"
                                    else -> "Mới nhất"
                                }
                                Text(
                                    text = sortLabel,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Product Cards List (Matching screenshot layout)
        if (resultList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Không tìm thấy món đồ phù hợp",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = "Hãy thử điều chỉnh từ khóa hoặc xóa bớt tiêu chí trong bộ lọc nhé.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
                )
                SecondaryButton(
                    text = "Xóa tất cả bộ lọc",
                    onClick = { viewModel.clearAllFilters() }
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .responsiveContainerWidth(720.dp)
            ) {
                itemsIndexed(resultList, key = { _, item -> item.id }) { index, product ->
                    val quantity = when (index) {
                        0, 1, 2 -> "x2"
                        else -> "x1"
                    }
                    ProductOfferCard(
                        product = product,
                        onClick = { onNavigateToProduct(product.id) },
                        onLikeToggle = { viewModel.toggleLike(product.id) },
                        onShopNow = { onNavigateToProduct(product.id) },
                        onVirtualFit = { onNavigateToProduct(product.id) },
                        isHighlighted = index == 0,
                        quantityMultiplier = quantity
                    )
                }
                item {
                    // Extra spacing so cards scroll behind floating pill dock and are never clipped
                    Spacer(modifier = Modifier.height(96.dp))
                }
            }
        }
    }

    // Info Modal Bottom Sheet (Triggered by ⓘ info button)
    if (showInfoSheet) {
        ModalBottomSheet(
            onDismissRequest = { showInfoSheet = false },
            sheetState = infoSheetState,
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .responsiveContentWidth(560.dp)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Ưu đãi độc quyền (Special Offers)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF18181B)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Bộ sưu tập mùa đông tuyển chọn (Winter Coat & Arctic Puffer Jackets) độc quyền trên SwapShe với cam kết kiểm định chất lượng nghiêm ngặt, thanh toán ký quỹ Escrow bảo đảm 100% và chính sách đồng kiểm 48h.",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                PrimaryButton(
                    text = "Đã hiểu",
                    onClick = { showInfoSheet = false },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Filter & Sort Bottom Sheet
    if (isFilterSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleFilterSheet(false) },
            sheetState = filterSheetState,
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .responsiveContentWidth(640.dp)
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bộ lọc sản phẩm",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    IconButton(onClick = { viewModel.toggleFilterSheet(false) }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 12.dp))

                // Size Section
                Text(
                    text = "Kích thước (Size)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("XS", "S", "M", "L", "XL", "XXL").forEach { size ->
                        SizeChip(
                            size = size,
                            isSelected = filterSize == size,
                            onClick = {
                                val nextSize = if (filterSize == size) null else size
                                viewModel.updateFilters(nextSize, filterCondition, filterSwapOnly)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Condition Section
                Text(
                    text = "Tình trạng đồ (Condition)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                ConditionType.values().forEach { cond ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val nextCond = if (filterCondition == cond) null else cond
                                viewModel.updateFilters(filterSize, nextCond, filterSwapOnly)
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = filterCondition == cond,
                            onCheckedChange = {
                                val nextCond = if (it) cond else null
                                viewModel.updateFilters(filterSize, nextCond, filterSwapOnly)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = Primary)
                        )
                        Text(
                            text = cond.label,
                            fontSize = 14.sp,
                            color = TextPrimary,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Swap option
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.updateFilters(filterSize, filterCondition, !filterSwapOnly)
                        }
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = filterSwapOnly,
                        onCheckedChange = {
                            viewModel.updateFilters(filterSize, filterCondition, it)
                        },
                        colors = CheckboxDefaults.colors(checkedColor = Primary)
                    )
                    Text(
                        text = "Chỉ hiện sản phẩm hỗ trợ đổi đồ (Smart Swap)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Primary,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SecondaryButton(
                        text = "Đặt lại (Reset)",
                        onClick = { viewModel.updateFilters(null, null, false) },
                        modifier = Modifier.weight(1f)
                    )
                    PrimaryButton(
                        text = "Áp dụng (${resultList.size})",
                        onClick = { viewModel.toggleFilterSheet(false) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
