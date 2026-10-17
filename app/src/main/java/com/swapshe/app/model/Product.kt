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

enum class SwapSheGrade(val code: String, val label: String, val percentage: String, val description: String) {
    GRADE_S("Hạng S", "Like New 99%", "99%", "Gần như mới tinh, nguyên tag hoặc mặc 1 lần chụp ảnh"),
    GRADE_A("Hạng A", "Rất tốt 90%", "90%", "Đã mặc vài lần, phom dáng & màu sắc giữ rất đẹp, không rách/ố"),
    GRADE_B("Hạng B", "Khá 80%", "80%", "Secondhand có vết sờn nhẹ hoặc ố nhỏ đã được ghi chú minh bạch")
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
    val grade: SwapSheGrade = SwapSheGrade.GRADE_S,
    val size: String,
    val category: String,
    val style: String,
    val color: String,
    val images: List<String>,
    val anglePhotos: Map<String, String> = emptyMap(), // front, back, tag, defect
    val videoUrl: String? = null,
    val flaws: List<String> = emptyList(),
    val fabricStretch: String = "Không co giãn",
    val measurements: ProductMeasurements,
    val description: String,
    val seller: Seller,
    val canSwap: Boolean = false,
    val allowBuy: Boolean = true,
    val status: ProductStatus = ProductStatus.AVAILABLE,
    val isLiked: Boolean = false,
    val ownerId: String = seller.id
)
