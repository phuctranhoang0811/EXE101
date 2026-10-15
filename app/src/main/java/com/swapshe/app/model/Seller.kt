package com.swapshe.app.model

data class SellerReview(
    val id: String,
    val reviewerName: String,
    val reviewerAvatar: String,
    val rating: Int,
    val comment: String,
    val productName: String,
    val date: String
)

data class Seller(
    val id: String,
    val name: String,
    val shopName: String,
    val avatarUrl: String,
    val rating: Float,
    val reviewCount: Int,
    val successfulTransactions: Int,
    val memberSince: String,
    val location: String,
    val isVerified: Boolean = false,
    val bio: String = "",
    val reviews: List<SellerReview> = emptyList()
)

