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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.DisputeOfferType
import com.swapshe.app.model.DisputeStatus
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.ErrorBg
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.EscrowBg
import com.swapshe.app.theme.EscrowBlue
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SuccessBg
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.theme.WarningAmber
import com.swapshe.app.theme.WarningBg
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DisputeDetailScreen(
    disputeId: String,
    onNavigateBack: () -> Unit
) {
    val repository = SwapSheRepository.instance
    val disputes by repository.disputes.collectAsState()
    val dispute = disputes.find { it.id == disputeId || it.targetId == disputeId } ?: disputes.firstOrNull()

    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)

    if (dispute == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmCream)
                .statusBarsPadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Không tìm thấy hồ sơ khiếu nại này.", color = TextPrimary)
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
                text = "Hồ sơ khiếu nại #${dispute.id.takeLast(6)}",
                fontFamily = FontFamily.Serif,
                fontSize = 17.sp,
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
            // Escrow Frozen Alert Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = EscrowBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, EscrowBlue.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(EscrowBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Filled.Lock, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(20.dp))
                    }
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(
                            text = "Tiền trong Ví Escrow đang ĐÓNG BĂNG",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = EscrowBlue
                        )
                        Text(
                            text = "Hệ thống đã tự động dừng giải ngân cho Seller cho đến khi khiếu nại được giải quyết thỏa đáng.",
                            fontSize = 11.sp,
                            color = EscrowBlue.copy(alpha = 0.85f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 24h Countdown & Status Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Timer, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                            Text(
                                text = " Thời gian thương lượng 2 bên:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(WarningBg, RoundedCornerShape(100.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (dispute.status == DisputeStatus.RESOLVED) "Đã chốt" else "Còn 21h 35m",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarningAmber
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Đơn khiếu nại: ${dispute.targetTitle}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Lý do: ${dispute.reason}", fontSize = 12.sp, color = Primary, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 2.dp))
                    Text(text = "Mô tả sự việc: ${dispute.description}", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Evidence Photos & Video Unbox Section
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Bằng chứng người mua cung cấp:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Proof image 1
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=600&q=80",
                            contentDescription = "Ảnh lỗi 1",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
                        )

                        // Video unbox preview card
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(BackgroundSubtle)
                                .border(1.dp, Primary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .clickable {
                                    repository.showToast("Đang phát Video Unbox mở hộp của Buyer (24 giây)...")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Filled.PlayCircleFilled, contentDescription = null, tint = Primary, modifier = Modifier.size(32.dp))
                                Text(text = "Video unbox", fontSize = 10.sp, color = Primary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Text(text = "✓ Video mở hộp và hình ảnh đã được mã hoá đối soát với bưu tá.", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(top = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Direct Negotiation Box (Thương lượng Buyer - Seller)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, Primary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Handshake, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                        Text(text = " Thương lượng trực tiếp giữa hai bên:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (dispute.sellerOffer != null) {
                        // Seller has made an offer
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimarySoft15,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Phương án từ Người bán:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary
                                )
                                Text(
                                    text = "${dispute.sellerOffer.type.label} (${formatter.format(dispute.sellerOffer.amount ?: 100000L)}đ)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Text(
                                    text = "\"${dispute.sellerOffer.note}\"",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        if (dispute.status != DisputeStatus.RESOLVED) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                PrimaryButton(
                                    text = "✓ Đồng ý nhận tiền hoàn",
                                    onClick = { repository.buyerAcceptDisputeOffer(dispute.id) },
                                    modifier = Modifier.weight(1f)
                                )
                                SecondaryButton(
                                    text = "Nhờ Admin xử",
                                    onClick = { repository.escalateDisputeToAdmin(dispute.id) },
                                    modifier = Modifier.weight(0.9f)
                                )
                            }
                        }
                    } else {
                        // Flow D: 3 Seller Options (Hoàn 1 phần / Đồng ý trả 100% / Từ chối)
                        Text(
                            text = "3 phương án xử lý của Người bán trong 24h (Flow D):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            SecondaryButton(
                                text = "Hoàn 30%",
                                onClick = {
                                    repository.sellerRespondDispute(
                                        dispute.id,
                                        DisputeOfferType.PARTIAL_REFUND,
                                        120000L,
                                        "Mình xin hoàn 120.000đ bồi thường và bạn giữ lại áo nhé!"
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                            SecondaryButton(
                                text = "Nhận lại 100%",
                                onClick = {
                                    repository.sellerRespondDispute(
                                        dispute.id,
                                        DisputeOfferType.FULL_RETURN_REFUND,
                                        420000L,
                                        "Đồng ý nhận lại hàng và hoàn 100% qua Escrow."
                                    )
                                },
                                modifier = Modifier.weight(1.2f)
                            )
                            SecondaryButton(
                                text = "Từ chối ✕",
                                onClick = {
                                    repository.sellerRejectDispute(dispute.id)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // SwapShe Admin Verdict Section
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Gavel, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                        Text(text = " Phán quyết của SwapShe Admin:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (dispute.adminVerdict != null) {
                        Text(text = dispute.adminVerdict, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
                        if (dispute.returnTrackingCode != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SuccessBg,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Filled.LocalShipping, contentDescription = null, tint = SuccessGreen)
                                    Column(modifier = Modifier.padding(start = 8.dp)) {
                                        Text(text = "Mã vận đơn hoàn hàng (GHN): ${dispute.returnTrackingCode}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                        Text(text = "Bưu tá sẽ tới lấy hàng miễn phí tại nhà người mua.", fontSize = 10.sp, color = TextSecondary)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            if (!dispute.isSellerReturnConfirmed) {
                                PrimaryButton(
                                    text = "📦 Seller bấm: Xác nhận đã nhận lại hàng trả",
                                    onClick = {
                                        repository.sellerConfirmReturnReceived(dispute.id)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = PrimarySoft15,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "✓ Hoàn tất giải quyết: Tiền Escrow đã được giải ngân hoàn về ví người mua.",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Primary,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Admin sẽ đối chiếu video unbox và hình ảnh đóng gói của 2 bên nếu cuộc thương lượng không đạt thỏa thuận.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        SecondaryButton(
                            text = "Mô phỏng Admin phân xử chấp thuận trả hàng",
                            onClick = { repository.adminResolveDispute(dispute.id, returnApproved = true) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom Back Button
        Surface(
            color = SurfaceWhite,
            shadowElevation = 8.dp,
            modifier = Modifier.navigationBarsPadding()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                PrimaryButton(text = "Quay lại danh sách", onClick = onNavigateBack)
            }
        }
    }
}
