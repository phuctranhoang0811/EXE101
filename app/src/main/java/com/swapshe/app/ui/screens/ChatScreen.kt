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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swapshe.app.data.SwapSheRepository
import com.swapshe.app.model.ChatMessage
import com.swapshe.app.theme.BackgroundSubtle
import com.swapshe.app.theme.BorderColor
import com.swapshe.app.theme.ErrorBg
import com.swapshe.app.theme.ErrorRed
import com.swapshe.app.theme.EscrowBg
import com.swapshe.app.theme.EscrowBlue
import com.swapshe.app.theme.Primary
import com.swapshe.app.theme.PrimarySoft15
import com.swapshe.app.theme.SurfaceWhite
import com.swapshe.app.theme.TextMuted
import com.swapshe.app.theme.TextPrimary
import com.swapshe.app.theme.TextSecondary
import com.swapshe.app.theme.WarmCream

@Composable
fun ChatScreen(
    recipientName: String = "Thùy Linh (Linh's Closet 👗)",
    productId: String? = null,
    onNavigateBack: () -> Unit,
    onNavigateToSwapOffer: ((String) -> Unit)? = null
) {
    val repository = SwapSheRepository.instance
    val chatMessages by repository.chatMessages.collectAsState()
    var inputText by remember { mutableStateOf("") }

    val quickTestPrompts = listOf(
        "Chào bạn, áo này còn mới không?",
        "Cho mình xin số tài khoản Zalo chuyển khoản riêng nhé!",
        "Đổi ngang 1-1 với chiếc áo blazer của mình nhé?"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .statusBarsPadding()
    ) {
        // App Bar
        Surface(
            color = SurfaceWhite,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(PrimarySoft15, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = recipientName.take(1).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = recipientName,
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Đang hoạt động • She-Trust 98/100 👑",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                if (productId != null && onNavigateToSwapOffer != null) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = PrimarySoft15,
                        modifier = Modifier.clickable { onNavigateToSwapOffer(productId) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.SwapHoriz, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                            Text(
                                text = " Đổi đồ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }
                    }
                }
            }
        }

        // Flow H Banner: Escrow & Anti-Scam notice
        Surface(
            color = EscrowBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, EscrowBlue.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Shield, contentDescription = null, tint = EscrowBlue, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Bảo vệ bởi SwapShe: Hệ thống tự động chặn gạ chuyển khoản ngoài luồng.",
                    fontSize = 11.sp,
                    color = EscrowBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(chatMessages, key = { it.id }) { msg ->
                ChatBubble(message = msg)
            }
        }

        // Demo Quick Prompts
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceWhite)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "⚡ Gợi ý gõ nhanh để test demo:",
                fontSize = 10.sp,
                color = TextMuted,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickTestPrompts.forEach { prompt ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BackgroundSubtle,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { inputText = prompt }
                    ) {
                        Text(
                            text = prompt,
                            fontSize = 10.sp,
                            maxLines = 2,
                            color = TextSecondary,
                            lineHeight = 13.sp,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
            }
        }

        // Chat Input Bar
        Surface(
            color = SurfaceWhite,
            shadowElevation = 8.dp,
            modifier = Modifier.navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Nhập tin nhắn... (Thử gõ số tài khoản hoặc Zalo)", fontSize = 12.sp, color = TextMuted) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = BorderColor
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            repository.sendChatMessage(inputText, recipientName)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Primary, CircleShape)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromMe) 16.dp else 4.dp,
                bottomEnd = if (message.isFromMe) 4.dp else 16.dp
            ),
            color = if (message.isFromMe) Primary else SurfaceWhite,
            border = if (!message.isFromMe) androidx.compose.foundation.BorderStroke(1.dp, BorderColor) else null,
            shadowElevation = 1.dp,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = if (message.isFromMe) Color.White else TextPrimary
                )
                Text(
                    text = message.timestamp,
                    fontSize = 10.sp,
                    color = if (message.isFromMe) Color.White.copy(alpha = 0.7f) else TextMuted,
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                )
            }
        }

        // Flow H: Scam warning callout banner directly below message
        if (message.isWarning && message.warningText != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ErrorBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .padding(top = 2.dp)
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                    Icon(imageVector = Icons.Filled.Warning, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = message.warningText,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = ErrorRed
                    )
                }
            }
        }
    }
}
