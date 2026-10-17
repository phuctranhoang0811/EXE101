package com.swapshe.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.BankPayoutAccount
import com.swapshe.app.model.WalletTransaction
import com.swapshe.app.model.WalletTransactionType
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.EscrowBg
import com.swapshe.app.theme.EscrowBlue
import com.swapshe.app.theme.GoldBg
import com.swapshe.app.theme.GoldCrown
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton
import java.text.NumberFormat
import java.util.Locale

@Composable
fun WalletScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExplore: () -> Unit
) {
    val repository = SwapSheRepository.instance
    val wallet by repository.wallet.collectAsState()
    val trustProfile by repository.sheTrustProfile.collectAsState()

    var showWithdrawDialog by remember { mutableStateOf(false) }
    var withdrawAmountText by remember { mutableStateOf("") }
    var selectedBank by remember { mutableStateOf(wallet.bankAccount?.bankName ?: "Vietcombank") }
    var accountNumber by remember { mutableStateOf(wallet.bankAccount?.accountNumber ?: "9988112233") }
    var accountHolder by remember { mutableStateOf(wallet.bankAccount?.accountHolderName ?: "NGUYEN HOANG MY") }

    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)

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
                text = "Ví SwapShe & Dòng tiền Ký quỹ",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Hero Card: Available Balance
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceWhite,
                    shadowElevation = 4.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(SurfaceWhite, WarmCream.copy(alpha = 0.6f))
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.AccountBalance,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = " Số dư khả dụng (Có thể rút)",
                                        fontSize = 13.sp,
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(GoldBg, RoundedCornerShape(100.dp))
                                        .border(1.dp, GoldCrown.copy(alpha = 0.4f), RoundedCornerShape(100.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = trustProfile.badge.title,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldCrown
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${formatter.format(wallet.availableBalance)}đ",
                                fontFamily = FontFamily.Serif,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                PrimaryButton(
                                    text = "🏦 Rút về Ngân hàng",
                                    onClick = { showWithdrawDialog = true },
                                    modifier = Modifier.weight(1.2f)
                                )
                                SecondaryButton(
                                    text = "Mua sắm ngay",
                                    onClick = onNavigateToExplore,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Escrow Hold Balance Card (USP Công Nghệ)
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = EscrowBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, EscrowBlue.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = null,
                                    tint = EscrowBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = " Tiền ký quỹ tạm giữ (Escrow)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EscrowBlue
                                )
                            }
                            Text(
                                text = "${formatter.format(wallet.escrowHoldBalance)}đ",
                                fontFamily = FontFamily.Serif,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = EscrowBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "🛡️ Cơ chế bảo hiểm SwapShe Escrow: Toàn bộ tiền thanh toán từ Buyer được giữ an toàn tại ví trung gian trong 24h–48h. Sau khi đơn giao thành công và không có khiếu nại, hệ thống sẽ tự động trừ 5% phí sàn và chuyển doanh thu vào số dư khả dụng của Seller.",
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = EscrowBlue.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Fee and Perks summary
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Security, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                            Text(
                                text = " Chính sách tài chính minh bạch SwapShe:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "• Phí dịch vụ sàn: Cố định 5% (Người dùng đạt Queen Closet giảm còn 2.5%).", fontSize = 11.sp, color = TextSecondary)
                        Text(text = "• Đổi đồ 1-1 (Direct Swap): Miễn phí hoàn toàn hoa hồng sàn.", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(top = 2.dp))
                        Text(text = "• Rút tiền ngân hàng: Miễn phí rút 24/7, nhận tiền trong 5–15 phút.", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }

            // Transaction History Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lịch sử biến động dòng tiền",
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${wallet.transactions.size} giao dịch",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            // Transaction items
            items(wallet.transactions, key = { it.id }) { tx ->
                TransactionCard(tx = tx, formatter = formatter)
            }
        }
    }

    // Modal Rút tiền về Ngân hàng (Payout Dialog)
    if (showWithdrawDialog) {
        AlertDialog(
            onDismissRequest = { showWithdrawDialog = false },
            title = {
                Text(
                    text = "Rút tiền về Tài khoản Ngân hàng",
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Số dư khả dụng hiện có: ${formatter.format(wallet.availableBalance)}đ",
                        fontSize = 12.sp,
                        color = Primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = withdrawAmountText,
                        onValueChange = { withdrawAmountText = it },
                        label = { Text("Số tiền muốn rút (VNĐ) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Rút tất cả",
                            fontSize = 12.sp,
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                withdrawAmountText = wallet.availableBalance.toString()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = selectedBank,
                        onValueChange = { selectedBank = it },
                        label = { Text("Ngân hàng thụ hưởng") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = { accountNumber = it },
                        label = { Text("Số tài khoản nhận tiền") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = accountHolder,
                        onValueChange = { accountHolder = it },
                        label = { Text("Tên chủ tài khoản (In hoa)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = withdrawAmountText.toLongOrNull() ?: 0L
                        if (amount > 0) {
                            val bank = BankPayoutAccount(selectedBank, accountNumber, accountHolder)
                            val ok = repository.withdrawFunds(amount, bank)
                            if (ok) showWithdrawDialog = false
                        } else {
                            repository.showToast("Vui lòng nhập số tiền rút hợp lệ.")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Xác nhận rút tiền", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdrawDialog = false }) {
                    Text("Hủy", color = TextSecondary)
                }
            },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun TransactionCard(tx: WalletTransaction, formatter: NumberFormat) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val (bgColor, iconColor, icon) = when (tx.type) {
                WalletTransactionType.RELEASE_EARNING, WalletTransactionType.DISPUTE_REFUND ->
                    Triple(SuccessGreen.copy(alpha = 0.15f), SuccessGreen, Icons.Filled.ArrowDownward)
                WalletTransactionType.ESCROW_HOLD ->
                    Triple(EscrowBg, EscrowBlue, Icons.Filled.Lock)
                WalletTransactionType.WITHDRAW_BANK ->
                    Triple(PrimarySoft15, Primary, Icons.Filled.ArrowUpward)
                else ->
                    Triple(BackgroundSubtle, TextSecondary, Icons.Filled.SwapHoriz)
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(bgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = tx.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = tx.description,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = tx.timestamp,
                    fontSize = 10.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            val sign = if (tx.isPositive) "+" else "-"
            val amountColor = if (tx.isPositive) SuccessGreen else TextPrimary
            Text(
                text = "$sign${formatter.format(tx.amount)}đ",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}
