package com.swapshe.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.DisputeReport
import com.swapshe.app.model.DisputeStatus
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.EscrowBlue
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.theme.WarningAmber
import java.text.DecimalFormat

@Composable
fun AdminDashboardScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDisputeDetail: (String) -> Unit
) {
    val repository = SwapSheRepository.instance
    val disputes by repository.disputes.collectAsState()
    val orders by repository.orders.collectAsState()
    val swapTransactions by repository.swapTransactions.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tranh chấp 48h Escrow", "Kiểm toán Quỹ Escrow", "Giám sát Anti-Scam & eKYC")
    val formatter = remember { DecimalFormat("#,###") }

    val pendingDisputesCount = disputes.count { it.status == DisputeStatus.UNDER_REVIEW || it.status == DisputeStatus.OPEN }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
    ) {
        // Admin Top Bar
        Surface(
            color = Color(0xFF0F172A), // Slate 900 Admin Theme
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF2563EB)
                        ) {
                            Text(
                                text = "TRỌNG TÀI SÀN",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SwapShe Trust & Safety",
                            fontFamily = FontFamily.Serif,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Hệ thống Giám sát Escrow 48h & Phán quyết Tranh chấp C2C",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }

        // Summary KPI Metrics Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceWhite)
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdminKpiCard(
                title = "Quỹ Escrow Bảo Đảm",
                value = "15.420.000đ",
                subtitle = "100% bảo hộ ngân hàng",
                color = Color(0xFF2563EB),
                modifier = Modifier.weight(1f)
            )
            AdminKpiCard(
                title = "Khiếu nại Chờ Xử Lý",
                value = "$pendingDisputesCount vụ",
                subtitle = if (pendingDisputesCount > 0) "Cần phân xử ngay" else "Đã hòa giải xong",
                color = if (pendingDisputesCount > 0) WarningAmber else SuccessGreen,
                modifier = Modifier.weight(1f)
            )
            AdminKpiCard(
                title = "Giao Dịch Đang Bảo Vệ",
                value = "${orders.size + swapTransactions.size} đơn",
                subtitle = "Bảo vệ tiền 48h",
                color = SuccessGreen,
                modifier = Modifier.weight(1f)
            )
        }

        // Tab Selector
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = SurfaceWhite,
            contentColor = Primary,
            edgePadding = 16.dp,
            divider = { HorizontalDivider(color = BorderColor) }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) Primary else TextSecondary
                        )
                    }
                )
            }
        }

        // Tab Contents
        when (selectedTabIndex) {
            0 -> DisputeArbitrationTab(
                disputes = disputes,
                onResolve = { disputeId, refundToBuyer ->
                    repository.resolveDisputeByAdmin(disputeId, refundToBuyer)
                },
                onViewDetail = onNavigateToDisputeDetail
            )
            1 -> EscrowAuditLedgerTab()
            2 -> AntiScamAndEkycTab()
        }
    }
}

@Composable
private fun AdminKpiCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = BackgroundSubtle,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = title, fontSize = 10.sp, color = TextMuted, maxLines = 1)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color, modifier = Modifier.padding(vertical = 2.dp))
            Text(text = subtitle, fontSize = 9.sp, color = TextSecondary, maxLines = 1)
        }
    }
}

@Composable
private fun DisputeArbitrationTab(
    disputes: List<DisputeReport>,
    onResolve: (String, Boolean) -> Unit,
    onViewDetail: (String) -> Unit
) {
    val formatter = remember { DecimalFormat("#,###") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEFF6FF),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Gavel, contentDescription = null, tint = Color(0xFF1D4ED8), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Quy Trình Phán Quyết Trọng Tài 48h:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                        Text(
                            text = "Khi Người mua & Người bán không tìm được tiếng nói chung, Trọng tài SwapShe sẽ thẩm định Video unbox, ảnh chụp lỗi và số tiền Escrow để ra phán quyết cuối cùng.",
                            fontSize = 11.sp,
                            color = Color(0xFF1E40AF),
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        items(disputes, key = { it.id }) { dispute ->
            val isPending = dispute.status == DisputeStatus.UNDER_REVIEW || dispute.status == DisputeStatus.OPEN

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(
                    if (isPending) 1.5.dp else 1.dp,
                    if (isPending) WarningAmber else BorderColor
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Dispute Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isPending) WarningAmber.copy(alpha = 0.15f) else SuccessGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = if (isPending) "⏳ CHỜ TRỌNG TÀI PHÁN QUYẾT" else "✓ ĐÃ GIẢI QUYẾT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPending) WarningAmber else SuccessGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(text = dispute.createdAt, fontSize = 11.sp, color = TextMuted)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = dispute.targetTitle, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Lý do: ${dispute.reason}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Primary, modifier = Modifier.padding(top = 2.dp))
                    Text(text = dispute.description, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))

                    Spacer(modifier = Modifier.height(10.dp))

                    // Participants and Escrow Hold amount
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BackgroundSubtle,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Người mua: ${dispute.reporterName}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                Text(text = "Yêu cầu hoàn: ${formatter.format(dispute.requestedAmount)}đ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary)
                            }
                            if (dispute.sellerOffer != null) {
                                Text(
                                    text = "Đề xuất của người bán: Hoàn ${formatter.format(dispute.sellerOffer.amount)}đ giữ đồ (${dispute.sellerOffer.note})",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    // Evidence Media Indicator
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (dispute.evidenceVideoUrl != null) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PrimarySoft15,
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = null, tint = Primary, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Có Video Unbox mở hộp", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Primary)
                                }
                            }
                        }
                        if (dispute.evidenceImages.isNotEmpty()) {
                            Text(text = "📸 ${dispute.evidenceImages.size} ảnh chụp lỗi", fontSize = 11.sp, color = TextSecondary)
                        }
                    }

                    // Resolution Note if resolved
                    if (dispute.resolutionNote != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SuccessGreen.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚖️ Phán quyết: ${dispute.resolutionNote}",
                                fontSize = 11.sp,
                                color = SuccessGreen,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    // Arbitration Action Buttons (Only when pending)
                    if (isPending) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SuccessGreen,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onResolve(dispute.id, true) }
                            ) {
                                Text(
                                    text = "✓ Hoàn 100% cho Buyer",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = WarningAmber,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onResolve(dispute.id, false) }
                            ) {
                                Text(
                                    text = "✕ Bác đơn & Trả Seller",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Xem toàn bộ hồ sơ tranh chấp →",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary,
                        modifier = Modifier
                            .clickable { onViewDetail(dispute.id) }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EscrowAuditLedgerTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Sổ Cái Ký Quỹ Đơn Hàng & Cọc Swap (Escrow Audit):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        val ledgerEntries = listOf(
            Triple("Đơn #SW-20261014-001 (Đầm Maxi Bloom)", "145.000đ", "Đang tạm giữ Escrow 48h • Buyer khiếu nại lỗi"),
            Triple("Cọc Swap #tx_swap_01 (2 chiều)", "100.000đ", "Mỗi bên cọc 50.000đ bảo đảm • Chờ giao hàng"),
            Triple("Đơn #SW-20261014-002 (Blazer Linen)", "280.000đ", "Đã giải ngân: 266.000đ về Seller • Sàn thu phí 14.000đ (5%)"),
            Triple("Đơn #SW-20261011-008 (Chân váy Midi)", "135.000đ", "Đã giải ngân hoàn tất sau 48h giao thành công")
        )

        items(ledgerEntries) { (title, amount, note) ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = note, fontSize = 10.sp, color = TextSecondary, modifier = Modifier.padding(top = 2.dp))
                    }
                    Text(text = amount, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary)
                }
            }
        }
    }
}

@Composable
private fun AntiScamAndEkycTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Hệ Thống Chống Lừa Đảo She-Trust & eKYC:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Security, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Bộ Lọc Tin Nhắn Ngoài Sàn (Keyword Regex)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    Text(
                        text = "Tự động phát hiện & chặn các từ khóa: \"zalo\", \"chuyển khoản riêng\", \"stk\", \"ck ngoài\", số điện thoại di động...",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SuccessGreen.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "✓ Hệ thống bảo vệ tự động đang BẬT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Danh sách tài khoản đã định danh eKYC:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "• Hoàng My (Buyer 👩): CCCD 079198012345 • She-Trust 95/100", fontSize = 11.sp, color = TextSecondary)
                    Text(text = "• Thùy Linh (Seller 👗): CCCD 079200088991 • She-Trust 99/100 (Queen Closet)", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}
