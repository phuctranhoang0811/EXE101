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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.MockData
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.ConditionType
import com.swapshe.app.model.Product
import com.swapshe.app.model.ProductMeasurements
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

@Composable
fun CreateEditListingScreen(
    productId: String?,
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val repository = SwapSheRepository.instance
    val myCloset by repository.myCloset.collectAsState()
    val existingProduct = myCloset.find { it.id == productId }

    var currentStep by remember { mutableIntStateOf(1) } // 1: Photos, 2: Details, 3: Measurements & Condition, 4: Pricing & Options

    // Form states
    var title by remember { mutableStateOf(existingProduct?.title ?: "") }
    var brand by remember { mutableStateOf(existingProduct?.brand ?: "Vintage Chic") }
    var category by remember { mutableStateOf(existingProduct?.category ?: "Tops") }
    var style by remember { mutableStateOf(existingProduct?.style ?: "Vintage") }
    var color by remember { mutableStateOf(existingProduct?.color ?: "Cream") }
    var description by remember { mutableStateOf(existingProduct?.description ?: "") }
    var sizeLabel by remember { mutableStateOf(existingProduct?.size ?: "S") }
    var condition by remember { mutableStateOf(existingProduct?.condition ?: ConditionType.LIKE_NEW) }

    // Measurements
    var bust by remember { mutableStateOf(existingProduct?.measurements?.bust?.toString() ?: "86") }
    var length by remember { mutableStateOf(existingProduct?.measurements?.length?.toString() ?: "52") }
    var waist by remember { mutableStateOf(existingProduct?.measurements?.waist?.toString() ?: "70") }

    // Pricing & Swap
    var priceText by remember { mutableStateOf(existingProduct?.price?.toString() ?: "120000") }
    var allowSwap by remember { mutableStateOf(existingProduct?.canSwap ?: true) }
    var allowBuy by remember { mutableStateOf(existingProduct?.allowBuy ?: true) }

    // Photos
    var imageList by remember {
        mutableStateOf(
            existingProduct?.images?.ifEmpty { null }
                ?: listOf(
                    "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=800&q=80",
                    "https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=800&q=80"
                )
        )
    }

    val steps = listOf("1. Ảnh chụp", "2. Chi tiết", "3. Số đo & Tình trạng", "4. Giá & Đổi đồ")

    fun buildProduct(isPublish: Boolean): Product {
        return Product(
            id = existingProduct?.id ?: "prod_${System.currentTimeMillis()}",
            title = title.ifBlank { "Món đồ Vintage" },
            brand = brand.ifBlank { "Thương hiệu tuyển chọn" },
            price = priceText.toLongOrNull() ?: 100000L,
            condition = condition,
            size = sizeLabel,
            category = category,
            style = style,
            color = color,
            images = imageList,
            measurements = ProductMeasurements(
                bust = bust.toIntOrNull(),
                length = length.toIntOrNull(),
                waist = waist.toIntOrNull()
            ),
            description = description.ifBlank { "Món đồ giữ gìn sạch đẹp, thơm tho chuẩn gu vintage." },
            seller = MockData.mySellerProfile,
            canSwap = allowSwap,
            allowBuy = allowBuy,
            status = if (isPublish) ProductStatus.AVAILABLE else ProductStatus.DRAFT
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
    ) {
        // Top Header
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
                text = if (productId != null) "Chỉnh sửa món đồ" else "Đăng bán / Đổi đồ",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Lưu nháp",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        repository.saveListing(buildProduct(isPublish = false), isPublish = false)
                        onSuccess()
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Stepper header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceWhite)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEachIndexed { index, stepName ->
                val stepNum = index + 1
                val isCurrent = currentStep == stepNum
                val isCompleted = currentStep > stepNum
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { currentStep = stepNum }
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                color = if (isCurrent) Primary else if (isCompleted) PrimarySoft15 else BorderColor,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCompleted) "✓" else "$stepNum",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) Color.White else Primary
                        )
                    }
                    Text(
                        text = stepName.substringAfter(". "),
                        fontSize = 10.sp,
                        color = if (isCurrent) Primary else TextMuted,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = BorderColor)

        // Step Contents
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (currentStep) {
                1 -> {
                    // STEP 1: Photos
                    Text(text = "Hình ảnh món đồ (Tối thiểu 2 ảnh)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Chụp rõ dáng áo, nhãn mác, chi tiết đường may và khuyết điểm (nếu có).", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        imageList.forEach { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                            )
                        }

                        // Add photo mockup
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceWhite)
                                .border(1.dp, Primary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .clickable {
                                    imageList = imageList + "https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=800&q=80"
                                    repository.showToast("Đã thêm ảnh minh chứng từ thư viện!")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Filled.AddPhotoAlternate, contentDescription = null, tint = Primary)
                                Text(text = "+ Thêm ảnh", fontSize = 11.sp, color = Primary, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                2 -> {
                    // STEP 2: Details
                    Text(text = "Thông tin chi tiết", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Tên món đồ *") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = brand,
                        onValueChange = { brand = it },
                        label = { Text("Thương hiệu") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Danh mục (Tops, Skirts, Dresses...)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Mô tả chi tiết & nguồn gốc") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                3 -> {
                    // STEP 3: Measurements & Condition
                    Text(text = "Tình trạng & Số đo thực tế", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Số đo đo tay giúp người mua đối chiếu độ vừa vặn chính xác.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Condition selector
                    Text(text = "Tình trạng sản phẩm:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ConditionType.values().forEach { c ->
                            val isSel = condition == c
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) Primary else SurfaceWhite,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) Primary else BorderColor),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { condition = c }
                            ) {
                                Text(
                                    text = c.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSel) Color.White else TextPrimary,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Measurements inputs
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = bust,
                            onValueChange = { bust = it },
                            label = { Text("Ngực (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = waist,
                            onValueChange = { waist = it },
                            label = { Text("Eo (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = length,
                            onValueChange = { length = it },
                            label = { Text("Dài (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                4 -> {
                    // STEP 4: Pricing & Options
                    Text(text = "Giá bán & Chế độ giao dịch", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Giá bán thanh lý (VNĐ) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Cho phép đổi đồ 1-1 (Direct Swap)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(text = "Người khác có thể gửi đề nghị đổi món này lấy một món trong tủ đồ của họ.", fontSize = 11.sp, color = TextSecondary)
                                }
                                Switch(
                                    checked = allowSwap,
                                    onCheckedChange = { allowSwap = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = Primary, checkedTrackColor = PrimarySoft15)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PrimarySoft15,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Info, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                            Text(
                                text = " Mỗi món secondhand có số lượng duy nhất = 1. Sau khi Publish, bạn có thể chỉnh sửa hoặc tạm ẩn trong My Closet.",
                                fontSize = 11.sp,
                                color = Primary,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Navigation Buttons
        Surface(
            color = SurfaceWhite,
            shadowElevation = 8.dp,
            modifier = Modifier.navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentStep > 1) {
                    SecondaryButton(
                        text = "Quay lại",
                        onClick = { currentStep -= 1 },
                        modifier = Modifier.weight(1f)
                    )
                }

                if (currentStep < 4) {
                    PrimaryButton(
                        text = "Tiếp theo",
                        onClick = { currentStep += 1 },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    PrimaryButton(
                        text = "Đăng bán ngay ✨",
                        onClick = {
                            repository.saveListing(buildProduct(isPublish = true), isPublish = true)
                            onSuccess()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
