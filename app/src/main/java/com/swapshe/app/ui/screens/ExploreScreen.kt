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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
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
import com.swapshe.app.data.MockData
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.ConditionType
import com.swapshe.app.ui.components.CategoryPill
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.ProductCard
import com.swapshe.app.ui.components.SecondaryButton
import com.swapshe.app.ui.components.SizeChip
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    initialCategory: String? = null,
    onNavigateToProduct: (String) -> Unit
) {
    val repository = SwapSheRepository.instance
    val products by repository.products.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(initialCategory ?: "All") }
    var sortOrder by remember { mutableStateOf("newest") } // "newest", "price_asc", "price_desc"
    var isFilterSheetOpen by remember { mutableStateOf(false) }

    // Filter sheet states
    var filterSize by remember { mutableStateOf<String?>(null) }
    var filterCondition by remember { mutableStateOf<ConditionType?>(null) }
    var filterSwapOnly by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Compute filtered items with memoization
    val resultList = remember(products, searchQuery, selectedCategory, filterSize, filterCondition, filterSwapOnly, sortOrder) {
        val filtered = products.filter { product ->
            val matchesQuery = searchQuery.isBlank() ||
                    product.title.contains(searchQuery, ignoreCase = true) ||
                    product.brand.contains(searchQuery, ignoreCase = true) ||
                    product.style.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == "All" || product.category.equals(selectedCategory, ignoreCase = true)
            val matchesSize = filterSize == null || product.size.equals(filterSize, ignoreCase = true)
            val matchesCondition = filterCondition == null || product.condition == filterCondition
            val matchesSwap = !filterSwapOnly || product.canSwap

            matchesQuery && matchesCategory && matchesSize && matchesCondition && matchesSwap
        }

        when (sortOrder) {
            "price_asc" -> filtered.sortedBy { it.price }
            "price_desc" -> filtered.sortedByDescending { it.price }
            else -> filtered
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
    ) {
        // Search Input Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Tìm theo tên, hãng, style...", color = TextMuted, fontSize = 13.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = TextMuted)
                },
                trailingIcon = if (searchQuery.isNotEmpty()) {
                    {
                        IconButton(onClick = { searchQuery = "" }) {
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
                modifier = Modifier.weight(1f)
            )
        }

        // Category Pills
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
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

        // Subheader Controls: Filter & Sort buttons + count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
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
                // Filter Button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (filterSize != null || filterCondition != null || filterSwapOnly) Primary else SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.clickable { isFilterSheetOpen = true }
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

                // Sort Button (toggles sort order)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.clickable {
                        sortOrder = when (sortOrder) {
                            "newest" -> "price_asc"
                            "price_asc" -> "price_desc"
                            else -> "newest"
                        }
                    }
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

        // Product Grid or Empty State
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
                    fontFamily = FontFamily.Serif,
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
                    onClick = {
                        searchQuery = ""
                        selectedCategory = "All"
                        filterSize = null
                        filterCondition = null
                        filterSwapOnly = false
                    }
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(resultList, key = { it.id }) { product ->
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
    }

    // F10: Filter & Sort Bottom Sheet
    if (isFilterSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isFilterSheetOpen = false },
            sheetState = sheetState,
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    IconButton(onClick = { isFilterSheetOpen = false }) {
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
                    listOf("XS", "S", "M", "L", "XL").forEach { size ->
                        SizeChip(
                            size = size,
                            isSelected = filterSize == size,
                            onClick = {
                                filterSize = if (filterSize == size) null else size
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
                                filterCondition = if (filterCondition == cond) null else cond
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = filterCondition == cond,
                            onCheckedChange = {
                                filterCondition = if (it) cond else null
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
                        .clickable { filterSwapOnly = !filterSwapOnly }
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = filterSwapOnly,
                        onCheckedChange = { filterSwapOnly = it },
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
                        onClick = {
                            filterSize = null
                            filterCondition = null
                            filterSwapOnly = false
                        },
                        modifier = Modifier.weight(1f)
                    )
                    PrimaryButton(
                        text = "Áp dụng (${resultList.size})",
                        onClick = { isFilterSheetOpen = false },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
