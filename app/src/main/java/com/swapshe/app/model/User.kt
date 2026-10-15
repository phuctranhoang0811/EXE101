package com.swapshe.app.model

data class BodyMeasurements(
    val heightCm: Int? = null,
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
    val savedAddresses: List<ShippingAddress> = emptyList()
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val type: String = "order"
)

