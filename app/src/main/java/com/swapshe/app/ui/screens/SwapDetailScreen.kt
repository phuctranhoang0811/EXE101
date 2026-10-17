package com.swapshe.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
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
import com.swapshe.app.model.SwapTransaction
import com.swapshe.app.model.SwapTransactionStatus
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
import com.swapshe.app.theme.WarningAmber
import com.swapshe.app.ui.components.OrderTimelineView
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton

@Composable
fun SwapDetailScreen(
    transactionId: String,
    onNavigateBack: () -> Unit,
    onNavigateToReport: (String, String) -> Unit
) {
    val repository = SwapSheRepository.instance
    val user by repository.currentUser.collectAsState()
    val swapTransactions by repository.swapTransactions.collectAsState()

    val transaction = swapTransactions.find { it.id == transactionId }

    if (transaction == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmCream)
                .statusBarsPadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Không tìm thấy giao dịch đổi đồ này.", color = TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(text = "Quay lại", onClick = onNavigateBack)
        }
        return
    }

    var simulatedSideIsA by remember(transaction.id, user.id) {
        mutableStateOf(user.id != transaction.userBId)
    }
    val isUserA = simulatedSideIsA
    val myItem = if (isUserA) transaction.productA else transaction.productB
    val theirItem = if (isUserA) transaction.productB else transaction.productA
    val myShipment = if (isUserA) transaction.shipmentAtoB else transaction.shipmentBtoA
    val theirShipment = if (isUserA) transaction.shipmentBtoA else transaction.shipmentAtoB
    val partnerName = if (isUserA) transaction.userBName else transaction.userAName
    val myRoleName = if (isUserA) transaction.userAName else transaction.userBName

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
                text = "Tiến trình đổi đồ 2 chiều",
                fontFamily = FontFamily.Serif,
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
            // Dynamic Perspective Switcher (Người mua A ⇄ Người bán B)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PrimarySoft15,
                border = androidx.compose.foundation.BorderStroke(1.dp, Primary.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Đang xem giao diện với vai trò:",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = if (isUserA) "👩 $myRoleName (Bên A)" else "👗 $myRoleName (Bên B)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Primary,
                        modifier = Modifier.clickable { simulatedSideIsA = !simulatedSideIsA }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Filled.SwapHoriz, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Đổi góc nhìn A ⇄ B",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            // Status Header
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (transaction.status == SwapTransactionStatus.COMPLETED) SuccessGreen.copy(alpha = 0.15f) else PrimarySoft15,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (transaction.status == SwapTransactionStatus.COMPLETED)
                            "🎉 Giao dịch trao đổi đã hoàn tất!"
                        else
                            "Tiến trình: ${transaction.status.label}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (transaction.status == SwapTransactionStatus.COMPLETED) SuccessGreen else Primary
                    )
                    Text(
                        text = "Trao đổi với $partnerName • Bắt đầu lúc ${transaction.createdAt}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Escrow Guarantee Card (Flow C: 50.000đ cọc mỗi bên)
            val myEscrowPaid = if (isUserA) transaction.isEscrowDepositedA else transaction.isEscrowDepositedB
            val theirEscrowPaid = if (isUserA) transaction.isEscrowDepositedB else transaction.isEscrowDepositedA

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (myEscrowPaid && theirEscrowPaid) SuccessGreen.copy(alpha = 0.5f) else BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🛡️ Cọc bảo đảm Escrow", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.size(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (myEscrowPaid && theirEscrowPaid) SuccessGreen.copy(alpha = 0.15f) else WarningAmber.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = if (myEscrowPaid && theirEscrowPaid) "Đã bảo hiểm" else "Chờ ký quỹ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (myEscrowPaid && theirEscrowPaid) SuccessGreen else WarningAmber,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(text = "50.000đ / bên", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Primary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Để chống bom hàng và tráo đồ, mỗi bên ký quỹ 50.000đ qua Escrow. Khoản cọc sẽ tự động hoàn lại ví 100% ngay khi hai bên xác nhận nhận đủ đồ.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BorderColor.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Bạn: " + if (myEscrowPaid) "✓ Đã cọc 50.000đ" else "Chưa cọc",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (myEscrowPaid) SuccessGreen else TextMuted
                        )
                        Text(
                            text = "$partnerName: " + if (theirEscrowPaid) "✓ Đã cọc 50.000đ" else "Chưa cọc",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (theirEscrowPaid) SuccessGreen else TextMuted
                        )
                    }

                    if (!myEscrowPaid) {
                        Spacer(modifier = Modifier.height(10.dp))
                        PrimaryButton(
                            text = "🛡️ Ký quỹ cọc Escrow 50.000đ ngay",
                            onClick = { repository.depositSwapEscrow(transaction.id, isUserA) }
                        )
                    } else if (!theirEscrowPaid) {
                        Spacer(modifier = Modifier.height(8.dp))
                        SecondaryButton(
                            text = "⚡ Demo: Mô phỏng $partnerName đã cọc",
                            onClick = { repository.depositSwapEscrow(transaction.id, !isUserA) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Two-Way Shipment Tracker Cards
            Text(text = "Hành trình vận chuyển 2 chiều:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            // Leg 1: You Send
            Surface(
                shape = RoundedCornerShape(14.dp),
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
                        Text(text = "📤 Hướng 1: Bạn gửi cho $partnerName", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            text = if (myShipment.isDispatched) "Đã gửi hàng" else "Chờ gửi hàng",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (myShipment.isDispatched) SuccessGreen else WarningAmber
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = myItem.images.firstOrNull(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp))
                        )
                        Column(modifier = Modifier.padding(start = 10.dp)) {
                            Text(text = myItem.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Text(text = "Mã vận đơn: ${myShipment.trackingCodeDemo}", fontSize = 11.sp, color = TextMuted)
                        }
                    }

                    if (!myShipment.isDispatched) {
                        Spacer(modifier = Modifier.height(10.dp))
                        PrimaryButton(
                            text = "🚚 Xác nhận tôi đã gửi kiện hàng này",
                            onClick = { repository.dispatchSwapShipment(transaction.id, isUserA) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Leg 2: Partner Sends to You
            Surface(
                shape = RoundedCornerShape(14.dp),
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
                        Text(text = "📥 Hướng 2: $partnerName gửi cho bạn", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            text = if (theirShipment.isReceivedConfirmed) "Đã nhận được" else if (theirShipment.isDispatched) "Đang giao đến bạn" else "Chờ đối tác gửi",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (theirShipment.isReceivedConfirmed) SuccessGreen else Primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = theirItem.images.firstOrNull(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp))
                        )
                        Column(modifier = Modifier.padding(start = 10.dp)) {
                            Text(text = theirItem.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Text(text = "Mã vận đơn: ${theirShipment.trackingCodeDemo}", fontSize = 11.sp, color = TextMuted)
                        }
                    }

                    if (!theirShipment.isDispatched) {
                        Spacer(modifier = Modifier.height(8.dp))
                        SecondaryButton(
                            text = "Mô phỏng đối tác đã gửi hàng",
                            onClick = { repository.dispatchSwapShipment(transaction.id, !isUserA) }
                        )
                    } else if (!theirShipment.isReceivedConfirmed) {
                        Spacer(modifier = Modifier.height(10.dp))
                        PrimaryButton(
                            text = "✓ Tôi đã nhận được món đồ & kiểm tra ưng ý",
                            onClick = { repository.confirmSwapReceived(transaction.id, isUserA) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Timeline
            Text(text = "Lịch sử sự kiện:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            OrderTimelineView(events = transaction.timeline)

            Spacer(modifier = Modifier.height(16.dp))

            // Report button
            SecondaryButton(
                text = "⚠️ Báo cáo sự cố về lần đổi này",
                onClick = { onNavigateToReport("SWAP", transaction.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
