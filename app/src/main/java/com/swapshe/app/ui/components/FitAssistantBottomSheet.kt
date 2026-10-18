package com.swapshe.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.ui.responsive.responsiveContainerWidth
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitAssistantBottomSheet(
    initialHeightCm: Int = 158,
    initialWeightKg: Int = 48,
    initialBustCm: Int? = 84,
    initialWaistCm: Int? = 64,
    initialHipsCm: Int? = 90,
    initialSkinTone: String = "Tone ấm (Warm Undertone)",
    onDismissRequest: () -> Unit,
    onApply: (
        size: String,
        heightCm: Int,
        weightKg: Int,
        bustCm: Int?,
        waistCm: Int?,
        hipsCm: Int?,
        skinTone: String
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var heightVal by remember { mutableFloatStateOf(initialHeightCm.toFloat().coerceIn(140f, 185f)) }
    var weightVal by remember { mutableFloatStateOf(initialWeightKg.toFloat().coerceIn(38f, 85f)) }

    var bustStr by remember { mutableStateOf(initialBustCm?.toString() ?: "84") }
    var waistStr by remember { mutableStateOf(initialWaistCm?.toString() ?: "64") }
    var hipsStr by remember { mutableStateOf(initialHipsCm?.toString() ?: "90") }
    var showDetailedMeasurements by remember { mutableStateOf(false) }

    var selectedSkinTone by remember { mutableStateOf(initialSkinTone) }

    val currentHeight = heightVal.roundToInt()
    val currentWeight = weightVal.roundToInt()
    val currentWaist = waistStr.toIntOrNull()

    // Smart Asian/Vietnamese female sizing formula
    val recommendedSize = remember(currentHeight, currentWeight, currentWaist) {
        val w = currentWaist ?: 65
        when {
            currentWeight <= 43 || (currentHeight <= 152 && currentWeight <= 45) || (w in 1..62) -> "XS"
            currentWeight in 44..50 || (currentHeight in 153..160 && currentWeight in 44..51) || (w in 63..67) -> "S"
            currentWeight in 51..57 || (currentHeight in 161..167 && currentWeight in 52..58) || (w in 68..73) -> "M"
            currentWeight in 58..65 || (currentHeight in 168..174 && currentWeight in 59..65) || (w in 74..80) -> "L"
            else -> "XL"
        }
    }

    val undertoneOptions = listOf(
        Triple(
            "Tone ấm (Warm Undertone)",
            "Hợp màu Be, Nâu Camel, Rêu, Cam đất, Vàng ấm",
            listOf(Color(0xFFB38B4D), Color(0xFFFFFDD0), Color(0xFF9CAF88), Color(0xFFC86D51))
        ),
        Triple(
            "Tone lạnh (Cool Undertone)",
            "Hợp màu Hồng pastel, Xanh baby, Tím lavender, Trắng sữa",
            listOf(Color(0xFFE09397), Color(0xFFD4C2FC), Color(0xFF8AA8C4), Color(0xFFFAF6F0))
        ),
        Triple(
            "Tone trung tính (Neutral)",
            "Dễ phối mọi màu sắc: Đen, Kem, Xám khói, Xanh navy",
            listOf(Color(0xFF382C24), Color(0xFFE5D5C5), Color(0xFF8E8E93), Color(0xFF2C3E50))
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = SurfaceWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .responsiveContainerWidth(640.dp)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = PrimarySoft15,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Filled.Straighten, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                    }
                }
                Column {
                    Text(
                        text = "Trợ lý chọn Size & Tone da",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Tính chuẩn size dựa trên cân nặng, chiều cao & undertone",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 1. CÂN NẶNG (Weight)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = WarmCream.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Cân nặng của bạn", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(
                            text = "$currentWeight kg",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }

                    Slider(
                        value = weightVal,
                        onValueChange = { weightVal = it },
                        valueRange = 38f..85f,
                        steps = 46,
                        colors = SliderDefaults.colors(
                            thumbColor = Primary,
                            activeTrackColor = Primary,
                            inactiveTrackColor = BorderColor
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(44, 48, 52, 56, 62).forEach { w ->
                            val isSelected = currentWeight == w
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Primary else SurfaceWhite,
                                border = BorderStroke(1.dp, if (isSelected) Primary else BorderColor),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { weightVal = w.toFloat() }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${w}kg",
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. CHIỀU CAO (Height)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = WarmCream.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Chiều cao của bạn", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        val m = currentHeight / 100
                        val cm = currentHeight % 100
                        Text(
                            text = "${currentHeight} cm (${m}m${if (cm < 10) "0$cm" else "$cm"})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }

                    Slider(
                        value = heightVal,
                        onValueChange = { heightVal = it },
                        valueRange = 142f..182f,
                        steps = 39,
                        colors = SliderDefaults.colors(
                            thumbColor = Primary,
                            activeTrackColor = Primary,
                            inactiveTrackColor = BorderColor
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(150, 155, 158, 162, 168).forEach { h ->
                            val isSelected = currentHeight == h
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Primary else SurfaceWhite,
                                border = BorderStroke(1.dp, if (isSelected) Primary else BorderColor),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { heightVal = h.toFloat() }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${h}cm",
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. SỐ ĐO 3 VÒNG (Tùy chọn)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceWhite,
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDetailedMeasurements = !showDetailedMeasurements },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Số đo 3 vòng (Ngực • Eo • Mông)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Text(
                                text = if (showDetailedMeasurements) "Thu gọn bớt" else "Nhập thêm để size chuẩn xác 100% (Tùy chọn)",
                                fontSize = 11.sp,
                                color = Primary
                            )
                        }
                        Text(
                            text = if (showDetailedMeasurements) "▲" else "▼",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    AnimatedVisibility(visible = showDetailedMeasurements) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SwapsheTextField(
                                    value = bustStr,
                                    onValueChange = { bustStr = it },
                                    label = "Vòng 1 (cm)",
                                    placeholder = "84",
                                    keyboardType = KeyboardType.Number,
                                    modifier = Modifier.weight(1f)
                                )
                                SwapsheTextField(
                                    value = waistStr,
                                    onValueChange = { waistStr = it },
                                    label = "Vòng 2 (cm)",
                                    placeholder = "64",
                                    keyboardType = KeyboardType.Number,
                                    modifier = Modifier.weight(1f)
                                )
                                SwapsheTextField(
                                    value = hipsStr,
                                    onValueChange = { hipsStr = it },
                                    label = "Vòng 3 (cm)",
                                    placeholder = "90",
                                    keyboardType = KeyboardType.Number,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. UNDERTONE DA (Sắc độ da)
            Text(
                text = "Sắc độ da của bạn (Undertone):",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                undertoneOptions.forEach { (name, desc, palette) ->
                    val isSelected = selectedSkinTone == name
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) PrimarySoft15 else SurfaceWhite,
                        border = BorderStroke(1.5.dp, if (isSelected) Primary else BorderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedSkinTone = name }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                Text(
                                    text = name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) Primary else TextPrimary
                                )
                                Text(
                                    text = desc,
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                palette.forEach { col ->
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(col)
                                            .border(0.5.dp, BorderColor, CircleShape)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. LIVE AI FIT RESULT BOX
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrimarySoft15,
                border = BorderStroke(1.5.dp, Primary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Straighten, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "KẾT QUẢ TÍNH SIZE VỪA VẶN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary,
                                letterSpacing = 1.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Primary
                        ) {
                            Text(
                                text = "Size $recommendedSize",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Với vóc dáng ${currentHeight}cm - ${currentWeight}kg${if (currentWaist != null) " (vòng eo ${currentWaist}cm)" else ""}, bạn mặc đẹp và thoải mái nhất ở kích cỡ Size $recommendedSize.",
                        fontSize = 12.sp,
                        color = TextPrimary
                    )

                    val colorAdvice = when {
                        selectedSkinTone.contains("ấm", ignoreCase = true) -> "Ưu tiên trang phục màu Be, Nâu Camel, Rêu, Cam đất."
                        selectedSkinTone.contains("lạnh", ignoreCase = true) -> "Ưu tiên trang phục màu Hồng pastel, Xanh baby, Trắng kem."
                        else -> "Phù hợp mọi bảng màu thanh lịch, đặc biệt là Kem, Đen, Xám."
                    }

                    Text(
                        text = "• Bảng màu tôn da: $colorAdvice",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 6. ACTION ENTER BUTTON
            PrimaryButton(
                text = "Áp dụng & Lọc đồ vừa vặn vào Trang chủ ↵",
                onClick = {
                    onApply(
                        recommendedSize,
                        currentHeight,
                        currentWeight,
                        bustStr.toIntOrNull(),
                        waistStr.toIntOrNull(),
                        hipsStr.toIntOrNull(),
                        selectedSkinTone
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
