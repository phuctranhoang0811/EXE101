package com.swapshe.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.ui.components.PrimaryButton
import com.swapshe.app.ui.components.SwapsheTextField
import com.swapshe.app.ui.components.TextLinkButton
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream

@Composable
fun AuthScreen(
    initialIsRegister: Boolean = false,
    onAuthSuccess: (isNewUser: Boolean, hasPendingCheckout: Boolean) -> Unit,
    onBack: () -> Unit
) {
    val repository = SwapSheRepository.instance
    var isRegister by remember { mutableStateOf(initialIsRegister) }
    var email by remember { mutableStateOf("hoangmy@example.com") }
    var fullName by remember { mutableStateOf("Hoàng My") }
    var password by remember { mutableStateOf("swapshe123") }
    var confirmPassword by remember { mutableStateOf("swapshe123") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val pendingCheckoutProduct by repository.pendingCheckoutProduct.collectAsState()

    fun validate(): Boolean {
        var isValid = true
        if (email.isBlank() || !email.contains("@")) {
            emailError = "Vui lòng nhập địa chỉ email hợp lệ"
            isValid = false
        } else {
            emailError = null
        }

        if (password.length < 6) {
            passwordError = "Mật khẩu phải có ít nhất 6 ký tự"
            isValid = false
        } else {
            passwordError = null
        }

        if (isRegister) {
            if (confirmPassword != password) {
                confirmPasswordError = "Mật khẩu xác nhận không khớp"
                isValid = false
            } else {
                confirmPasswordError = null
            }
        }
        return isValid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Back row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextLinkButton(
                text = "← Back",
                onClick = onBack
            )
            Text(
                text = "SWAPSHE",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            Spacer(modifier = Modifier.padding(24.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Title
        Text(
            text = if (isRegister) "Tạo tài khoản" else "Chào mừng trở lại",
            fontFamily = FontFamily.Serif,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Text(
            text = "Một tài khoản duy nhất để Mua sắm, Bán thanh lý & Trao đổi thời trang secondhand.",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
        )

        // Segmented Switcher
        val loginBg by animateColorAsState(if (!isRegister) SurfaceWhite else Color.Transparent, tween(200), label = "loginBg")
        val loginText by animateColorAsState(if (!isRegister) Primary else TextSecondary, tween(200), label = "loginText")
        val registerBg by animateColorAsState(if (isRegister) SurfaceWhite else Color.Transparent, tween(200), label = "registerBg")
        val registerText by animateColorAsState(if (isRegister) Primary else TextSecondary, tween(200), label = "registerText")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(BorderColor)
                .padding(4.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(loginBg)
                        .clickable { isRegister = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Đăng nhập",
                        fontSize = 14.sp,
                        fontWeight = if (!isRegister) FontWeight.Bold else FontWeight.Medium,
                        color = loginText
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(registerBg)
                        .clickable { isRegister = true }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Đăng ký",
                        fontSize = 14.sp,
                        fontWeight = if (isRegister) FontWeight.Bold else FontWeight.Medium,
                        color = registerText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Inputs
        AnimatedVisibility(
            visible = isRegister,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                SwapsheTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Họ và tên",
                    placeholder = "Ví dụ: Nguyễn Hoàng My"
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        SwapsheTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = "Email",
            placeholder = "name@example.com",
            keyboardType = KeyboardType.Email,
            isError = emailError != null,
            errorMessage = emailError
        )

        Spacer(modifier = Modifier.height(14.dp))

        SwapsheTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = "Mật khẩu",
            placeholder = "••••••••",
            isPassword = true,
            isError = passwordError != null,
            errorMessage = passwordError
        )

        AnimatedVisibility(
            visible = isRegister,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.height(14.dp))
                SwapsheTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null
                    },
                    label = "Xác nhận mật khẩu",
                    placeholder = "••••••••",
                    isPassword = true,
                    isError = confirmPasswordError != null,
                    errorMessage = confirmPasswordError
                )
            }
        }

        if (!isRegister) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextLinkButton(
                    text = "Quên mật khẩu?",
                    onClick = {
                        repository.showToast("Mã xác thực đặt lại mật khẩu đã gửi qua email demo.")
                    },
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        PrimaryButton(
            text = if (isRegister) "Tạo tài khoản & Bắt đầu" else "Đăng nhập",
            onClick = {
                if (validate()) {
                    if (isRegister) {
                        repository.register(email, fullName)
                        onAuthSuccess(true, pendingCheckoutProduct != null)
                    } else {
                        repository.login(email, if (fullName.isNotBlank()) fullName else "Hoàng My")
                        onAuthSuccess(false, pendingCheckoutProduct != null)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
