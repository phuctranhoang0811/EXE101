package com.swapshe.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.OrderStatus
import com.swapshe.app.model.UserRole
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
import com.swapshe.app.theme.WarningAmber
import java.text.DecimalFormat

@Composable
fun FloatingDemoControlBar(
    onNavigateToHome: () -> Unit,
    onNavigateToSellerCloset: () -> Unit,
    onNavigateToDisputeDetail: (String) -> Unit,
    onNavigateToAdminDashboard: () -> Unit = {}
) {
    val repository = SwapSheRepository.instance
    val currentRole by repository.currentRole.collectAsState()
    val currentUser by repository.currentUser.collectAsState()
    val orders by repository.orders.collectAsState()
    val disputes by repository.disputes.collectAsState()

    var isExpanded by remember { mutableStateOf(false) }

    val latestOrder = orders.firstOrNull()
    val latestDispute = disputes.firstOrNull()
    val formatter = remember { DecimalFormat("#,###") }

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = 76.dp, end = 16.dp, start = 16.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Expanded Simulator Panel
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceWhite,
                shadowElevation = 14.dp,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Primary.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🛠️ DEMO SIMULATOR & ROLE SWITCHER",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(BackgroundSubtle, CircleShape)
                                .clickable { isExpanded = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = "Công cụ chuyển vai trò & tua nhanh đơn hàng để demo mượt mà cho giáo viên",
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                    )

                    // Active Persona Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BackgroundSubtle,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = currentUser.avatarUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                            )
                            Column(modifier = Modifier.padding(start = 10.dp).weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = currentUser.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SuccessGreen.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "eKYC: ${currentUser.trustProfile.score}/100",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SuccessGreen,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Số dư: ${formatter.format(currentUser.wallet.availableBalance)}đ • Giữ Escrow: ${formatter.format(currentUser.wallet.escrowHoldBalance)}đ",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 1. ROLE SWITCHER (Buyer <-> Seller <-> Admin)
                    Text(
                        text = "1. Chọn danh tính tương tác (Role Switcher):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Role 1: Buyer
                        RoleSelectCard(
                            role = UserRole.BUYER,
                            title = "Người mua (Hoàng My 👩)",
                            subtitle = "Săn đồ vintage, nạp ví, đặt cọc Escrow & mở khiếu nại 48h",
                            badgeText = "BUYER",
                            isSelected = currentRole == UserRole.BUYER,
                            activeColor = Color(0xFF6B21A8),
                            onClick = {
                                repository.setRole(UserRole.BUYER)
                                onNavigateToHome()
                            }
                        )

                        // Role 2: Seller
                        RoleSelectCard(
                            role = UserRole.SELLER,
                            title = "Người bán (Thùy Linh - Linh's Closet 👗)",
                            subtitle = "Quản lý tủ đồ, chuẩn bị giao GHN, duyệt Swap & nhận hoàn tiền",
                            badgeText = "SELLER",
                            isSelected = currentRole == UserRole.SELLER,
                            activeColor = Color(0xFFC2185B),
                            onClick = {
                                repository.setRole(UserRole.SELLER)
                                onNavigateToSellerCloset()
                            }
                        )

                        // Role 3: Admin
                        RoleSelectCard(
                            role = UserRole.ADMIN,
                            title = "Trọng tài SwapShe (Admin ⚖️)",
                            subtitle = "Thẩm định video unbox, phân xử tranh chấp Escrow 48h & duyệt hoàn tiền",
                            badgeText = "ADMIN",
                            isSelected = currentRole == UserRole.ADMIN,
                            activeColor = Color(0xFF0D47A1),
                            onClick = {
                                repository.setRole(UserRole.ADMIN)
                                onNavigateToAdminDashboard()
                            }
                        )
                    }

                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(vertical = 12.dp))

                    // 2. ORDER FAST-FORWARD STEPPER
                    Text(
                        text = "2. Tua nhanh tiến trình đơn hàng #${latestOrder?.internalCode ?: "MẪU"}:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    if (latestOrder != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val statuses = listOf(
                                "Chuẩn bị" to OrderStatus.PREPARING,
                                "Giao bưu tá" to OrderStatus.SHIPPED,
                                "Đã giao (48h)" to OrderStatus.DELIVERED,
                                "Hoàn tất" to OrderStatus.COMPLETED
                            )

                            statuses.forEach { (lbl, st) ->
                                val isCurrent = latestOrder.status == st
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isCurrent) WarningAmber.copy(alpha = 0.2f) else BackgroundSubtle,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isCurrent) WarningAmber else BorderColor),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            repository.updateSellerOrderStatus(latestOrder.id, st)
                                        }
                                ) {
                                    Text(
                                        text = lbl,
                                        fontSize = 10.sp,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isCurrent) WarningAmber else TextPrimary,
                                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Chưa có đơn hàng nào. Hãy mua 1 món đồ để kích hoạt!",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // Floating Pill Trigger Button (Always visible on screen)
        Surface(
            shape = RoundedCornerShape(100.dp),
            color = when (currentRole) {
                UserRole.BUYER -> Color(0xFF6B21A8)
                UserRole.SELLER -> Color(0xFFC2185B)
                UserRole.ADMIN -> Color(0xFF0D47A1)
            },
            shadowElevation = 8.dp,
            modifier = Modifier.clickable { isExpanded = !isExpanded }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = currentRole.icon, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = currentUser.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isExpanded) "Bấm để thu gọn ▲" else "Bấm để đổi vai trò & tua đơn ▾",
                        fontSize = 9.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RoleSelectCard(
    role: UserRole,
    title: String,
    subtitle: String,
    badgeText: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) activeColor.copy(alpha = 0.12f) else SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isSelected) activeColor else BorderColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = role.icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) activeColor else TextPrimary
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isSelected) activeColor else BackgroundSubtle
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else TextMuted,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = TextSecondary,
                    lineHeight = 13.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
