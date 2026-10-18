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
import com.swapshe.app.theme.ActiveIconRed
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

    // Photos & 4-Angles (Flow I)
    var imageList by remember {
        mutableStateOf(
            existingProduct?.images?.ifEmpty { null }
                ?: listOf(
                    "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=800&q=80",
                    "https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=800&q=80"
                )
        )
    }
    var hasFrontAngle by remember { mutableStateOf(true) }
    var hasBackAngle by remember { mutableStateOf(true) }
    var hasTagAngle by remember { mutableStateOf(true) }
    var hasDefectAngle by remember { mutableStateOf(false) }
    var hasVideoRecorded by remember { mutableStateOf(false) }

    // Condition Checklist (Flow I)
    var isOriginalTag by remember { mutableStateOf(true) }
    var isWornOnceOrTwice by remember { mutableStateOf(true) }
    var isPristineFabric by remember { mutableStateOf(true) }
    var hasMinorStain by remember { mutableStateOf(false) }
    var hasMissingButton by remember { mutableStateOf(false) }
    var flawNote by remember { mutableStateOf("") }

    // Auto grading calculation
    val calculatedGrade = remember(isOriginalTag, isWornOnceOrTwice, isPristineFabric, hasMinorStain, hasMissingButton) {
        if ((isOriginalTag || isWornOnceOrTwice) && isPristineFabric && !hasMinorStain && !hasMissingButton) {
            com.swapshe.app.model.SwapSheGrade.GRADE_S
        } else if (isPristineFabric && !hasMinorStain && !hasMissingButton) {
            com.swapshe.app.model.SwapSheGrade.GRADE_A
        } else {
            com.swapshe.app.model.SwapSheGrade.GRADE_B
        }
    }

    val steps = listOf("1. Ảnh 4 góc", "2. Chi tiết", "3. Phân hạng & Đo", "4. Giá & Đổi đồ")

    fun buildProduct(isPublish: Boolean): Product {
        val flawsList = mutableListOf<String>()
        if (hasMinorStain) flawsList.add("Có vết ố nhỏ khó thấy đã báo trước")
        if (hasMissingButton) flawsList.add("Mất cúc phụ / Khóa hơi rít đã xử lý")
        if (flawNote.isNotBlank()) flawsList.add(flawNote)

        return Product(
            id = existingProduct?.id ?: "prod_${System.currentTimeMillis()}",
            title = title.ifBlank { "Món đồ Vintage" },
            brand = brand.ifBlank { "Thương hiệu tuyển chọn" },
            price = priceText.toLongOrNull() ?: 100000L,
            condition = when (calculatedGrade) {
                com.swapshe.app.model.SwapSheGrade.GRADE_S -> ConditionType.LIKE_NEW
                com.swapshe.app.model.SwapSheGrade.GRADE_A -> ConditionType.EXCELLENT
                com.swapshe.app.model.SwapSheGrade.GRADE_B -> ConditionType.GOOD
            },
            grade = calculatedGrade,
            flaws = flawsList,
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
                fontFamily = FontFamily.SansSerif,
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
                    // STEP 1: Photos & 4 Mandatory Angles (Flow I)
                    Text(text = "Chụp 4 góc quy chuẩn & Video", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Chụp đủ 4 góc bắt buộc giúp tin đăng được duyệt nhanh và tăng 90% khả năng chốt đơn.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(14.dp))

                    // 4-Angle Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Angle 1: Front
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (hasFrontAngle) Primary else BorderColor),
                            modifier = Modifier.weight(1f).clickable { hasFrontAngle = true }
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "1. Mặt trước *", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Primary)
                                Box(modifier = Modifier.size(60.dp).padding(top = 4.dp).clip(RoundedCornerShape(6.dp)).background(PrimarySoft15), contentAlignment = Alignment.Center) {
                                    Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                                }
                            }
                        }

                        // Angle 2: Back
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (hasBackAngle) Primary else BorderColor),
                            modifier = Modifier.weight(1f).clickable { hasBackAngle = true }
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "2. Mặt sau *", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Primary)
                                Box(modifier = Modifier.size(60.dp).padding(top = 4.dp).clip(RoundedCornerShape(6.dp)).background(PrimarySoft15), contentAlignment = Alignment.Center) {
                                    Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                                }
                            }
                        }

                        // Angle 3: Tag / Label
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (hasTagAngle) Primary else BorderColor),
                            modifier = Modifier.weight(1f).clickable { hasTagAngle = true }
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "3. Tem mác *", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Primary)
                                Box(modifier = Modifier.size(60.dp).padding(top = 4.dp).clip(RoundedCornerShape(6.dp)).background(PrimarySoft15), contentAlignment = Alignment.Center) {
                                    Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                                }
                            }
                        }

                        // Angle 4: Flaw / Details
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (hasDefectAngle) Primary else BorderColor),
                            modifier = Modifier.weight(1f).clickable { hasDefectAngle = !hasDefectAngle }
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "4. Vết lỗi", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Box(modifier = Modifier.size(60.dp).padding(top = 4.dp).clip(RoundedCornerShape(6.dp)).background(BackgroundSubtle), contentAlignment = Alignment.Center) {
                                    Text(text = if (hasDefectAngle) "✓ Đã chụp" else "+ Chụp", fontSize = 9.sp, color = TextMuted)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Video upload slot
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (hasVideoRecorded) PrimarySoft15 else SurfaceWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (hasVideoRecorded) Primary else BorderColor),
                        modifier = Modifier.fillMaxWidth().clickable {
                            hasVideoRecorded = !hasVideoRecorded
                            repository.showToast(if (hasVideoRecorded) "Đã ghi nhận video ngắn kiểm tra áo (7s)!" else "Đã xóa video")
                        }
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.AddPhotoAlternate, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
                            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                                Text(text = "🎥 Video ngắn 5–10s quay cận chất vải", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(text = if (hasVideoRecorded) "✓ Đã đính kèm video kiểm tra phom dáng & ánh sáng thực" else "Tùy chọn • Tăng uy tín She-Trust Score", fontSize = 11.sp, color = TextSecondary)
                            }
                            Text(text = if (hasVideoRecorded) "Đã thêm" else "+ Quay", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Primary)
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
                    // STEP 3: Condition Checklist & Auto Grading (Flow I)
                    Text(text = "Checklist Tình trạng & Phân hạng SwapShe", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Tích chọn đúng thực tế để hệ thống tự động gán nhãn Hạng S, Hạng A hoặc Hạng B.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Checklist items
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceWhite, RoundedCornerShape(12.dp))
                            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { isOriginalTag = !isOriginalTag }) {
                            Checkbox(checked = isOriginalTag, onCheckedChange = { isOriginalTag = it }, colors = CheckboxDefaults.colors(checkedColor = Primary))
                            Text(text = "Còn nguyên tag mác của hãng (Chưa cắt tag)", fontSize = 12.sp, color = TextPrimary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { isWornOnceOrTwice = !isWornOnceOrTwice }) {
                            Checkbox(checked = isWornOnceOrTwice, onCheckedChange = { isWornOnceOrTwice = it }, colors = CheckboxDefaults.colors(checkedColor = Primary))
                            Text(text = "Chỉ mặc 1–2 lần chụp ảnh, chất vải nguyên bản", fontSize = 12.sp, color = TextPrimary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { isPristineFabric = !isPristineFabric }) {
                            Checkbox(checked = isPristineFabric, onCheckedChange = { isPristineFabric = it }, colors = CheckboxDefaults.colors(checkedColor = Primary))
                            Text(text = "Vải không sờn rách, không xù lông sợi dệt", fontSize = 12.sp, color = TextPrimary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { hasMinorStain = !hasMinorStain }) {
                            Checkbox(checked = hasMinorStain, onCheckedChange = { hasMinorStain = it }, colors = CheckboxDefaults.colors(checkedColor = Primary))
                            Text(text = "Có vết ố mờ / sờn nhẹ khó thấy (đã chụp ở góc 4)", fontSize = 12.sp, color = TextPrimary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { hasMissingButton = !hasMissingButton }) {
                            Checkbox(checked = hasMissingButton, onCheckedChange = { hasMissingButton = it }, colors = CheckboxDefaults.colors(checkedColor = Primary))
                            Text(text = "Mất cúc phụ / Khóa kéo hơi rít đã xử lý", fontSize = 12.sp, color = TextPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Auto Grading Result Banner
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PrimarySoft15,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Hệ thống tự động gán nhãn:", fontSize = 11.sp, color = TextSecondary)
                                Box(
                                    modifier = Modifier
                                        .background(Primary, RoundedCornerShape(100.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${calculatedGrade.code}: ${calculatedGrade.label}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Text(
                                text = calculatedGrade.description,
                                fontSize = 12.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = flawNote,
                        onValueChange = { flawNote = it },
                        label = { Text("Ghi chú khuyết điểm minh bạch cho Buyer (nếu có)") },
                        placeholder = { Text("VD: Cúc áo thứ 2 hơi lỏng nhẹ...", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Số đo đo tay thực tế (cm):", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))

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
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = ActiveIconRed,
                                        checkedTrackColor = ActiveIconRed.copy(alpha = 0.35f),
                                        uncheckedThumbColor = Color(0xFF8E8E93),
                                        uncheckedTrackColor = Color(0xFFE5E5EA)
                                    )
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
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
