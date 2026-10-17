package com.swapshe.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.model.Product
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import kotlin.math.abs
import kotlin.math.roundToInt

// Status Color Tokens
private val StatusTight = Color(0xFFEF4444)     // Đỏ: Chật
private val StatusSnug = Color(0xFFF59E0B)      // Vàng cam: Ôm sát
private val StatusPerfect = Color(0xFF10B981)   // Xanh lá: Vừa vặn
private val StatusLoose = Color(0xFF3B82F6)     // Xanh dương: Form rộng

enum class FitZoneStatus(val label: String, val color: Color, val bgColor: Color) {
    TIGHT("Chật / Bị kích", StatusTight, Color(0xFFFEF2F2)),
    SNUG("Ôm sát body", StatusSnug, Color(0xFFFFFBEB)),
    PERFECT("Vừa vặn thoải mái", StatusPerfect, Color(0xFFECFDF5)),
    LOOSE("Form rộng / Thoải mái", StatusLoose, Color(0xFFEFF6FF))
}

data class ZoneEvaluation(
    val status: FitZoneStatus,
    val delta: Int,
    val detailText: String,
    val shortDetail: String
)

data class BodyShapeOption(
    val id: String,
    val name: String,
    val subtitle: String,
    val icon: String,
    val height: Int,
    val bust: Int,
    val waist: Int,
    val hips: Int
)

private val BODY_SHAPE_PRESETS = listOf(
    BodyShapeOption("pear", "Quả lê (Pear)", "Vai nhỏ, hông nở", "🍐", 158, 80, 64, 96),
    BodyShapeOption("hourglass", "Đồng hồ cát", "Ngực & mông nở, eo thon", "⏳", 160, 88, 62, 92),
    BodyShapeOption("apple", "Quả táo (Apple)", "Thân trên & eo đầy đặn", "🍎", 156, 88, 72, 88),
    BodyShapeOption("rectangle", "Chữ nhật (Rectangle)", "Dáng mảnh khảnh, cân đối", "📏", 162, 82, 65, 86)
)

private fun evaluateZone(itemVal: Int?, buyerVal: Int): ZoneEvaluation? {
    if (itemVal == null) return null
    val delta = itemVal - buyerVal
    val status = when {
        delta < 0 -> FitZoneStatus.TIGHT
        delta <= 2 -> FitZoneStatus.SNUG
        delta <= 7 -> FitZoneStatus.PERFECT
        else -> FitZoneStatus.LOOSE
    }
    val detail = when (status) {
        FitZoneStatus.TIGHT -> "Chật - Bị kích khoảng ${abs(delta)} cm"
        FitZoneStatus.SNUG -> "Ôm sát body (+${delta} cm)"
        FitZoneStatus.PERFECT -> "Vừa vặn thoải mái (+${delta} cm)"
        FitZoneStatus.LOOSE -> "Form rộng / Thoải mái (+${delta} cm)"
    }
    val short = when (status) {
        FitZoneStatus.TIGHT -> "Chật (${delta}cm)"
        FitZoneStatus.SNUG -> "Ôm sát (+${delta}cm)"
        FitZoneStatus.PERFECT -> "Vừa khít (+${delta}cm)"
        FitZoneStatus.LOOSE -> "Form rộng (+${delta}cm)"
    }
    return ZoneEvaluation(status, delta, detail, short)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualFitStudioSheet(
    initialProduct: Product,
    allProducts: List<Product>,
    userHeightCm: Int = 158,
    userBustCm: Int = 84,
    userWaistCm: Int = 66,
    userHipsCm: Int = 90,
    onDismissRequest: () -> Unit,
    onApplyMeasurements: (height: Int, bust: Int, waist: Int, hips: Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var currentProduct by remember(initialProduct) { mutableStateOf(initialProduct) }

    var heightVal by remember { mutableFloatStateOf(userHeightCm.toFloat().coerceIn(145f, 180f)) }
    var bustVal by remember { mutableFloatStateOf(userBustCm.toFloat().coerceIn(70f, 110f)) }
    var waistVal by remember { mutableFloatStateOf(userWaistCm.toFloat().coerceIn(55f, 95f)) }
    var hipsVal by remember { mutableFloatStateOf(userHipsCm.toFloat().coerceIn(75f, 115f)) }
    var selectedShapeId by remember { mutableStateOf<String?>(null) }

    val curHeight = heightVal.roundToInt()
    val curBust = bustVal.roundToInt()
    val curWaist = waistVal.roundToInt()
    val curHips = hipsVal.roundToInt()

    // Zone evaluations
    val bustEval = remember(currentProduct, curBust) { evaluateZone(currentProduct.measurements.bust, curBust) }
    val waistEval = remember(currentProduct, curWaist) { evaluateZone(currentProduct.measurements.waist, curWaist) }
    val hipsEval = remember(currentProduct, curHips) { evaluateZone(currentProduct.measurements.hips, curHips) }

    val activeEvals = remember(bustEval, waistEval, hipsEval) {
        listOfNotNull(bustEval, waistEval, hipsEval)
    }

    val isTightAnywhere = remember(activeEvals) {
        activeEvals.any { it.status == FitZoneStatus.TIGHT }
    }

    val tightWarningMsg = remember(bustEval, waistEval, hipsEval, isTightAnywhere) {
        if (!isTightAnywhere) null
        else {
            val zones = mutableListOf<String>()
            if (bustEval?.status == FitZoneStatus.TIGHT) zones.add("Ngực")
            if (waistEval?.status == FitZoneStatus.TIGHT) zones.add("Eo")
            if (hipsEval?.status == FitZoneStatus.TIGHT) zones.add("Mông")
            "Món đồ này hơi nhỏ ở vùng ${zones.joinToString(" và ")} so với số đo của bạn, hãy cân nhắc nhé!"
        }
    }

    val matchScore = remember(activeEvals, isTightAnywhere) {
        if (isTightAnywhere) 42
        else {
            val scores = activeEvals.map {
                when (it.status) {
                    FitZoneStatus.TIGHT -> 30
                    FitZoneStatus.SNUG -> 88
                    FitZoneStatus.PERFECT -> 98
                    FitZoneStatus.LOOSE -> 84
                }
            }
            if (scores.isEmpty()) 90 else scores.average().roundToInt()
        }
    }

    val scoreColor = when {
        isTightAnywhere -> StatusTight
        matchScore < 85 -> StatusSnug
        else -> StatusPerfect
    }

    // Dynamic length ruler calculation
    val lengthRulerData = remember(currentProduct, curHeight) {
        val cat = currentProduct.category.lowercase()
        val itemLen = currentProduct.measurements.length ?: 80

        if (cat.contains("skirt") || cat.contains("pant") || cat.contains("quần") || cat.contains("váy")) {
            // Skirt / Pants: measured from waist down
            val realWaistToAnkle = curHeight * 0.58
            val pxRatio = (itemLen / realWaistToAnkle).coerceIn(0.15, 1.05)
            val waistToKnee = curHeight * 0.33
            val diffFromKnee = (itemLen - waistToKnee).roundToInt()

            val text = when {
                diffFromKnee < -12 -> "Ngắn trên gối khoảng ${abs(diffFromKnee)} cm (Dáng mini)"
                diffFromKnee < -2 -> "Chớm trên gối ${abs(diffFromKnee)} cm"
                diffFromKnee <= 3 -> "Chạm ngang đầu gối (Dáng chuẩn)"
                diffFromKnee <= 16 -> "Qua gối khoảng ${diffFromKnee} cm (Với chiều cao ${curHeight}cm)"
                diffFromKnee <= 26 -> "Ngang bắp chân (${diffFromKnee}cm qua gối)"
                else -> "Chạm mắt cá chân (Dáng dài)"
            }
            Pair(pxRatio.toFloat(), text)
        } else {
            // Dress / Coat / Tops: measured from shoulder down
            val realShoulderToAnkle = curHeight * 0.78
            val pxRatio = (itemLen / realShoulderToAnkle).coerceIn(0.2, 1.05)
            val shoulderToKnee = curHeight * 0.533
            val diffFromKnee = (itemLen - shoulderToKnee).roundToInt()

            val text = when {
                diffFromKnee < -12 -> "Ngắn trên gối ${abs(diffFromKnee)} cm"
                diffFromKnee < -2 -> "Chớm trên gối ${abs(diffFromKnee)} cm"
                diffFromKnee <= 3 -> "Chạm ngang đầu gối"
                diffFromKnee <= 16 -> "Qua gối khoảng ${diffFromKnee} cm (Với chiều cao ${curHeight}cm)"
                diffFromKnee <= 28 -> "Ngang bắp chân (${diffFromKnee}cm qua gối)"
                else -> "Chạm mắt cá chân (Dáng maxi)"
            }
            Pair(pxRatio.toFloat(), text)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = SurfaceWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. TOP HEADER (CLEAN FASHION ICONS, NO AI)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = PrimarySoft15,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Straighten,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Phòng Thử Đồ 2D & Thước Đo Vừa Vặn",
                            fontFamily = FontFamily.Serif,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Mô phỏng phom dáng trên Mannequin & Thước đo độ dài gấu áo",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
                IconButton(onClick = onDismissRequest) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. GARMENT SELECTOR CAROUSEL (BROWSE ALL CLOTHES IN APP)
            Text(
                text = "MÓN ĐỒ ĐANG THỬ TRÊN MANNEQUIN (CHỌN MÓN ĐỂ ĐO):",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(allProducts) { item ->
                    val isSelected = item.id == currentProduct.id
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) PrimarySoft15 else SurfaceWhite,
                        border = BorderStroke(1.5.dp, if (isSelected) Primary else BorderColor),
                        modifier = Modifier
                            .width(160.dp)
                            .clickable { currentProduct = item }
                    ) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = item.images.firstOrNull(),
                                contentDescription = item.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = item.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    color = if (isSelected) Primary else TextPrimary
                                )
                                Text(
                                    text = "${item.category} • Size ${item.size}",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                                Text(
                                    text = item.fabricStretch,
                                    fontSize = 9.sp,
                                    color = Color(0xFFC86D51),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. MATCH SCORE BAR & WARNING BANNER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ĐỘ TƯƠNG THÍCH VỚI DÁNG BẠN:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Text(
                        text = currentProduct.title,
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1
                    )
                }

                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = scoreColor.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, scoreColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "★ $matchScore% Match",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = scoreColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            if (isTightAnywhere && tightWarningMsg != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFEF2F2),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⚠️", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = tightWarningMsg,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFB91C1C)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. 2D MANNEQUIN CANVAS WITH HEATMAP & LENGTH RULER
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = WarmCream.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.58f) // Khóa cứng tỷ lệ Hình chữ nhật đứng (Rộng : Cao = 1 : 1.72 giống hệt bản Web)
            ) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val boxHeight = maxHeight
                    val boxWidth = maxWidth

                    // Calculate virtual hemY in 520px coordinate system:
                    val catLower = currentProduct.category.lowercase()
                    val rulerRatio = lengthRulerData.first
                    val hemY = if (catLower.contains("skirt") || catLower.contains("pant") || catLower.contains("quần") || catLower.contains("váy")) {
                        (195f + rulerRatio * (485f - 195f)).coerceIn(210f, 495f)
                    } else {
                        (95f + rulerRatio * (485f - 95f)).coerceIn(160f, 495f)
                    }

                    // Compose Canvas 2D Mannequin with fixed ViewBox 260x520
                    Mannequin2DCanvas(
                        buyerHeight = curHeight,
                        buyerBust = curBust,
                        buyerWaist = curWaist,
                        buyerHips = curHips,
                        selectedShapeId = selectedShapeId,
                        garment = currentProduct,
                        bustEval = bustEval,
                        waistEval = waistEval,
                        hipsEval = hipsEval,
                        lengthRatio = rulerRatio,
                        lengthText = lengthRulerData.second,
                        hemY = hemY,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Synchronized Y positions in dp based on virtual 520px canvas:
                    val bustTopDp = (boxHeight * (145f / 520f) - 18.dp).coerceAtLeast(0.dp)
                    val waistTopDp = (boxHeight * (195f / 520f) - 18.dp).coerceAtLeast(0.dp)
                    val hipsTopDp = (boxHeight * (255f / 520f) - 18.dp).coerceAtLeast(0.dp)
                    val rulerTopDp = (boxHeight * (hemY / 520f) - 13.dp).coerceAtLeast(0.dp)

                    // Side Callout Badges (Compact Pill / Badge, max 125dp, backdrop-like white)
                    bustEval?.let {
                        if (!currentProduct.category.lowercase().contains("skirt") &&
                            !currentProduct.category.lowercase().contains("pant")
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xEBFFFFFF),
                                border = BorderStroke(1.dp, it.status.color.copy(alpha = 0.45f)),
                                shadowElevation = 2.dp,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(start = 6.dp, top = bustTopDp)
                                    .widthIn(max = 125.dp)
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(it.status.color)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "VÒNG 1: ${currentProduct.measurements.bust}cm",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = it.status.color
                                        )
                                    }
                                    Text(
                                        text = it.shortDetail,
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp,
                                        color = TextPrimary.copy(alpha = 0.85f),
                                        maxLines = 1,
                                        modifier = Modifier.padding(top = 1.dp)
                                    )
                                }
                            }
                        }
                    }

                    waistEval?.let {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xEBFFFFFF),
                            border = BorderStroke(1.dp, it.status.color.copy(alpha = 0.45f)),
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 6.dp, top = waistTopDp)
                                .widthIn(max = 125.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "VÒNG 2: ${currentProduct.measurements.waist}cm",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = it.status.color
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(it.status.color)
                                    )
                                }
                                Text(
                                    text = it.shortDetail,
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp,
                                    color = TextPrimary.copy(alpha = 0.85f),
                                    maxLines = 1,
                                    modifier = Modifier.padding(top = 1.dp)
                                )
                            }
                        }
                    }

                    hipsEval?.let {
                        if (currentProduct.measurements.hips != null) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xEBFFFFFF),
                                border = BorderStroke(1.dp, it.status.color.copy(alpha = 0.45f)),
                                shadowElevation = 2.dp,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(start = 6.dp, top = hipsTopDp)
                                    .widthIn(max = 125.dp)
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(it.status.color)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "VÒNG 3: ${currentProduct.measurements.hips}cm",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = it.status.color
                                        )
                                    }
                                    Text(
                                        text = it.shortDetail,
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp,
                                        color = TextPrimary.copy(alpha = 0.85f),
                                        maxLines = 1,
                                        modifier = Modifier.padding(top = 1.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Floating Ruler Tag Badge (Compact, fontSize 11sp, padding 12x5dp)
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = Color(0xEE241A14),
                        shadowElevation = 3.dp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = rulerTopDp)
                    ) {
                        Text(
                            text = "📍 Gấu đồ: ${lengthRulerData.second}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5. COLOR HEATMAP LEGEND (EXPLAINING DELTA = ĐỒ - NGƯỜI)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                LegendChip("Chật (<0cm)", StatusTight, Modifier.weight(1f))
                LegendChip("Ôm sát (0-2cm)", StatusSnug, Modifier.weight(1f))
                LegendChip("Vừa khít (2-7cm)", StatusPerfect, Modifier.weight(1f))
                LegendChip("Rộng (>7cm)", StatusLoose, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 6. BUYER CONTROLS: 4 BODY SHAPES PRESETS (REQUIREMENT 5: 2x2 GRID WITH ICONS)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Chọn Nhanh Dáng Người Chuẩn",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "4 Phom cơ thể",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Requirement 5: 2x2 Grid instead of 1 crowded row
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Row 1: Pear & Hourglass
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(BODY_SHAPE_PRESETS[0], BODY_SHAPE_PRESETS[1]).forEach { preset ->
                        val isSelected = selectedShapeId == preset.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) PrimarySoft15 else SurfaceWhite,
                            border = BorderStroke(1.2.dp, if (isSelected) Primary else BorderColor),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedShapeId = preset.id
                                    heightVal = preset.height.toFloat()
                                    bustVal = preset.bust.toFloat()
                                    waistVal = preset.waist.toFloat()
                                    hipsVal = preset.hips.toFloat()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = preset.icon, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = preset.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Primary else TextPrimary,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = preset.subtitle,
                                        fontSize = 10.sp,
                                        color = TextSecondary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
                // Row 2: Apple & Rectangle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(BODY_SHAPE_PRESETS[2], BODY_SHAPE_PRESETS[3]).forEach { preset ->
                        val isSelected = selectedShapeId == preset.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) PrimarySoft15 else SurfaceWhite,
                            border = BorderStroke(1.2.dp, if (isSelected) Primary else BorderColor),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedShapeId = preset.id
                                    heightVal = preset.height.toFloat()
                                    bustVal = preset.bust.toFloat()
                                    waistVal = preset.waist.toFloat()
                                    hipsVal = preset.hips.toFloat()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = preset.icon, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = preset.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Primary else TextPrimary,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = preset.subtitle,
                                        fontSize = 10.sp,
                                        color = TextSecondary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 7. SLIDERS: CHIỀU CAO & 3 VÒNG
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.Tune, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Điều chỉnh số đo thực của bạn",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "Khôi phục",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary,
                    modifier = Modifier.clickable {
                        heightVal = 158f
                        bustVal = 84f
                        waistVal = 66f
                        hipsVal = 90f
                        selectedShapeId = null
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Height Slider
            SliderRow(
                label = "Chiều cao",
                valueText = "$curHeight cm",
                currentVal = heightVal,
                minVal = 145f,
                maxVal = 180f,
                steps = 35,
                onValueChange = {
                    heightVal = it
                    selectedShapeId = null
                }
            )

            // Bust Slider
            SliderRow(
                label = "Vòng 1 (Ngực)",
                valueText = "$curBust cm",
                currentVal = bustVal,
                minVal = 70f,
                maxVal = 110f,
                steps = 40,
                statusColor = bustEval?.status?.color,
                onValueChange = {
                    bustVal = it
                    selectedShapeId = null
                }
            )

            // Waist Slider
            SliderRow(
                label = "Vòng 2 (Eo)",
                valueText = "$curWaist cm",
                currentVal = waistVal,
                minVal = 55f,
                maxVal = 95f,
                steps = 40,
                statusColor = waistEval?.status?.color,
                onValueChange = {
                    waistVal = it
                    selectedShapeId = null
                }
            )

            // Hips Slider
            SliderRow(
                label = "Vòng 3 (Mông)",
                valueText = "$curHips cm",
                currentVal = hipsVal,
                minVal = 75f,
                maxVal = 115f,
                steps = 40,
                statusColor = hipsEval?.status?.color,
                onValueChange = {
                    hipsVal = it
                    selectedShapeId = null
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 8. APPLY BUTTON
            PrimaryButton(
                text = "Áp dụng số đo & Lưu hồ sơ thử đồ ↵",
                onClick = {
                    onApplyMeasurements(curHeight, curBust, curWaist, curHips)
                    onDismissRequest()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun LegendChip(text: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = color, maxLines = 1)
        }
    }
}

@Composable
private fun SliderRow(
    label: String,
    valueText: String,
    currentVal: Float,
    minVal: Float,
    maxVal: Float,
    steps: Int,
    statusColor: Color? = null,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                statusColor?.let {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(it))
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(text = valueText, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary)
            }
        }
        Slider(
            value = currentVal,
            onValueChange = onValueChange,
            valueRange = minVal..maxVal,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = Primary,
                activeTrackColor = Primary,
                inactiveTrackColor = BorderColor
            )
        )
    }
}

@Composable
private fun Mannequin2DCanvas(
    buyerHeight: Int,
    buyerBust: Int,
    buyerWaist: Int,
    buyerHips: Int,
    selectedShapeId: String?,
    garment: Product,
    bustEval: ZoneEvaluation?,
    waistEval: ZoneEvaluation?,
    hipsEval: ZoneEvaluation?,
    lengthRatio: Float,
    lengthText: String,
    hemY: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        // Step 2: Virtual Coordinate System (ViewBox 260 x 520, preserveAspectRatio xMidYMid meet)
        val virtualW = 260f
        val virtualH = 520f
        val scale = minOf(size.width / virtualW, size.height / virtualH)
        val transX = (size.width - virtualW * scale) / 2f
        val transY = (size.height - virtualH * scale) / 2f

        withTransform({
            translate(left = transX, top = transY)
            scale(scaleX = scale, scaleY = scale, pivot = Offset.Zero)
        }) {
            val cx = 130f
            val H = 520f

            // Body shape nuance modifiers
            val isPear = selectedShapeId == "pear"
            val isApple = selectedShapeId == "apple"
            val isHourglass = selectedShapeId == "hourglass"
            val isRectangle = selectedShapeId == "rectangle"

            val shoulderScale = when {
                isPear -> 0.92f
                isApple -> 0.98f
                isRectangle -> 0.96f
                else -> 0.98f
            }
            val hipScale = when {
                isPear -> 1.08f
                isApple -> 0.96f
                isHourglass -> 1.04f
                isRectangle -> 0.95f
                else -> 0.98f
            }
            val waistScale = when {
                isApple -> 1.04f
                isHourglass -> 0.88f
                isRectangle -> 0.98f
                else -> 0.95f
            }

            // Golden proportions: Body width ~35%-40% of canvas width (cx=130, hipsHalfW=34, body width ~68-84px)
            val shoulderHalfW = 24f * shoulderScale
            val bustHalfW = (29f * (buyerBust / 84f)).coerceIn(24f, 36f)
            val waistHalfW = (20f * (buyerWaist / 66f) * waistScale).coerceIn(16f, 27f)
            val hipsHalfW = (34f * (buyerHips / 90f) * hipScale).coerceIn(28f, 42f)

            // Key vertical Y anatomical landmarks (Standard 520px high-fashion croquis)
            val neckTopY = 60f
            val shoulderY = 95f
            val bustY = 145f
            val waistY = 195f
            val hipsY = 255f
            val crotchY = 275f
            val midThighY = 320f
            val kneeY = 375f
            val calfY = 430f
            val ankleY = 485f
            val footY = 505f

            // Colors: line-art gold/sand, soft opacity
            val contourColor = Color(0xFFBCA173).copy(alpha = 0.65f)
            val detailColor = Color(0xFFBCA173).copy(alpha = 0.42f)
            val skinBrush = Brush.linearGradient(
                colors = listOf(Color(0xFFFDFBF7), Color(0xFFF8EFE4), Color(0xFFEDE1D1), Color(0xFFFAF5EE)),
                start = Offset(cx - hipsHalfW * 1.5f, 0f),
                end = Offset(cx + hipsHalfW * 1.5f, 0f)
            )

            val garmentStroke = Color(0xFFDCA3A7)
            val garmentFill = Color(0xFFDCA3A7).copy(alpha = 0.08f)
            val garmentDashed = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))

            // 1. LEFT TECHNICAL LANDMARKS (Vai, Eo, Gối, Mắt cá in #A89F91)
            val guidePaint = Color(0xFFBCA173).copy(alpha = 0.50f)
            val guideDash = PathEffect.dashPathEffect(floatArrayOf(3f, 3f))
            val landmarkStartX = 6f
            val landmarkEndX = 18f
            val yLandmarks = listOf(
                Pair(shoulderY, "Vai"),
                Pair(waistY, "Eo"),
                Pair(kneeY, "Gối"),
                Pair(ankleY, "Mắt cá")
            )
            yLandmarks.forEach { (y, label) ->
                drawLine(guidePaint, Offset(landmarkStartX, y), Offset(landmarkEndX, y), strokeWidth = 0.8f, pathEffect = guideDash)
                drawCircle(guidePaint, radius = 1.5f, center = Offset(landmarkStartX, y))
                drawCircle(guidePaint, radius = 1.5f, center = Offset(landmarkEndX, y))
                drawContext.canvas.nativeCanvas.apply {
                    val p = android.graphics.Paint().apply {
                        color = android.graphics.Color.parseColor("#A89F91")
                        textSize = 8.5f
                        isAntiAlias = true
                        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.NORMAL)
                    }
                    drawText(label, 22f, y + 3f, p)
                }
            }

            // 2. PEAR SHAPE GUIDE AURA
            if (isPear) {
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFBCA173).copy(alpha = 0.08f), Color(0xFFBCA173).copy(alpha = 0.02f), Color.Transparent),
                        center = Offset(130f, 225f),
                        radius = 55f
                    ),
                    topLeft = Offset(75f, 150f),
                    size = Size(110f, 150f)
                )
                drawOval(
                    color = Color(0xFFBCA173).copy(alpha = 0.45f),
                    topLeft = Offset(75f, 150f),
                    size = Size(110f, 150f),
                    style = Stroke(width = 0.8f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f)))
                )
            }

            // 3. TORSO AND LEGS SILHOUETTE
            val torsoLegsPath = Path().apply {
                moveTo(cx - 7f, shoulderY - 5f)
                cubicTo(
                    cx - 15f, shoulderY - 2f,
                    cx - shoulderHalfW + 2f, shoulderY - 1f,
                    cx - shoulderHalfW, shoulderY
                )
                cubicTo(
                    cx - shoulderHalfW - 1f, shoulderY + 18f,
                    cx - bustHalfW, bustY - 15f,
                    cx - bustHalfW, bustY
                )
                cubicTo(
                    cx - bustHalfW, bustY + 20f,
                    cx - waistHalfW, waistY - 20f,
                    cx - waistHalfW, waistY
                )
                cubicTo(
                    cx - waistHalfW, waistY + 22f,
                    cx - hipsHalfW, hipsY - 20f,
                    cx - hipsHalfW, hipsY
                )
                cubicTo(
                    cx - hipsHalfW, hipsY + 30f,
                    cx - 24f, kneeY - 25f,
                    cx - 18f, kneeY
                )
                cubicTo(
                    cx - 17f, kneeY + 18f,
                    cx - 20f, calfY,
                    cx - 18f, calfY + 18f
                )
                cubicTo(
                    cx - 15f, ankleY - 18f,
                    cx - 11f, ankleY - 5f,
                    cx - 10f, ankleY
                )
                cubicTo(
                    cx - 11f, ankleY + 10f,
                    cx - 12f, footY - 5f,
                    cx - 10f, footY
                )
                lineTo(cx - 5f, footY)
                cubicTo(
                    cx - 4f, footY - 8f,
                    cx - 5f, ankleY + 8f,
                    cx - 5.5f, ankleY
                )
                cubicTo(
                    cx - 6f, calfY + 15f,
                    cx - 9f, calfY,
                    cx - 8f, kneeY + 12f
                )
                lineTo(cx - 8.5f, kneeY)
                cubicTo(
                    cx - 9f, kneeY - 25f,
                    cx - 6f, crotchY + 20f,
                    cx - 1.5f, crotchY
                )
                lineTo(cx + 1.5f, crotchY)
                cubicTo(
                    cx + 6f, crotchY + 20f,
                    cx + 9f, kneeY - 25f,
                    cx + 8.5f, kneeY
                )
                lineTo(cx + 8f, kneeY + 12f)
                cubicTo(
                    cx + 9f, calfY,
                    cx + 6f, calfY + 15f,
                    cx + 5.5f, ankleY
                )
                cubicTo(
                    cx + 5f, ankleY + 8f,
                    cx + 4f, footY - 8f,
                    cx + 5f, footY
                )
                lineTo(cx + 10f, footY)
                cubicTo(
                    cx + 12f, footY - 5f,
                    cx + 11f, ankleY + 10f,
                    cx + 10f, ankleY
                )
                cubicTo(
                    cx + 11f, ankleY - 5f,
                    cx + 15f, ankleY - 18f,
                    cx + 18f, calfY + 18f
                )
                cubicTo(
                    cx + 20f, calfY,
                    cx + 17f, kneeY + 18f,
                    cx + 18f, kneeY
                )
                cubicTo(
                    cx + 24f, kneeY - 25f,
                    cx + hipsHalfW, hipsY + 30f,
                    cx + hipsHalfW, hipsY
                )
                cubicTo(
                    cx + hipsHalfW, hipsY - 20f,
                    cx + waistHalfW, waistY + 22f,
                    cx + waistHalfW, waistY
                )
                cubicTo(
                    cx + waistHalfW, waistY - 20f,
                    cx + bustHalfW, bustY + 20f,
                    cx + bustHalfW, bustY
                )
                cubicTo(
                    cx + bustHalfW, bustY - 15f,
                    cx + shoulderHalfW + 1f, shoulderY + 18f,
                    cx + shoulderHalfW, shoulderY
                )
                cubicTo(
                    cx + shoulderHalfW - 2f, shoulderY - 1f,
                    cx + 15f, shoulderY - 2f,
                    cx + 7f, shoulderY - 5f
                )
                close()
            }
            drawPath(torsoLegsPath, brush = skinBrush)
            drawPath(torsoLegsPath, color = contourColor, style = Stroke(width = 1.2f))

            // Inseam subtle line
            drawLine(
                color = detailColor,
                start = Offset(cx, crotchY),
                end = Offset(cx, kneeY - 12f),
                strokeWidth = 0.8f
            )

            // 4. FASHION ARMS
            val leftArmPath = Path().apply {
                moveTo(cx - shoulderHalfW, shoulderY)
                cubicTo(
                    cx - shoulderHalfW - 7f, shoulderY + 28f,
                    cx - waistHalfW - 15f, waistY - 12f,
                    cx - waistHalfW - 14f, waistY + 6f
                )
                cubicTo(
                    cx - waistHalfW - 13f, waistY + 28f,
                    cx - hipsHalfW - 8f, hipsY + 12f,
                    cx - hipsHalfW - 5f, midThighY
                )
                cubicTo(
                    cx - hipsHalfW - 3f, midThighY + 7f,
                    cx - hipsHalfW - 0.5f, midThighY + 3f,
                    cx - hipsHalfW - 0.5f, midThighY - 7f
                )
                cubicTo(
                    cx - hipsHalfW - 1.5f, hipsY + 9f,
                    cx - waistHalfW - 4.5f, waistY + 6f,
                    cx - waistHalfW - 4.5f, waistY - 6f
                )
                cubicTo(
                    cx - bustHalfW - 3.5f, bustY + 9f,
                    cx - shoulderHalfW + 2f, shoulderY + 15f,
                    cx - shoulderHalfW + 1f, shoulderY + 6f
                )
                close()
            }
            drawPath(leftArmPath, brush = skinBrush)
            drawPath(leftArmPath, color = contourColor, style = Stroke(width = 1.1f))

            val rightArmPath = Path().apply {
                moveTo(cx + shoulderHalfW, shoulderY)
                cubicTo(
                    cx + shoulderHalfW + 10f, shoulderY + 22f,
                    cx + shoulderHalfW + 16f, shoulderY + 45f,
                    cx + shoulderHalfW + 15f, shoulderY + 65f
                )
                cubicTo(
                    cx + shoulderHalfW + 12f, shoulderY + 80f,
                    cx + waistHalfW + 11f, waistY - 3f,
                    cx + waistHalfW + 3f, waistY + 8f
                )
                lineTo(cx + waistHalfW - 2f, waistY + 4f)
                cubicTo(
                    cx + waistHalfW + 3f, waistY - 7f,
                    cx + shoulderHalfW + 4f, shoulderY + 70f,
                    cx + shoulderHalfW + 5f, shoulderY + 58f
                )
                cubicTo(
                    cx + shoulderHalfW + 4f, shoulderY + 38f,
                    cx + bustHalfW + 3f, bustY - 8f,
                    cx + shoulderHalfW - 1f, shoulderY + 6f
                )
                close()
            }
            drawPath(rightArmPath, brush = skinBrush)
            drawPath(rightArmPath, color = contourColor, style = Stroke(width = 1.1f))

            val finger1 = Path().apply {
                moveTo(cx + waistHalfW + 4f, waistY + 6f)
                lineTo(cx + waistHalfW - 3f, waistY + 4f)
            }
            val finger2 = Path().apply {
                moveTo(cx + waistHalfW + 3.5f, waistY + 10f)
                lineTo(cx + waistHalfW - 1.5f, waistY + 8f)
            }
            drawPath(finger1, color = contourColor, style = Stroke(width = 0.9f))
            drawPath(finger2, color = contourColor, style = Stroke(width = 0.8f))

            // 5. SWAN NECK & HIGH-FASHION HEAD WITH ARTISTIC BUN
            val neckPath = Path().apply {
                moveTo(125f, 60f)
                cubicTo(125f, 72f, 122f, 84f, 116f, 95f)
                lineTo(144f, 95f)
                cubicTo(138f, 84f, 135f, 72f, 135f, 60f)
                close()
            }
            drawPath(neckPath, brush = skinBrush)
            drawPath(neckPath, color = contourColor, style = Stroke(width = 1f))

            drawLine(detailColor, Offset(129f, 64f), Offset(126f, 92f), strokeWidth = 0.7f)
            drawLine(detailColor, Offset(133f, 65f), Offset(137f, 92f), strokeWidth = 0.7f)

            // Head Base
            val headPath = Path().apply {
                addOval(Rect(Offset(129f - 11f, 48f - 15f), Size(22f, 30f)))
            }
            drawPath(headPath, brush = skinBrush)
            drawPath(headPath, color = contourColor, style = Stroke(width = 1f))

            // Artistic Loose High Bun
            val bunPath = Path().apply {
                moveTo(122f, 32f)
                cubicTo(116f, 23f, 121f, 12f, 131f, 13f)
                cubicTo(141f, 14f, 145f, 22f, 141f, 31f)
                cubicTo(137f, 35f, 127f, 35f, 122f, 32f)
                close()
            }
            drawPath(bunPath, brush = skinBrush)
            drawPath(bunPath, color = contourColor, style = Stroke(width = 1.1f))

            // Bun swirls
            val bunSwirl1 = Path().apply {
                moveTo(126f, 27f)
                cubicTo(128f, 19f, 136f, 19f, 134f, 27f)
            }
            val bunSwirl2 = Path().apply {
                moveTo(123f, 23f)
                cubicTo(127f, 16f, 135f, 17f, 132f, 25f)
            }
            val topKnot = Path().apply {
                moveTo(128f, 16f)
                cubicTo(130f, 11f, 136f, 12f, 134f, 17f)
            }
            drawPath(bunSwirl1, color = contourColor, style = Stroke(width = 0.8f))
            drawPath(bunSwirl2, color = detailColor, style = Stroke(width = 0.7f))
            drawPath(topKnot, color = contourColor, style = Stroke(width = 0.8f))

            // Wisps
            val wisp1 = Path().apply {
                moveTo(119f, 35f)
                cubicTo(116f, 41f, 117f, 47f, 120f, 50f)
            }
            val wisp2 = Path().apply {
                moveTo(138f, 34f)
                cubicTo(143f, 39f, 141f, 46f, 138f, 50f)
            }
            val wisp3 = Path().apply {
                moveTo(121f, 54f)
                cubicTo(119f, 60f, 120f, 69f, 122f, 74f)
            }
            drawPath(wisp1, color = detailColor, style = Stroke(width = 0.7f))
            drawPath(wisp2, color = detailColor, style = Stroke(width = 0.7f))
            drawPath(wisp3, color = detailColor, style = Stroke(width = 0.7f))

            // 3/4 Face Profile Line-Art
            val eyebrow = Path().apply {
                moveTo(131f, 41f)
                quadraticTo(134f, 39f, 138f, 41f)
            }
            val eyeLash = Path().apply {
                moveTo(132f, 45f)
                quadraticTo(135f, 47f, 138f, 44f)
            }
            val nose = Path().apply {
                moveTo(136f, 43f)
                quadraticTo(140f, 47f, 141f, 50f)
                lineTo(138f, 51f)
            }
            val lips = Path().apply {
                moveTo(134f, 54f)
                quadraticTo(137f, 55f, 140f, 54f)
            }
            val chin = Path().apply {
                moveTo(140f, 53f)
                cubicTo(142f, 57f, 136f, 63f, 129f, 63f)
            }
            drawPath(eyebrow, color = contourColor, style = Stroke(width = 0.8f))
            drawPath(eyeLash, color = contourColor, style = Stroke(width = 0.9f))
            drawPath(nose, color = contourColor, style = Stroke(width = 0.8f))
            drawPath(lips, color = contourColor, style = Stroke(width = 0.8f))
            drawPath(chin, color = contourColor, style = Stroke(width = 0.8f))
            drawCircle(contourColor, radius = 1.5f, center = Offset(118f, 52f))

            // Clavicles
            val clavicleLeft = Path().apply {
                moveTo(126f, 96f)
                cubicTo(118f, 96f, 109f, 93f, 98f, 95f)
            }
            val clavicleRight = Path().apply {
                moveTo(134f, 96f)
                cubicTo(142f, 96f, 151f, 93f, 162f, 95f)
            }
            drawPath(clavicleLeft, color = detailColor, style = Stroke(width = 0.9f))
            drawPath(clavicleRight, color = detailColor, style = Stroke(width = 0.9f))

            // Breast under-curves
            val bustLeft = Path().apply {
                moveTo(cx - bustHalfW * 0.85f, bustY)
                cubicTo(cx - bustHalfW * 0.8f, bustY + 12f, cx - 10f, bustY + 14f, cx - 4f, bustY + 6f)
            }
            val bustRight = Path().apply {
                moveTo(cx + bustHalfW * 0.85f, bustY)
                cubicTo(cx + bustHalfW * 0.8f, bustY + 12f, cx + 10f, bustY + 14f, cx + 4f, bustY + 6f)
            }
            drawPath(bustLeft, color = detailColor, style = Stroke(width = 0.9f))
            drawPath(bustRight, color = detailColor, style = Stroke(width = 0.9f))

            drawCircle(detailColor, radius = 1.4f, center = Offset(cx, waistY + 20f))

            val kneeCreaseLeft = Path().apply {
                moveTo(cx - 20f, kneeY)
                quadraticTo(cx - 18f, kneeY + 2f, cx - 16f, kneeY)
            }
            val kneeCreaseRight = Path().apply {
                moveTo(cx + 16f, kneeY)
                quadraticTo(cx + 18f, kneeY + 2f, cx + 20f, kneeY)
            }
            drawPath(kneeCreaseLeft, color = detailColor, style = Stroke(width = 0.8f))
            drawPath(kneeCreaseRight, color = detailColor, style = Stroke(width = 0.8f))

            // 6. GARMENT SILHOUETTE OVERLAY
            val cat = garment.category.lowercase()
            when {
                cat.contains("dress") || cat.contains("đầm") -> {
                    val dressPath = Path().apply {
                        moveTo(cx - shoulderHalfW * 0.7f, shoulderY)
                        quadraticTo(cx, shoulderY + 8f, cx + shoulderHalfW * 0.7f, shoulderY)
                        lineTo(cx + bustHalfW + 7f, bustY)
                        quadraticTo(cx + waistHalfW + 6f, waistY, cx + hipsHalfW + 10f, hipsY)
                        lineTo(cx + hipsHalfW + 26f, hemY)
                        cubicTo(
                            cx + 20f, hemY + 7f,
                            cx - 20f, hemY - 5f,
                            cx - hipsHalfW - 26f, hemY
                        )
                        lineTo(cx - hipsHalfW - 10f, hipsY)
                        quadraticTo(cx - waistHalfW - 6f, waistY, cx - bustHalfW - 7f, bustY)
                        close()
                    }
                    drawPath(dressPath, color = garmentFill)
                    drawPath(dressPath, color = garmentStroke, style = Stroke(width = 1.5f, pathEffect = garmentDashed))

                    val drape1 = Path().apply {
                        moveTo(cx - waistHalfW * 0.3f, waistY + 6f)
                        cubicTo(cx - waistHalfW * 0.4f, hipsY, cx - hipsHalfW * 0.5f, (hipsY + hemY) / 2f, cx - hipsHalfW * 0.25f, hemY)
                    }
                    val drape2 = Path().apply {
                        moveTo(cx + waistHalfW * 0.3f, waistY + 6f)
                        cubicTo(cx + waistHalfW * 0.4f, hipsY, cx + hipsHalfW * 0.5f, (hipsY + hemY) / 2f, cx + hipsHalfW * 0.25f, hemY)
                    }
                    drawPath(drape1, color = garmentStroke.copy(alpha = 0.45f), style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))))
                    drawPath(drape2, color = garmentStroke.copy(alpha = 0.45f), style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))))
                }
                cat.contains("coat") || cat.contains("outerwear") || cat.contains("khoác") -> {
                    val coatPath = Path().apply {
                        moveTo(cx - shoulderHalfW - 6f, shoulderY - 3f)
                        lineTo(cx + shoulderHalfW + 6f, shoulderY - 3f)
                        lineTo(cx + bustHalfW + 14f, bustY)
                        lineTo(cx + waistHalfW + 16f, waistY)
                        lineTo(cx + hipsHalfW + 20f, hemY)
                        cubicTo(cx + 15f, hemY + 4f, cx - 15f, hemY - 4f, cx - hipsHalfW - 20f, hemY)
                        lineTo(cx - waistHalfW - 16f, waistY)
                        lineTo(cx - bustHalfW - 14f, bustY)
                        close()
                    }
                    drawPath(coatPath, color = garmentFill)
                    drawPath(coatPath, color = garmentStroke, style = Stroke(width = 1.5f, pathEffect = garmentDashed))

                    val lapel = Path().apply {
                        moveTo(cx - shoulderHalfW * 0.5f, shoulderY)
                        lineTo(cx - 16f, bustY)
                        lineTo(cx, waistY - 10f)
                        lineTo(cx + 16f, bustY)
                        lineTo(cx + shoulderHalfW * 0.5f, shoulderY)
                    }
                    drawPath(lapel, color = garmentStroke.copy(alpha = 0.6f), style = Stroke(width = 1.1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))))
                    drawCircle(garmentStroke.copy(alpha = 0.7f), radius = 2.2f, center = Offset(cx - 7f, waistY - 22f))
                    drawCircle(garmentStroke.copy(alpha = 0.7f), radius = 2.2f, center = Offset(cx + 7f, waistY - 22f))
                }
                cat.contains("skirt") || cat.contains("chân váy") -> {
                    val skirtPath = Path().apply {
                        moveTo(cx - waistHalfW - 5f, waistY)
                        quadraticTo(cx, waistY + 5f, cx + waistHalfW + 5f, waistY)
                        quadraticTo(cx + hipsHalfW + 8f, hipsY, cx + hipsHalfW + 22f, hemY)
                        cubicTo(cx + 15f, hemY + 5f, cx - 15f, hemY - 4f, cx - hipsHalfW - 22f, hemY)
                        quadraticTo(cx - hipsHalfW - 8f, hipsY, cx - waistHalfW - 5f, waistY)
                        close()
                    }
                    drawPath(skirtPath, color = garmentFill)
                    drawPath(skirtPath, color = garmentStroke, style = Stroke(width = 1.5f, pathEffect = garmentDashed))

                    val drapeSkirt = Path().apply {
                        moveTo(cx, waistY + 6f)
                        cubicTo(cx - 6f, hipsY, cx + 3f, (hipsY + hemY) / 2f, cx, hemY)
                    }
                    drawPath(drapeSkirt, color = garmentStroke.copy(alpha = 0.45f), style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))))
                }
                cat.contains("pant") || cat.contains("quần") -> {
                    val pantsPath = Path().apply {
                        moveTo(cx - waistHalfW - 5f, waistY)
                        lineTo(cx + waistHalfW + 5f, waistY)
                        lineTo(cx + hipsHalfW + 6f, hipsY)
                        lineTo(cx + 30f, hemY)
                        lineTo(cx + 8f, hemY)
                        lineTo(cx, crotchY)
                        lineTo(cx - 8f, hemY)
                        lineTo(cx - 30f, hemY)
                        lineTo(cx - hipsHalfW - 6f, hipsY)
                        close()
                    }
                    drawPath(pantsPath, color = garmentFill)
                    drawPath(pantsPath, color = garmentStroke, style = Stroke(width = 1.5f, pathEffect = garmentDashed))
                }
                else -> {
                    val topPath = Path().apply {
                        moveTo(cx - shoulderHalfW * 0.8f, shoulderY)
                        lineTo(cx + shoulderHalfW * 0.8f, shoulderY)
                        lineTo(cx + bustHalfW + 7f, bustY)
                        lineTo(cx + waistHalfW + 7f, hemY)
                        lineTo(cx - waistHalfW - 7f, hemY)
                        lineTo(cx - bustHalfW - 7f, bustY)
                        close()
                    }
                    drawPath(topPath, color = garmentFill)
                    drawPath(topPath, color = garmentStroke, style = Stroke(width = 1.5f, pathEffect = garmentDashed))
                }
            }

            // 7. HEATMAP RADIAL GLOW & CALLOUT POINTER CONNECTORS
            val pointerDash = PathEffect.dashPathEffect(floatArrayOf(3f, 3f))

            bustEval?.let {
                if (!cat.contains("skirt") && !cat.contains("pant")) {
                    drawOval(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                it.status.color.copy(alpha = 0.28f),
                                it.status.color.copy(alpha = 0.14f),
                                it.status.color.copy(alpha = 0.03f),
                                Color.Transparent
                            ),
                            center = Offset(cx, bustY),
                            radius = bustHalfW + 18f
                        ),
                        topLeft = Offset(cx - bustHalfW - 18f, bustY - 14f),
                        size = Size((bustHalfW + 18f) * 2f, 28f)
                    )

                    drawLine(
                        color = it.status.color.copy(alpha = 0.8f),
                        start = Offset(72f, bustY),
                        end = Offset(cx - bustHalfW, bustY),
                        strokeWidth = 1f,
                        pathEffect = pointerDash
                    )
                    drawCircle(it.status.color, radius = 2f, center = Offset(cx - bustHalfW, bustY))
                    drawCircle(it.status.color, radius = 1.8f, center = Offset(72f, bustY))
                }
            }

            waistEval?.let {
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            it.status.color.copy(alpha = 0.28f),
                            it.status.color.copy(alpha = 0.14f),
                            it.status.color.copy(alpha = 0.03f),
                            Color.Transparent
                        ),
                        center = Offset(cx, waistY),
                        radius = waistHalfW + 16f
                    ),
                    topLeft = Offset(cx - waistHalfW - 16f, waistY - 12f),
                    size = Size((waistHalfW + 16f) * 2f, 24f)
                )

                drawLine(
                    color = it.status.color.copy(alpha = 0.8f),
                    start = Offset(cx + waistHalfW, waistY),
                    end = Offset(188f, waistY),
                    strokeWidth = 1f,
                    pathEffect = pointerDash
                )
                drawCircle(it.status.color, radius = 2f, center = Offset(cx + waistHalfW, waistY))
                drawCircle(it.status.color, radius = 1.8f, center = Offset(188f, waistY))
            }

            hipsEval?.let {
                if (garment.measurements.hips != null) {
                    drawOval(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                it.status.color.copy(alpha = 0.28f),
                                it.status.color.copy(alpha = 0.14f),
                                it.status.color.copy(alpha = 0.03f),
                                Color.Transparent
                            ),
                            center = Offset(cx, hipsY),
                            radius = hipsHalfW + 20f
                        ),
                        topLeft = Offset(cx - hipsHalfW - 20f, hipsY - 15f),
                        size = Size((hipsHalfW + 20f) * 2f, 30f)
                    )

                    drawLine(
                        color = it.status.color.copy(alpha = 0.8f),
                        start = Offset(72f, hipsY),
                        end = Offset(cx - hipsHalfW, hipsY),
                        strokeWidth = 1f,
                        pathEffect = pointerDash
                    )
                    drawCircle(it.status.color, radius = 2f, center = Offset(cx - hipsHalfW, hipsY))
                    drawCircle(it.status.color, radius = 1.8f, center = Offset(72f, hipsY))
                }
            }

            // 8. DYNAMIC LENGTH RULER LINE (At hemY)
            drawLine(
                color = Color(0xFFBCA173).copy(alpha = 0.18f),
                start = Offset(10f, hemY),
                end = Offset(250f, hemY),
                strokeWidth = 2.5f
            )
            drawLine(
                color = Color(0xFFBCA173).copy(alpha = 0.75f),
                start = Offset(15f, hemY),
                end = Offset(245f, hemY),
                strokeWidth = 1.1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 5f))
            )
            drawCircle(Color(0xFFBCA173), radius = 2f, center = Offset(15f, hemY))
            drawCircle(Color(0xFFBCA173), radius = 2f, center = Offset(245f, hemY))
        }
    }
}

