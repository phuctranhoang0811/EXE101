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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.EkycDocument
import com.swapshe.app.model.EkycStatus
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.GoldBg
import com.swapshe.app.theme.GoldCrown
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SuccessBg
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton

@Composable
fun EkycScreen(
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val repository = SwapSheRepository.instance
    val trustProfile by repository.sheTrustProfile.collectAsState()

    var currentStep by remember { mutableIntStateOf(1) } // 1: CCCD, 2: Face Scan, 3: Phone OTP
    var fullName by remember { mutableStateOf(trustProfile.document?.fullName ?: "NGUYỄN HOÀNG MY") }
    var idCardNumber by remember { mutableStateOf(trustProfile.document?.idCardNumber ?: "079198012345") }
    var phone by remember { mutableStateOf(trustProfile.document?.phone ?: "0987654321") }
    var hasFrontPhoto by remember { mutableStateOf(true) }
    var hasBackPhoto by remember { mutableStateOf(true) }
    var isFaceScanned by remember { mutableStateOf(true) }
    var otpCode by remember { mutableStateOf("9988") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
    ) {
        // App Bar
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
                text = "Định danh eKYC & She-Trust",
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // She-Trust Score Header Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Điểm tín nhiệm cộng đồng", fontSize = 12.sp, color = TextMuted)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${trustProfile.score}",
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary
                                )
                                Text(
                                    text = "/100 điểm",
                                    fontSize = 14.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(GoldBg, RoundedCornerShape(100.dp))
                                .border(1.dp, GoldCrown, RoundedCornerShape(100.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = trustProfile.badge.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldCrown
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "👑 Đặc quyền Queen Closet: Giảm 50% phí dịch vụ sàn (chỉ còn 2.5%) • Ưu tiên hiển thị bài đăng trên cùng • Miễn phí ký quỹ khi đổi đồ 1-1.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stepper pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("1. Chụp CCCD", "2. Quét mặt", "3. Xác thực OTP").forEachIndexed { index, stepName ->
                    val stepNum = index + 1
                    val isSel = currentStep == stepNum
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSel) Primary else SurfaceWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) Primary else BorderColor),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { currentStep = stepNum }
                    ) {
                        Text(
                            text = stepName,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) Color.White else TextPrimary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            when (currentStep) {
                1 -> {
                    // STEP 1: CCCD 2 mặt
                    Text(text = "Chụp 2 mặt Căn cước công dân (CCCD)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Chụp rõ 4 góc, không chói sáng để hệ thống AI đối soát thông tin.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Mặt trước
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (hasFrontPhoto) SuccessBg else SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (hasFrontPhoto) SuccessGreen else BorderColor),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { hasFrontPhoto = true }
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = if (hasFrontPhoto) Icons.Filled.CheckCircle else Icons.Filled.CameraAlt,
                                    contentDescription = null,
                                    tint = if (hasFrontPhoto) SuccessGreen else Primary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Mặt trước CCCD", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                Text(text = if (hasFrontPhoto) "✓ Đã chụp hợp lệ" else "Nhấn để chụp", fontSize = 10.sp, color = TextSecondary)
                            }
                        }

                        // Mặt sau
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (hasBackPhoto) SuccessBg else SurfaceWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (hasBackPhoto) SuccessGreen else BorderColor),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { hasBackPhoto = true }
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = if (hasBackPhoto) Icons.Filled.CheckCircle else Icons.Filled.CameraAlt,
                                    contentDescription = null,
                                    tint = if (hasBackPhoto) SuccessGreen else Primary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Mặt sau CCCD", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                Text(text = if (hasBackPhoto) "✓ Đã chụp hợp lệ" else "Nhấn để chụp", fontSize = 10.sp, color = TextSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Họ và tên trên CCCD") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = idCardNumber,
                        onValueChange = { idCardNumber = it },
                        label = { Text("Số CCCD (12 chữ số)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                2 -> {
                    // STEP 2: Quét khuôn mặt (Face Recognition)
                    Text(text = "Quét khuôn mặt trực tiếp (Face Scan)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Xác nhận bạn là chủ sở hữu CCCD và ngăn chặn tài khoản ảo giả mạo phái đẹp.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(20.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .background(PrimarySoft15, CircleShape)
                                    .border(2.dp, Primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Face,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Khuôn mặt đã được so khớp 99.8%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary)
                            Text(text = "Dữ liệu sinh trắc học được mã hóa 256-bit an toàn tuyệt đối.", fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }

                3 -> {
                    // STEP 3: Phone OTP
                    Text(text = "Xác thực Số điện thoại chính chủ", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Nhận mã OTP qua SMS để kích hoạt tính năng Đăng bán và Swap 1-1.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Số điện thoại nhận OTP") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { otpCode = it },
                        label = { Text("Mã xác thực OTP (Mô phỏng 9988)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Safety policy note
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BackgroundSubtle,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                    Text(
                        text = " SWAPSHE cam kết bảo mật 100% dữ liệu cá nhân. Định danh eKYC giúp loại trừ 100% gian lận ngoài luồng và xây dựng cộng đồng thời trang phái đẹp văn minh.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // Bottom CTA
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (currentStep > 1) {
                    SecondaryButton(
                        text = "Quay lại",
                        onClick = { currentStep -= 1 },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (currentStep < 3) {
                    PrimaryButton(
                        text = "Tiếp theo →",
                        onClick = { currentStep += 1 },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    PrimaryButton(
                        text = "Hoàn tất định danh eKYC ✨",
                        onClick = {
                            val doc = EkycDocument(fullName, idCardNumber, "11/08/1998", phone)
                            repository.submitEkyc(doc)
                            onSuccess()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
