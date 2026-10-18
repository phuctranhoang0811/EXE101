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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SwapHoriz
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.AppNotification
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream

@Composable
fun NotificationCenterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToSwap: () -> Unit,
    onNavigateToProduct: (String) -> Unit = {}
) {
    val repository = SwapSheRepository.instance
    val notifications by repository.notifications.collectAsState()

    val tabs = listOf("Tất cả", "Đơn hàng", "Đổi đồ", "Khuyến mãi & Tìm kiếm")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val filteredList = when (selectedTabIndex) {
        1 -> notifications.filter { it.type == "order" }
        2 -> notifications.filter { it.type == "swap" }
        3 -> notifications.filter { it.type == "price_drop" || it.type == "saved_search_match" }
        else -> notifications
    }

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
                text = "Trung tâm thông báo",
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Đọc tất cả",
                fontSize = 12.sp,
                color = Primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable {
                        notifications.forEach { repository.markNotificationAsRead(it.id) }
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Tabs
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
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTabIndex == index) Primary else TextSecondary
                        )
                    }
                )
            }
        }

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Filled.Notifications, contentDescription = null, tint = TextMuted, modifier = Modifier.size(56.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Không có thông báo nào", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { notif ->
                    NotificationItemCard(
                        notification = notif,
                        onClick = {
                            repository.markNotificationAsRead(notif.id)
                            if (notif.targetProductId != null) {
                                onNavigateToProduct(notif.targetProductId)
                            } else if (notif.type == "order") {
                                onNavigateToOrders()
                            } else if (notif.type == "swap") {
                                onNavigateToSwap()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationItemCard(
    notification: AppNotification,
    onClick: () -> Unit
) {
    val (icon, iconTint) = when (notification.type) {
        "swap" -> Pair(Icons.Filled.SwapHoriz, Primary)
        "price_drop" -> Pair(Icons.Filled.LocalOffer, Primary)
        "saved_search_match" -> Pair(Icons.Filled.AutoAwesome, Primary)
        else -> Pair(Icons.Filled.ShoppingBag, Primary)
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (notification.isRead) SurfaceWhite else PrimarySoft15.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(PrimarySoft15, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }

            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 13.sp,
                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Primary, CircleShape)
                        )
                    }
                }

                Text(
                    text = notification.message,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = notification.timestamp,
                    fontSize = 10.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}
