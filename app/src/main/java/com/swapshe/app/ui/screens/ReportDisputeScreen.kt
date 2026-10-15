package com.swapshe.app.ui.screens

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.DisputeTargetType
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.ui.components.PrimaryButton

@Composable
fun ReportDisputeScreen(
    targetTypeStr: String,
    targetId: String,
    targetTitle: String,
    onNavigateBack: () -> Unit
) {
    val repository = SwapSheRepository.instance

    val targetType = when (targetTypeStr.uppercase()) {
        "ORDER" -> DisputeTargetType.ORDER
        "SWAP" -> DisputeTargetType.SWAP
        "USER" -> DisputeTargetType.USER
        else -> DisputeTargetType.LISTING
    }

    val reasons = listOf(
        "Món đồ không đúng mô tả / sai số đo",
        "Hàng bị rách / dính bẩn không thông báo trước",
        "Người bán chậm gửi / không liên lạc được",
        "Nghi vấn hàng giả / nội dung không phù hợp",
        "Lý do khác"
    )

    var selectedReason by remember { mutableStateOf(reasons.first()) }
    var description by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

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
                text = "Báo cáo sự cố & Khiếu nại",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        if (isSubmitted) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✓ Đã gửi khiếu nại thành công!",
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Text(
                    text = "Bộ phận Trust & Safety của SWAPSHE đã tiếp nhận và tạm dừng tự động hoàn tất giao dịch. Chúng tôi sẽ phản hồi trong 24 giờ làm việc.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp, bottom = 24.dp)
                )
                PrimaryButton(text = "Quay lại", onClick = onNavigateBack)
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Target Info Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "Đối tượng khiếu nại:", fontSize = 11.sp, color = TextMuted)
                        Text(text = "$targetTitle (Mã: $targetId)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Loại: ${targetType.label}", fontSize = 12.sp, color = Primary, modifier = Modifier.padding(top = 2.dp))
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(text = "Chọn lý do khiếu nại:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                reasons.forEach { r ->
                    val isSel = selectedReason == r
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSel) PrimarySoft15 else SurfaceWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) Primary else BorderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedReason = r }
                    ) {
                        Text(
                            text = r,
                            fontSize = 13.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) Primary else TextPrimary,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(text = "Mô tả chi tiết sự việc:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Hãy nêu rõ tình trạng thực tế bạn gặp phải...") },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PrimarySoft15,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🛡️ Chính sách bảo vệ người dùng: Sau khi gửi khiếu nại, các khoản thanh toán ký quỹ giả lập hoặc trạng thái giao dịch sẽ bị đóng băng cho đến khi có kết luận.",
                        fontSize = 11.sp,
                        color = Primary,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Surface(
                color = SurfaceWhite,
                shadowElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    PrimaryButton(
                        text = "Gửi khiếu nại tới CSKH",
                        onClick = {
                            repository.submitDispute(
                                targetType = targetType,
                                targetId = targetId,
                                targetTitle = targetTitle,
                                reason = selectedReason,
                                description = description.ifBlank { "Khiếu nại người dùng gửi lên hệ thống" }
                            )
                            isSubmitted = true
                        }
                    )
                }
            }
        }
    }
}
