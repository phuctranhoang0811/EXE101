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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.DisputeTargetType
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.EscrowBg
import com.swapshe.app.theme.EscrowBlue
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
fun ReportDisputeScreen(
    targetTypeStr: String,
    targetId: String,
    targetTitle: String,
    onNavigateBack: () -> Unit,
    onNavigateToDisputeDetail: (String) -> Unit = {}
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
        "Người bán gửi sai sản phẩm",
        "Hàng giao chậm quá thời hạn cam kết",
        "Lý do khác"
    )

    var selectedReason by remember { mutableStateOf(reasons.first()) }
    var description by remember { mutableStateOf("Tôi nhận được sản phẩm có vết ố ở tay áo và form rộng hơn số đo trong bài đăng. Đính kèm video unbox chứng minh.") }
    var refundAmountText by remember { mutableStateOf("420000") }
    var hasPhotoProof by remember { mutableStateOf(true) }
    var hasVideoProof by remember { mutableStateOf(true) }
    var isSubmitted by remember { mutableStateOf(false) }
    var submittedDisputeId by remember { mutableStateOf("") }

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
                fontFamily = FontFamily.SansSerif,
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
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(EscrowBlue.copy(alpha = 0.15f), RoundedCornerShape(100.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = null, tint = EscrowBlue, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "🛡️ Đã gửi khiếu nại thành công!",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Text(
                    text = "Tiền đơn hàng đã được ĐÓNG BĂNG trong Ví Escrow SwapShe. Người bán có 24 giờ để đưa ra giải pháp thương lượng trực tiếp.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )

                PrimaryButton(
                    text = "🔍 Theo dõi & Thương lượng ngay →",
                    onClick = { onNavigateToDisputeDetail(submittedDisputeId) }
                )
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    text = "Quay lại danh sách đơn",
                    onClick = onNavigateBack
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Escrow Freeze Notice Banner
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = EscrowBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, EscrowBlue.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Lock, contentDescription = null, tint = EscrowBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Bảo vệ tiền của bạn (Escrow Protection 48h)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EscrowBlue
                            )
                            Text(
                                text = "Ngay khi bấm Gửi, tiền thanh toán sẽ tự động đóng băng không chuyển cho Người bán cho đến khi 2 bên đồng thuận.",
                                fontSize = 11.sp,
                                color = EscrowBlue.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Target Info Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "Đơn hàng khiếu nại:", fontSize = 11.sp, color = TextMuted)
                        Text(text = "$targetTitle (Mã: $targetId)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Loại yêu cầu: Trả hàng & Hoàn tiền Ký Quỹ", fontSize = 12.sp, color = Primary, modifier = Modifier.padding(top = 2.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Chọn lý do khiếu nại:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))

                reasons.forEach { r ->
                    val isSel = selectedReason == r
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSel) PrimarySoft15 else SurfaceWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) Primary else BorderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clickable { selectedReason = r }
                    ) {
                        Text(
                            text = r,
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) Primary else TextPrimary,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Evidence Attachments (Flow D requirement)
                Text(text = "Bằng chứng đối soát bắt buộc:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Video unbox checkbox
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (hasVideoProof) PrimarySoft15 else SurfaceWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (hasVideoProof) Primary else BorderColor),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { hasVideoProof = !hasVideoProof }
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Videocam, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(text = "Video unbox mở hộp", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(text = if (hasVideoProof) "✓ Đã tải lên (24s)" else "Chưa có", fontSize = 10.sp, color = if (hasVideoProof) Primary else TextMuted)
                            }
                        }
                    }

                    // Defect photo proof
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (hasPhotoProof) PrimarySoft15 else SurfaceWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (hasPhotoProof) Primary else BorderColor),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { hasPhotoProof = !hasPhotoProof }
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.PhotoCamera, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(text = "Ảnh chụp vết lỗi", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(text = if (hasPhotoProof) "✓ Đã đính kèm (2 ảnh)" else "Chưa có", fontSize = 10.sp, color = if (hasPhotoProof) Primary else TextMuted)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Requested Refund Amount
                Text(text = "Số tiền yêu cầu hoàn (VNĐ):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = refundAmountText,
                    onValueChange = { refundAmountText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(text = "Mô tả chi tiết sự việc:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Surface(
                color = SurfaceWhite,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp)
                ) {
                    PrimaryButton(
                        text = "Gửi khiếu nại & ĐÓNG BĂNG TIỀN ESCROW 🛡️",
                        onClick = {
                            val newReport = repository.submitDisputeWithMedia(
                                targetType = targetType,
                                targetId = targetId,
                                targetTitle = targetTitle,
                                reason = selectedReason,
                                description = description.ifBlank { "Khiếu nại người dùng gửi lên hệ thống" },
                                evidencePhotos = listOf("https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=600&q=80"),
                                evidenceVideoUrl = "https://example.com/demo_unbox_video.mp4",
                                requestedAmount = refundAmountText.toLongOrNull() ?: 420000L
                            )
                            submittedDisputeId = newReport.id
                            isSubmitted = true
                        }
                    )
                }
            }
        }
    }
}
