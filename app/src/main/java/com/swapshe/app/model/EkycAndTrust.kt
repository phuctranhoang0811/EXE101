package com.swapshe.app.model

enum class EkycStatus(val label: String) {
    NOT_VERIFIED("Chưa định danh"),
    PENDING("Đang chờ duyệt"),
    VERIFIED("Đã xác thực eKYC ✓")
}

enum class TrustBadgeType(val title: String, val perk: String, val minScore: Int) {
    QUEEN_CLOSET("Queen Closet 👑", "Giảm 50% phí sàn (chỉ 2.5%) • Ưu tiên hiển thị Top 1", 90),
    TRUSTED_SELLER("Trusted Seller ⭐", "Miễn phí đặt cọc đổi đồ • Huy hiệu tích xanh uy tín", 75),
    RISING_STAR("Tủ đồ triển vọng 🌸", "Ưu tiên gợi ý trong Smart Match", 50),
    STANDARD("Thành viên mới 🌱", "Định danh để nâng hạn mức bán & đổi đồ", 0);

    val badgeName: String get() = title
}

data class EkycDocument(
    val fullName: String,
    val idCardNumber: String,
    val dob: String,
    val phone: String,
    val frontCardUrl: String = "",
    val backCardUrl: String = "",
    val faceScanCompleted: Boolean = true,
    val verifiedDate: String = ""
)

data class SheTrustProfile(
    val score: Int = 98,
    val badge: TrustBadgeType = TrustBadgeType.QUEEN_CLOSET,
    val status: EkycStatus = EkycStatus.VERIFIED,
    val document: EkycDocument? = EkycDocument(
        fullName = "NGUYỄN HOÀNG MY",
        idCardNumber = "079198012345",
        dob = "11/08/1998",
        phone = "0987654321",
        verifiedDate = "15/09/2026"
    ),
    val completedOrdersCount: Int = 28,
    val disputeFreeStreakMonths: Int = 8
) {
    val trustScore: Int get() = score
    val isEkycVerified: Boolean get() = status == EkycStatus.VERIFIED
    val ekycStatus: EkycStatus get() = status
}
