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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.SwapOffer
import com.swapshe.app.model.SwapOfferStatus
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.SuccessGreen
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream
import com.swapshe.app.theme.WarningAmber
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton

@Composable
fun SwapRequestsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSwapDetail: (String) -> Unit
) {
    val repository = SwapSheRepository.instance
    val user by repository.currentUser.collectAsState()
    val swapOffers by repository.swapOffers.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Received (Lời mời nhận được), 1: Sent (Đã gửi)

    val receivedOffers = swapOffers.filter { it.recipientId == user.id }
    val sentOffers = swapOffers.filter { it.senderId == user.id }
    val currentList = if (selectedTabIndex == 0) receivedOffers else sentOffers

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
                text = "Hộp thư đổi đồ (Swap Requests)",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = SurfaceWhite,
            contentColor = Primary,
            divider = { HorizontalDivider(color = BorderColor) }
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Lời mời nhận được (${receivedOffers.size})", fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Đã gửi đi (${sentOffers.size})", fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal) }
            )
        }

        if (currentList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Outlined.Inbox, contentDescription = null, tint = TextMuted, modifier = Modifier.size(56.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Chưa có đề nghị trao đổi nào ở đây.", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Các lời mời đổi đồ sẽ xuất hiện tại đây để bạn xem xét.", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(currentList, key = { it.id }) { offer ->
                    SwapOfferCard(
                        offer = offer,
                        isReceived = selectedTabIndex == 0,
                        onAccept = {
                            val success = repository.acceptSwapOffer(offer.id)
                            if (success && offer.swapTransactionId != null) {
                                onNavigateToSwapDetail(offer.swapTransactionId)
                            }
                        },
                        onReject = { repository.rejectSwapOffer(offer.id) },
                        onCancel = { repository.cancelSwapOffer(offer.id) },
                        onViewTransaction = { txId -> onNavigateToSwapDetail(txId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SwapOfferCard(
    offer: SwapOffer,
    isReceived: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onCancel: () -> Unit,
    onViewTransaction: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isReceived) "Từ: ${offer.senderName}" else "Tới: ${offer.recipientName}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                // Status chip
                val (bg, txt, clr) = when (offer.status) {
                    SwapOfferStatus.PENDING -> Triple(WarningAmber.copy(alpha = 0.15f), "Đang chờ duyệt", WarningAmber)
                    SwapOfferStatus.ACCEPTED -> Triple(SuccessGreen.copy(alpha = 0.15f), "Đã chấp thuận", SuccessGreen)
                    SwapOfferStatus.REJECTED -> Triple(ErrorRed.copy(alpha = 0.12f), "Đã từ chối", ErrorRed)
                    SwapOfferStatus.CANCELLED -> Triple(TextMuted.copy(alpha = 0.15f), "Đã hủy", TextMuted)
                    SwapOfferStatus.CONFLICT -> Triple(ErrorRed.copy(alpha = 0.15f), "Xung đột / Hết hàng", ErrorRed)
                }

                Surface(shape = RoundedCornerShape(6.dp), color = bg) {
                    Text(text = txt, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = clr, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pair Items
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Item 1
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = offer.offeredProduct.images.firstOrNull(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(54.dp).clip(RoundedCornerShape(8.dp))
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(text = "Đổi món:", fontSize = 10.sp, color = TextMuted)
                        Text(text = offer.offeredProduct.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary, maxLines = 1)
                        Text(text = "Size ${offer.offeredProduct.size}", fontSize = 10.sp, color = TextSecondary)
                    }
                }

                Icon(imageVector = Icons.Filled.SwapHoriz, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp).padding(horizontal = 4.dp))

                // Item 2
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = offer.requestedProduct.images.firstOrNull(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(54.dp).clip(RoundedCornerShape(8.dp))
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(text = "Lấy món:", fontSize = 10.sp, color = TextMuted)
                        Text(text = offer.requestedProduct.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary, maxLines = 1)
                        Text(text = "Size ${offer.requestedProduct.size}", fontSize = 10.sp, color = TextSecondary)
                    }
                }
            }

            if (offer.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"${offer.note}\"",
                    fontSize = 12.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 10.dp))

            // Action Buttons
            if (offer.status == SwapOfferStatus.PENDING) {
                if (isReceived) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SecondaryButton(
                            text = "Từ chối",
                            onClick = onReject,
                            modifier = Modifier.weight(1f)
                        )
                        PrimaryButton(
                            text = "Đồng ý đổi ✓",
                            onClick = onAccept,
                            modifier = Modifier.weight(1.5f)
                        )
                    }
                } else {
                    SecondaryButton(
                        text = "Hủy yêu cầu",
                        onClick = onCancel,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (offer.status == SwapOfferStatus.ACCEPTED && offer.swapTransactionId != null) {
                PrimaryButton(
                    text = "📦 Xem tiến trình giao nhận 2 chiều",
                    onClick = { onViewTransaction(offer.swapTransactionId) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
