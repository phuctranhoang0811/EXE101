package com.swapshe.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swapshe.app.R
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SecondaryButton
import com.swapshe.app.ui.components.TextLinkButton
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream

@Composable
fun WelcomeScreen(
    onNavigateToAuth: (isRegister: Boolean) -> Unit,
    onNavigateToGuestHome: () -> Unit
) {
    val repository = SwapSheRepository.instance
    val contentAlpha = remember { Animatable(0f) }
    val contentOffsetY = remember { Animatable(32f) }

    LaunchedEffect(Unit) {
        launch {
            contentAlpha.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))
        }
        launch {
            contentOffsetY.animateTo(0f, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow))
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .graphicsLayer {
                alpha = contentAlpha.value
                translationY = contentOffsetY.value
            }
    ) {
        // Top Guest action
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            TextLinkButton(
                text = "Khám phá dạng Khách →",
                onClick = {
                    repository.setGuestMode()
                    onNavigateToGuestHome()
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Branding with Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "SwapShe Logo",
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            )
            Column {
                Text(
                    text = "SWAPSHE",
                    fontFamily = FontFamily.Serif,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "PRE-LOVED FASHION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    letterSpacing = 2.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Lookbook Image
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .aspectRatio(4f / 5f)
                .clip(RoundedCornerShape(20.dp))
                .background(BackgroundSubtle)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=800&q=80",
                contentDescription = "Vintage Lookbook",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Slogan & Heading
        Text(
            text = "Loved Before, Styled Again.",
            fontFamily = FontFamily.Serif,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Nền tảng mua bán & trao đổi thời trang vintage chọn lọc. Bền vững, tinh tế và riêng biệt.",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // CTAs
        PrimaryButton(
            text = "Đăng ký tài khoản",
            onClick = { onNavigateToAuth(true) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SecondaryButton(
            text = "Đăng nhập",
            onClick = { onNavigateToAuth(false) }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
