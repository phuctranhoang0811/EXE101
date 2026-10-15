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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream

@Composable
fun ProfileScreen(
    onNavigateToOrders: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToStyleOnboarding: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToCloset: () -> Unit = {},
    onNavigateToSwapRequests: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToSellerOrders: () -> Unit = {}
) {
    val repository = SwapSheRepository.instance
    val user by repository.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Hồ sơ của tôi (My Profile)",
            fontFamily = FontFamily.Serif,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Header Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (user.avatarUrl.isNotBlank()) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = user.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(PrimarySoft15, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = null, tint = Primary, modifier = Modifier.size(32.dp))
                    }
                }

                Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                    Text(
                        text = if (user.isGuest) "Guest Explorer" else user.name,
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (user.isGuest) "Chưa đăng nhập tài khoản" else user.email,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Guest Prompt to log in
        if (user.isGuest) {
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Đăng nhập để đồng bộ tủ đồ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Lưu lại danh sách yêu thích, theo dõi đơn hàng và trao đổi đồ secondhand tiện lợi.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )
                    PrimaryButton(
                        text = "Đăng nhập / Đăng ký",
                        onClick = onNavigateToAuth
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Preference Summary
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Gu thời trang & Size cá nhân", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        text = "Chỉnh sửa",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary,
                        modifier = Modifier.clickable { onNavigateToStyleOnboarding() }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Phong cách:", fontSize = 12.sp, color = TextMuted)
                    Text(text = user.stylePreferences.joinToString(", ").ifEmpty { "Chưa chọn" }, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Size chuẩn:", fontSize = 12.sp, color = TextMuted)
                    Text(text = user.sizePreference ?: "S", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Màu sắc:", fontSize = 12.sp, color = TextMuted)
                    Text(text = user.colorPreferences.joinToString(", ").ifEmpty { "Chưa chọn" }, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Menu items
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Filled.ShoppingBag,
                    title = "Đơn mua của tôi (My Orders)",
                    onClick = onNavigateToOrders
                )
                HorizontalDivider(color = BorderColor)
                ProfileMenuItem(
                    icon = Icons.Filled.Favorite,
                    title = "Món đồ yêu thích (Wishlist)",
                    onClick = onNavigateToWishlist
                )
                HorizontalDivider(color = BorderColor)
                ProfileMenuItem(
                    icon = Icons.Outlined.Checkroom,
                    title = "Tủ đồ của tôi (My Closet — F22)",
                    onClick = onNavigateToCloset
                )
                HorizontalDivider(color = BorderColor)
                ProfileMenuItem(
                    icon = Icons.Filled.ShoppingBag,
                    title = "Đơn bán của tôi (Seller Orders — F24)",
                    onClick = onNavigateToSellerOrders
                )
                HorizontalDivider(color = BorderColor)
                ProfileMenuItem(
                    icon = Icons.Filled.SwapHoriz,
                    title = "Yêu cầu đổi đồ (Swap Requests — F28)",
                    onClick = onNavigateToSwapRequests
                )
                HorizontalDivider(color = BorderColor)
                ProfileMenuItem(
                    icon = Icons.Filled.LocationOn,
                    title = "Thông báo hệ thống (Notifications — F31)",
                    onClick = onNavigateToNotifications
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Account Switcher for Demo
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = SurfaceWhite,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Chế độ trải nghiệm (Demo Switcher)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SecondaryButton(
                        text = if (user.isGuest) "✓ Đang là Guest" else "Chuyển sang Guest",
                        onClick = { repository.setGuestMode() },
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        text = if (!user.isGuest) "✓ Đang là User" else "Chuyển sang User",
                        onClick = { repository.login("hoangmy@example.com", "Hoàng My") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (!user.isGuest) {
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SurfaceWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { repository.logout() }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Outlined.ExitToApp, contentDescription = null, tint = ErrorRed)
                    Text(
                        text = "Đăng xuất tài khoản",
                        color = ErrorRed,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.padding(start = 12.dp).weight(1f)
        )
        Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
    }
}
