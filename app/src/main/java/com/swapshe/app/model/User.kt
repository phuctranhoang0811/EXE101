package com.swapshe.app.model

data class BodyMeasurements(
    val heightCm: Int? = null,
    val weightKg: Int? = null,
    val bustCm: Int? = null,
    val waistCm: Int? = null,
    val hipsCm: Int? = null
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val isGuest: Boolean = false,
    val avatarUrl: String = "",
    val stylePreferences: Set<String> = emptySet(),
    val sizePreference: String? = null,
    val bodyMeasurements: BodyMeasurements? = null,
    val colorPreferences: Set<String> = emptySet(),
    val skinTone: String = "Tone ấm (Warm Undertone)",
    val savedAddresses: List<ShippingAddress> = emptyList(),
    val trustProfile: SheTrustProfile = SheTrustProfile(),
    val wallet: Wallet = Wallet(),
    val role: UserRole = UserRole.BUYER,
    val bio: String = ""
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String = "Vừa xong",
    val isRead: Boolean = false,
    val type: String = "order",
    val targetProductId: String? = null
)

enum class UserRole(val label: String, val icon: String) {
    BUYER("Người mua (Buyer)", "👩"),
    SELLER("Người bán (Seller)", "👗"),
    ADMIN("SwapShe Trọng tài (Admin)", "⚖️")
}

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean = true,
    val isWarning: Boolean = false,
    val warningText: String? = null
)
