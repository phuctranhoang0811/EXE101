package com.swapshe.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.Product
import com.swapshe.app.model.ProductStatus
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.theme.WarningAmber
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import com.swapshe.app.model.EkycStatus

@Composable
fun MyClosetScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCreateListing: (String?) -> Unit,
    onNavigateToSellerOrders: () -> Unit,
    onPreviewProduct: (String) -> Unit,
    onNavigateToEkyc: () -> Unit = {}
) {
    val repository = SwapSheRepository.instance
    val user by repository.currentUser.collectAsState()
    val closetProducts by repository.myCloset.collectAsState()
    val sellerOrders by repository.sellerOrders.collectAsState()

    var showEkycModal by remember { mutableStateOf(false) }

    fun checkAndNavigateToCreate(id: String?) {
        if (user.trustProfile.status != EkycStatus.VERIFIED) {
            showEkycModal = true
        } else {
            onNavigateToCreateListing(id)
        }
    }

    val tabs = listOf("Tất cả", "Đang bán", "Đã giữ chỗ", "Đã bán", "Bản nháp")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val filteredList = when (selectedTabIndex) {
        1 -> closetProducts.filter { it.status == ProductStatus.AVAILABLE }
        2 -> closetProducts.filter { it.status == ProductStatus.RESERVED }
        3 -> closetProducts.filter { it.status == ProductStatus.SOLD }
        4 -> closetProducts.filter { it.status == ProductStatus.DRAFT }
        else -> closetProducts
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Tủ đồ của tôi (My Closet)",
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Quản lý ${closetProducts.size} món đồ đăng bán & trao đổi",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                // Shortcut to Seller Orders
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimarySoft15,
                    modifier = Modifier.clickable { onNavigateToSellerOrders() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.ShoppingBag, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                        Text(
                            text = " Đơn bán (${sellerOrders.size})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary
                        )
                    }
                }
            }

            // Tab bar
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

            // Content list
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.Checkroom,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Tủ đồ chưa có món nào ở mục này",
                            fontFamily = FontFamily.Serif,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Hãy chia sẻ những món đồ vintage của bạn để tìm chủ nhân mới!",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                        )
                        PrimaryButton(
                            text = "+ Đăng món đầu tiên",
                            onClick = { checkAndNavigateToCreate(null) }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList, key = { it.id }) { product ->
                        ClosetItemCard(
                            product = product,
                            onEdit = { onNavigateToCreateListing(product.id) },
                            onPreview = { onPreviewProduct(product.id) },
                            onDelete = { repository.deleteDraft(product.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Floating Action Button to add item
        FloatingActionButton(
            onClick = { checkAndNavigateToCreate(null) },
            containerColor = Primary,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Item")
                Text(
                    text = " Đăng đồ mới",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // eKYC Gate Modal Dialog (Flow B & Flow H)
        if (showEkycModal) {
            AlertDialog(
                onDismissRequest = { showEkycModal = false },
                title = {
                    Text(
                        text = "🛡️ Yêu cầu xác thực eKYC",
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Để bảo vệ an toàn cho cộng đồng và kích hoạt tính năng Đăng bán / Trao đổi, bạn vui lòng hoàn tất định danh eKYC (chụp CCCD & khuôn mặt).",
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "✨ Sau khi xác thực: Đạt huy hiệu Queen Closet 👑, tăng điểm tín nhiệm She-Trust lên 98/100 và giảm 50% phí dịch vụ sàn!",
                            fontSize = 12.sp,
                            color = Primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showEkycModal = false
                            onNavigateToEkyc()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Xác thực ngay ✨", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEkycModal = false }) {
                        Text("Để sau", color = TextSecondary)
                    }
                },
                containerColor = SurfaceWhite,
                shape = RoundedCornerShape(18.dp)
            )
        }
    }
}

@Composable
private fun ClosetItemCard(
    product: Product,
    onEdit: () -> Unit,
    onPreview: () -> Unit,
    onDelete: () -> Unit
) {
    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPreview() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    // Status Chip
                    val (badgeBg, badgeText, badgeColor) = when (product.status) {
                        ProductStatus.AVAILABLE -> Triple(SuccessGreen.copy(alpha = 0.12f), "Đang bán", SuccessGreen)
                        ProductStatus.RESERVED -> Triple(WarningAmber.copy(alpha = 0.15f), "Đang giữ chỗ", WarningAmber)
                        ProductStatus.SOLD -> Triple(TextMuted.copy(alpha = 0.15f), "Đã bán", TextMuted)
                        ProductStatus.DRAFT -> Triple(PrimarySoft15, "Bản nháp", Primary)
                        ProductStatus.HIDDEN -> Triple(BorderColor, "Đã ẩn", TextMuted)
                        ProductStatus.ARCHIVED -> Triple(BorderColor, "Lưu trữ", TextMuted)
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = badgeBg
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = badgeColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${formatter.format(product.price)}đ • Size ${product.size} • ${product.condition.label}",
                    fontSize = 12.sp,
                    color = Primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.canSwap) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BackgroundSubtle
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SwapHoriz,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Đổi đồ 1-1",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.size(1.dp))
                    }

                    Row {
                        IconButton(
                            onClick = onEdit,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit", tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }
                        if (product.status == ProductStatus.DRAFT) {
                            IconButton(
                                onClick = onDelete,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.DeleteOutline, contentDescription = "Delete", tint = ErrorRed, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
