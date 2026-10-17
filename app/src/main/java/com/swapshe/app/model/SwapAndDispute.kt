package com.swapshe.app.model

enum class SwapOfferStatus(val label: String) {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled"),
    CONFLICT("Unavailable / Conflict")
}

enum class SwapTransactionStatus(val label: String) {
    AWAITING_DISPATCH("Awaiting Dispatch"),
    PARTIALLY_SHIPPED("Partially Shipped"),
    BOTH_SHIPPED("Both Shipped"),
    PARTIALLY_RECEIVED("Partially Received"),
    COMPLETED("Completed"),
    DISPUTED("Disputed"),
    FAILED("Cancelled / Failed")
}

data class TwoWayShipment(
    val id: String,
    val fromUserId: String,
    val toUserId: String,
    val trackingCodeDemo: String,
    val carrierName: String = "GHN Express",
    val isDispatched: Boolean = false,
    val dispatchedAt: String? = null,
    val isDelivered: Boolean = false,
    val isReceivedConfirmed: Boolean = false,
    val receivedAt: String? = null
)

data class SwapOffer(
    val id: String,
    val senderId: String,
    val senderName: String,
    val senderAvatar: String,
    val recipientId: String,
    val recipientName: String,
    val offeredProduct: Product,
    val requestedProduct: Product,
    val note: String = "",
    val status: SwapOfferStatus = SwapOfferStatus.PENDING,
    val createdAt: String,
    val swapTransactionId: String? = null
)

data class SwapTransaction(
    val id: String,
    val offerId: String,
    val userAId: String,
    val userAName: String,
    val userBId: String,
    val userBName: String,
    val productA: Product, // Item from User A to User B
    val productB: Product, // Item from User B to User A
    val shipmentAtoB: TwoWayShipment,
    val shipmentBtoA: TwoWayShipment,
    val status: SwapTransactionStatus = SwapTransactionStatus.AWAITING_DISPATCH,
    val createdAt: String,
    val timeline: List<TimelineEvent> = emptyList(),
    val escrowDepositAmount: Long = 50000L,
    val isEscrowDepositedA: Boolean = true,
    val isEscrowDepositedB: Boolean = false
)

enum class DisputeTargetType(val label: String) {
    LISTING("Listing"),
    ORDER("Order"),
    SWAP("Swap Transaction"),
    USER("User")
}

enum class DisputeStatus(val label: String) {
    OPEN("Open"),
    UNDER_REVIEW("Under Review"),
    RESOLVED("Resolved"),
    REJECTED("Rejected")
}

enum class DisputeOfferType(val label: String) {
    PARTIAL_REFUND("Thương lượng hoàn tiền một phần"),
    FULL_RETURN_REFUND("Đồng ý trả hàng hoàn tiền 100%"),
    REJECT("Từ chối khiếu nại (Không chấp thuận)"),
    REJECT_AND_ESCALATE("Từ chối & Nhờ SwapShe Admin phân xử")
}

data class DisputeOffer(
    val type: DisputeOfferType,
    val amount: Long? = null,
    val note: String = "",
    val timestamp: String = ""
)

data class DisputeReport(
    val id: String,
    val reporterId: String,
    val reporterName: String,
    val targetType: DisputeTargetType,
    val targetId: String,
    val targetTitle: String,
    val reason: String,
    val description: String,
    val status: DisputeStatus = DisputeStatus.OPEN,
    val createdAt: String,
    val resolutionNote: String? = null,
    val evidencePhotos: List<String> = emptyList(),
    val evidenceVideoUrl: String? = null,
    val requestedRefundAmount: Long? = null,
    val sellerOffer: DisputeOffer? = null,
    val adminVerdict: String? = null,
    val returnTrackingCode: String? = null,
    val isEscrowFrozen: Boolean = true,
    val isSellerReturnConfirmed: Boolean = false
) {
    val requestedAmount: Long get() = requestedRefundAmount ?: 0L
    val evidenceImages: List<String> get() = evidencePhotos
}

data class SavedSearch(
    val id: String,
    val query: String = "",
    val style: String? = null,
    val size: String? = null,
    val maxPrice: Long? = null,
    val category: String? = null,
    val notifyNewItem: Boolean = true,
    val createdAt: String
)

