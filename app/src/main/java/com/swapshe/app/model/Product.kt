package com.swapshe.app.model

enum class ConditionType(val label: String) {
    LIKE_NEW("Like New"),
    EXCELLENT("Excellent"),
    GOOD("Good"),
    FAIR("Fair")
}

enum class ProductStatus(val label: String) {
    AVAILABLE("Available"),
    RESERVED("Reserved"),
    SOLD("Sold"),
    DRAFT("Draft"),
    HIDDEN("Hidden"),
    ARCHIVED("Archived")
}

data class ProductMeasurements(
    val bust: Int? = null,
    val length: Int? = null,
    val waist: Int? = null,
    val hips: Int? = null,
    val shoulder: Int? = null
)

data class Product(
    val id: String,
    val title: String,
    val brand: String,
    val price: Long,
    val originalPrice: Long? = null,
    val condition: ConditionType,
    val size: String,
    val category: String,
    val style: String,
    val color: String,
    val images: List<String>,
    val measurements: ProductMeasurements,
    val description: String,
    val seller: Seller,
    val canSwap: Boolean = false,
    val allowBuy: Boolean = true,
    val status: ProductStatus = ProductStatus.AVAILABLE,
    val isLiked: Boolean = false,
    val ownerId: String = seller.id
)
