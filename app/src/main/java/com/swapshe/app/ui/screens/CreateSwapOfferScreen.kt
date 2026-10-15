package com.swapshe.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CreateSwapOfferScreen(
    targetProductId: String,
    onNavigateBack: () -> Unit,
    onNavigateToCreateListing: () -> Unit,
    onOfferSent: () -> Unit
) {
    val repository = SwapSheRepository.instance
    val products by repository.products.collectAsState()
    val myCloset by repository.myCloset.collectAsState()

    val targetProduct = products.find { it.id == targetProductId }
    val availableMyItems = myCloset.filter { it.status == ProductStatus.AVAILABLE && it.canSwap }

    var selectedMyProduct by remember { mutableStateOf(availableMyItems.firstOrNull()) }
    var note by remember { mutableStateOf("Chào bạn, mình rất thích món đồ này của bạn. Đổi ngang 1-1 với chiếc của mình nhé!") }

    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)

    if (targetProduct == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmCream)
                .statusBarsPadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Không tìm thấy món đồ muốn đổi.", color = TextPrimary)
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
                text = "Đề nghị đổi đồ 1-1",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Pair Cards: Target Item ↔ Selected Item
            Text(text = "Cặp đồ trao đổi:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Target Item (Their Item)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Món của bạn muốn đổi", fontSize = 11.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(6.dp))
                        AsyncImage(
                            model = targetProduct.images.firstOrNull(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Text(text = targetProduct.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, color = TextPrimary, modifier = Modifier.padding(top = 4.dp))
                        Text(text = "Size ${targetProduct.size} • ${targetProduct.seller.name}", fontSize = 10.sp, color = TextSecondary)
                    }
                }

                Icon(
                    imageVector = Icons.Filled.SwapHoriz,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(28.dp)
                )

                // Offered Item (Your Item)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedMyProduct != null) Primary else BorderColor),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Món trong tủ của bạn", fontSize = 11.sp, color = Primary, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        if (selectedMyProduct != null) {
                            AsyncImage(
                                model = selectedMyProduct?.images?.firstOrNull(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Text(text = selectedMyProduct!!.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, color = TextPrimary, modifier = Modifier.padding(top = 4.dp))
                            Text(text = "Size ${selectedMyProduct!!.size}", fontSize = 10.sp, color = TextSecondary)
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BackgroundSubtle),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Chưa chọn món", fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Selector from My Closet
            Text(text = "Chọn món từ Tủ đồ của bạn:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            if (availableMyItems.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Tủ đồ của bạn hiện chưa có món nào khả dụng để đổi.", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(10.dp))
                        PrimaryButton(text = "+ Đăng món mới ngay", onClick = onNavigateToCreateListing)
                    }
                }
            } else {
                availableMyItems.forEach { item ->
                    val isSelected = selectedMyProduct?.id == item.id
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) PrimarySoft15 else SurfaceWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Primary else BorderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedMyProduct = item }
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = item.images.firstOrNull(),
                                contentDescription = item.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )
                            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                                Text(text = item.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                Text(text = "Size ${item.size} • ${item.condition.label} • ${formatter.format(item.price)}đ", fontSize = 11.sp, color = TextSecondary)
                            }
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Primary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Note to recipient
            Text(text = "Lời nhắn gửi người nhận:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Terms note
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = BackgroundSubtle,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "💡 Quy tắc SwapShe: Đổi ngang 1-1, không bù tiền mặt trong bản MVP. Mỗi bên tự chịu phí giao hàng khi gửi kiện hàng của mình.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Bottom CTA
        Surface(
            color = SurfaceWhite,
            shadowElevation = 8.dp,
            modifier = Modifier.navigationBarsPadding()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                PrimaryButton(
                    text = "Gửi đề nghị đổi đồ 🔄",
                    enabled = selectedMyProduct != null,
                    onClick = {
                        selectedMyProduct?.let { myItem ->
                            repository.sendSwapOffer(targetProduct, myItem, note)
                            onOfferSent()
                        }
                    }
                )
            }
        }
    }
}
