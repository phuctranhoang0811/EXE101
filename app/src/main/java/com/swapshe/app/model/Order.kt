package com.swapshe.app.model

enum class PaymentStatus(val label: String) {
    PENDING("Payment Pending"),
    PAID("Paid"),
    FAILED("Failed"),
    COD_PENDING("COD Pending"),
    REFUNDED("Refunded")
}

enum class OrderStatus(val label: String) {
    PLACED("Order Placed"),
    PAID("Payment Confirmed"),
    PREPARING("Seller Preparing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    DISPUTED("Issue Reported")
}

data class TimelineEvent(
    val title: String,
    val description: String,
    val timestamp: String,
    val isCompleted: Boolean = true
)

data class ShippingAddress(
    val fullName: String,
    val phone: String,
    val province: String,
    val ward: String,
    val detailAddress: String
) {
    val fullAddressText: String
        get() = "$detailAddress, $ward, $province"
}

data class OrderReview(
    val rating: Int,
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class Order(
    val id: String,
    val internalCode: String,
    val trackingCode: String? = null,
    val item: Product,
    val shippingAddress: ShippingAddress,
    val deliveryMethod: String = "Standard Delivery",
    val shippingFee: Long = 25000L,
    val subtotal: Long,
    val total: Long,
    val status: OrderStatus = OrderStatus.PREPARING,
    val paymentStatus: PaymentStatus = PaymentStatus.PAID,
    val buyerId: String = "user_my",
    val sellerId: String = item.seller.id,
    val createdAt: Long = System.currentTimeMillis(),
    val timeline: List<TimelineEvent> = emptyList(),
    val review: OrderReview? = null
)
