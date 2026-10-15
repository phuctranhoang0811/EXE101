package com.swapshe.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.data.MockData
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.BodyMeasurements
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SuccessBg
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.ui.components.ColorSwatchItem
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SizeChip
import com.swapshe.app.ui.components.StyleCard
import com.swapshe.app.ui.components.SwapsheTextField
import com.swapshe.app.ui.components.TextLinkButton

/* --------------------------------------------------------------------------
   F04: STYLE PREFERENCE (Step 1/3)
   -------------------------------------------------------------------------- */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StylePreferenceScreen(
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit
) {
    val repository = SwapSheRepository.instance
    var selectedStyles by remember { mutableStateOf(setOf("Vintage")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Step Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextLinkButton(text = "← Back", onClick = onBack)
            Text(
                text = "Step 1 of 3",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )
            TextLinkButton(text = "Skip", onClick = onSkip)
        }

        LinearProgressIndicator(
            progress = { 0.33f },
            color = Primary,
            trackColor = BorderColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .height(4.dp)
        )

        Text(
            text = "What's your style?",
            fontFamily = FontFamily.Serif,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Text(
            text = "Chọn phong cách bạn yêu thích để SWAPSHE gợi ý tủ đồ secondhand chuẩn gu.",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
        )

        // 2-column style grid
        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            MockData.styleOptions.forEach { style ->
                val isSelected = selectedStyles.contains(style.name)
                StyleCard(
                    name = style.name,
                    imageUrl = style.imageUrl,
                    isSelected = isSelected,
                    onClick = {
                        selectedStyles = if (isSelected) {
                            selectedStyles - style.name
                        } else {
                            selectedStyles + style.name
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.48f)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        PrimaryButton(
            text = "Continue →",
            enabled = selectedStyles.isNotEmpty(),
            onClick = {
                repository.updatePreferences(styles = selectedStyles)
                onContinue()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/* --------------------------------------------------------------------------
   F05: SIZE PROFILE (Step 2/3)
   -------------------------------------------------------------------------- */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SizeProfileScreen(
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit
) {
    val repository = SwapSheRepository.instance
    var selectedSize by remember { mutableStateOf("S") }
    var isExpandedMeasurements by remember { mutableStateOf(false) }

    var heightCm by remember { mutableStateOf("") }
    var bustCm by remember { mutableStateOf("") }
    var waistCm by remember { mutableStateOf("") }
    var hipsCm by remember { mutableStateOf("") }

    val sizes = listOf("XS", "S", "M", "L", "XL", "XXL", "Not sure")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextLinkButton(text = "← Back", onClick = onBack)
            Text(
                text = "Step 2 of 3",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )
            TextLinkButton(text = "Skip", onClick = onSkip)
        }

        LinearProgressIndicator(
            progress = { 0.66f },
            color = Primary,
            trackColor = BorderColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .height(4.dp)
        )

        Text(
            text = "Find your perfect fit",
            fontFamily = FontFamily.Serif,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Text(
            text = "Đồ secondhand mỗi hiệu có thể sai lệch phom dáng. Size bạn hay mặc là gì?",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
        )

        // Size chips
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            sizes.forEach { size ->
                SizeChip(
                    size = size,
                    isSelected = selectedSize == size,
                    onClick = { selectedSize = size }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Expandable measurements
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpandedMeasurements = !isExpandedMeasurements }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "+ Thêm số đo cơ thể",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Primary
                        )
                        Text(
                            text = "Tùy chọn • So khớp chính xác với số đo áo/váy",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Icon(
                        imageVector = if (isExpandedMeasurements) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Primary
                    )
                }

                AnimatedVisibility(visible = isExpandedMeasurements) {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            SwapsheTextField(
                                value = heightCm,
                                onValueChange = { heightCm = it },
                                label = "Chiều cao (cm)",
                                placeholder = "160",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                            SwapsheTextField(
                                value = bustCm,
                                onValueChange = { bustCm = it },
                                label = "Vòng 1 (cm)",
                                placeholder = "84",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            SwapsheTextField(
                                value = waistCm,
                                onValueChange = { waistCm = it },
                                label = "Vòng 2 (cm)",
                                placeholder = "66",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                            SwapsheTextField(
                                value = hipsCm,
                                onValueChange = { hipsCm = it },
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 12.dp, bottom = 28.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = " Số đo của bạn hoàn toàn riêng tư và không bao giờ công khai.",
                fontSize = 11.sp,
                color = TextMuted
            )
        }

        PrimaryButton(
            text = "Continue →",
            onClick = {
                repository.updatePreferences(
                    size = selectedSize,
                    measurements = BodyMeasurements(
                        heightCm = heightCm.toIntOrNull(),
                        bustCm = bustCm.toIntOrNull(),
                        waistCm = waistCm.toIntOrNull(),
                        hipsCm = hipsCm.toIntOrNull()
                    )
                )
                onContinue()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/* --------------------------------------------------------------------------
   F06: COLOR PREFERENCE (Step 3/3)
   -------------------------------------------------------------------------- */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPreferenceScreen(
    onFinish: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit
) {
    val repository = SwapSheRepository.instance
    var selectedColors by remember { mutableStateOf(setOf("Dusty Pink", "Cream")) }
    var isAllColors by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextLinkButton(text = "← Back", onClick = onBack)
            Text(
                text = "Step 3 of 3",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )
            TextLinkButton(text = "Skip", onClick = onSkip)
        }

        LinearProgressIndicator(
            progress = { 1.0f },
            color = Primary,
            trackColor = BorderColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .height(4.dp)
        )

        Text(
            text = "Tone màu trang phục",
            fontFamily = FontFamily.Serif,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Text(
            text = "Chọn những gam màu bạn tự tin nhất khi phối đồ.",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
        )

        // Color Swatches
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            MockData.colorOptions.forEach { colorOpt ->
                val isSelected = !isAllColors && selectedColors.contains(colorOpt.name)
                ColorSwatchItem(
                    name = colorOpt.name,
                    hex = colorOpt.hex,
                    isSelected = isSelected,
                    onClick = {
                        isAllColors = false
                        selectedColors = if (isSelected) {
                            selectedColors - colorOpt.name
                        } else {
                            selectedColors + colorOpt.name
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // All colors pill
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isAllColors) Primary else SurfaceWhite,
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isAllColors) Primary else BorderColor),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isAllColors = !isAllColors
                    if (isAllColors) selectedColors = emptySet()
                }
        ) {
            Box(
                modifier = Modifier.padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✨ Tôi yêu thích tất cả các màu (All Colors)",
                    color = if (isAllColors) Color.White else TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        PrimaryButton(
            text = "Hoàn tất thiết lập ✓",
            onClick = {
                val finalColors = if (isAllColors) setOf("All Colors") else selectedColors
                repository.updatePreferences(colors = finalColors)
                onFinish()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/* --------------------------------------------------------------------------
   F07: SETUP COMPLETED (P1)
   -------------------------------------------------------------------------- */
@Composable
fun SetupCompleteScreen(
    onExplorePicks: () -> Unit
) {
    val repository = SwapSheRepository.instance
    val user = repository.currentUser.value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(SuccessBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Hồ sơ đã sẵn sàng!",
            fontFamily = FontFamily.Serif,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Text(
            text = "SWAPSHE đã lưu sở thích và cá nhân hóa bảng tin thời trang secondhand dành riêng cho bạn.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 28.dp)
        )

        // Preference recap card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tóm tắt gu thời trang của bạn",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Phong cách:", fontSize = 13.sp, color = TextSecondary)
                    Text(
                        text = user.stylePreferences.joinToString(", ").ifEmpty { "Vintage, Minimal" },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Size chuẩn:", fontSize = 13.sp, color = TextSecondary)
                    Text(
                        text = user.sizePreference ?: "S",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Gam màu:", fontSize = 13.sp, color = TextSecondary)
                    Text(
                        text = user.colorPreferences.joinToString(", ").ifEmpty { "Dusty Pink, Cream" },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        PrimaryButton(
            text = "Khám phá tủ đồ cho tôi (Explore My Picks)",
            onClick = onExplorePicks
        )
    }
}
