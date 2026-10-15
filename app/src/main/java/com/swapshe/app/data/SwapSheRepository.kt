package com.swapshe.app.data

import com.swapshe.app.model.AppNotification
import com.swapshe.app.model.BodyMeasurements
import com.swapshe.app.model.DisputeReport
import com.swapshe.app.model.DisputeStatus
import com.swapshe.app.model.DisputeTargetType
import com.swapshe.app.model.Order
import com.swapshe.app.model.OrderReview
import com.swapshe.app.model.OrderStatus
import com.swapshe.app.model.PaymentStatus
import com.swapshe.app.model.Product
import com.swapshe.app.model.ProductStatus
import com.swapshe.app.model.ShippingAddress
import com.swapshe.app.model.SwapOffer
import com.swapshe.app.model.SwapOfferStatus
import com.swapshe.app.model.SwapTransaction
import com.swapshe.app.model.SwapTransactionStatus
import com.swapshe.app.model.TimelineEvent
import com.swapshe.app.model.TwoWayShipment
import com.swapshe.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SwapSheRepository private constructor() {

    private val _currentUser = MutableStateFlow(MockData.initialUser)
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Public explore products
    private val _products = MutableStateFlow(MockData.products)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // User's own closet (F22)
    private val _myCloset = MutableStateFlow(MockData.myClosetInitial)
    val myCloset: StateFlow<List<Product>> = _myCloset.asStateFlow()

    // Buyer orders (F18)
    private val _orders = MutableStateFlow(MockData.initialOrders)
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Seller incoming orders (F24)
    private val _sellerOrders = MutableStateFlow(MockData.initialSellerOrders)
    val sellerOrders: StateFlow<List<Order>> = _sellerOrders.asStateFlow()

    // Direct Swap Offers (F28)
    private val _swapOffers = MutableStateFlow(MockData.initialSwapOffers)
    val swapOffers: StateFlow<List<SwapOffer>> = _swapOffers.asStateFlow()

    // Active Swap Transactions (F29)
    private val _swapTransactions = MutableStateFlow(MockData.initialSwapTransactions)
    val swapTransactions: StateFlow<List<SwapTransaction>> = _swapTransactions.asStateFlow()

    // Disputes and Reports (F30)
    private val _disputes = MutableStateFlow(MockData.initialDisputes)
    val disputes: StateFlow<List<DisputeReport>> = _disputes.asStateFlow()

    private val _pendingCheckoutProduct = MutableStateFlow<Product?>(null)
    val pendingCheckoutProduct: StateFlow<Product?> = _pendingCheckoutProduct.asStateFlow()

    private val _currentAddress = MutableStateFlow(MockData.defaultAddress)
    val currentAddress: StateFlow<ShippingAddress> = _currentAddress.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _notifications = MutableStateFlow(MockData.notifications)
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    // Smart Wishlist & Saved Search (Flow E / F13)
    private val _savedSearches = MutableStateFlow(listOf(
        com.swapshe.app.model.SavedSearch(
            id = "search_01",
            query = "Cardigan len",
            style = "Vintage",
            size = "S",
            maxPrice = 200000L,
            notifyNewItem = true,
            createdAt = "14/10/2026"
        ),
        com.swapshe.app.model.SavedSearch(
            id = "search_02",
            query = "Blazer linen",
            style = "Minimal",
            size = "M",
            maxPrice = 300000L,
            notifyNewItem = true,
            createdAt = "12/10/2026"
        )
    ))
    val savedSearches: StateFlow<List<com.swapshe.app.model.SavedSearch>> = _savedSearches.asStateFlow()

    fun addSavedSearch(query: String, style: String?, size: String?, maxPrice: Long?) {
        val newSearch = com.swapshe.app.model.SavedSearch(
            id = "search_${System.currentTimeMillis()}",
            query = query,
            style = style,
            size = size,
            maxPrice = maxPrice,
            notifyNewItem = true,
            createdAt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        )
        _savedSearches.value = listOf(newSearch) + _savedSearches.value
        showToast("Đã lưu bộ lọc tìm kiếm! Hệ thống sẽ thông báo khi có đồ mới ✨")
    }

    fun removeSavedSearch(id: String) {
        _savedSearches.value = _savedSearches.value.filter { it.id != id }
        showToast("Đã xóa bộ lọc tìm kiếm.")
    }

    fun toggleSavedSearchAlert(id: String) {
        _savedSearches.value = _savedSearches.value.map {
            if (it.id == id) it.copy(notifyNewItem = !it.notifyNewItem) else it
        }
    }

    fun markNotificationAsRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun setGuestMode() {
        _currentUser.value = User(
            id = "guest_temp",
            name = "Guest Traveler",
            email = "",
            isGuest = true
        )
    }

    fun login(email: String, name: String = "Hoàng My") {
        _currentUser.value = MockData.initialUser.copy(
            email = email,
            name = name,
            isGuest = false
        )
    }

    fun register(email: String, name: String) {
        _currentUser.value = User(
            id = "user_${System.currentTimeMillis()}",
            name = name,
            email = email,
            isGuest = false,
            savedAddresses = listOf(MockData.defaultAddress)
        )
    }

    fun logout() {
        setGuestMode()
    }

    fun updatePreferences(
        styles: Set<String>? = null,
        size: String? = null,
        colors: Set<String>? = null,
        measurements: BodyMeasurements? = null
    ) {
        val curr = _currentUser.value
        _currentUser.value = curr.copy(
            stylePreferences = styles ?: curr.stylePreferences,
            sizePreference = size ?: curr.sizePreference,
            colorPreferences = colors ?: curr.colorPreferences,
            bodyMeasurements = measurements ?: curr.bodyMeasurements
        )
    }

    fun toggleLike(productId: String) {
        _products.value = _products.value.map { product ->
            if (product.id == productId) {
                product.copy(isLiked = !product.isLiked)
            } else product
        }
    }

    fun setPendingCheckout(product: Product?) {
        _pendingCheckoutProduct.value = product
    }

    fun updateShippingAddress(address: ShippingAddress) {
        _currentAddress.value = address
    }

    // --- BUYER ORDER OPERATIONS ---
    fun createOrder(product: Product, address: ShippingAddress, isCod: Boolean = false): Order {
        val dateStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomSeq = (100..999).random()
        val orderCode = "SW-$dateStr-$randomSeq"
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        val pStatus = if (isCod) PaymentStatus.COD_PENDING else PaymentStatus.PAID
        val oStatus = if (isCod) OrderStatus.PLACED else OrderStatus.PAID

        val newOrder = Order(
            id = "order_${System.currentTimeMillis()}",
            internalCode = orderCode,
            trackingCode = "GHN-${(10000000..99999999).random()}",
            item = product,
            shippingAddress = address,
            deliveryMethod = "Standard Delivery",
            shippingFee = 25000L,
            subtotal = product.price,
            total = product.price + 25000L,
            status = oStatus,
            paymentStatus = pStatus,
            buyerId = _currentUser.value.id,
            sellerId = product.seller.id,
            timeline = listOf(
                TimelineEvent(
                    if (isCod) "Order Placed (COD)" else "Payment confirmed",
                    if (isCod) "Đơn hàng đã ghi nhận phương thức thanh toán khi nhận hàng" else "Mô phỏng thanh toán đã xác thực",
                    timeNow,
                    true
                ),
                TimelineEvent("Seller preparing item", "Người bán đang chuẩn bị đóng gói sản phẩm", "Đang xử lý", false)
            )
        )

        // Mark product as sold across catalog
        _products.value = _products.value.map {
            if (it.id == product.id) it.copy(status = ProductStatus.SOLD) else it
        }

        _orders.value = listOf(newOrder) + _orders.value
        _pendingCheckoutProduct.value = null
        return newOrder
    }

    fun confirmDelivery(orderId: String) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                val updatedTimeline = order.timeline + TimelineEvent(
                    "Order completed",
                    "Người mua đã xác nhận nhận hàng",
                    timeNow,
                    true
                )
                order.copy(
                    status = OrderStatus.COMPLETED,
                    paymentStatus = PaymentStatus.PAID,
                    timeline = updatedTimeline
                )
            } else order
        }
        showToast("Đã xác nhận nhận hàng thành công!")
    }

    fun reviewOrder(orderId: String, rating: Int, comment: String) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(review = OrderReview(rating, comment))
            } else order
        }
        showToast("Cảm ơn bạn đã gửi đánh giá!")
    }

    // --- SELLER CLOSET (F22 - F25) ---
    fun saveListing(product: Product, isPublish: Boolean) {
        val currentUserId = _currentUser.value.id
        val updatedProduct = product.copy(
            ownerId = currentUserId,
            seller = MockData.mySellerProfile.copy(id = currentUserId, name = _currentUser.value.name),
            status = if (isPublish) ProductStatus.AVAILABLE else ProductStatus.DRAFT
        )

        val existingIdx = _myCloset.value.indexOfFirst { it.id == updatedProduct.id }
        if (existingIdx >= 0) {
            _myCloset.value = _myCloset.value.toMutableList().apply {
                set(existingIdx, updatedProduct)
            }
        } else {
            _myCloset.value = listOf(updatedProduct) + _myCloset.value
        }

        if (isPublish) {
            // Also make available in public products
            _products.value = listOf(updatedProduct) + _products.value.filter { it.id != updatedProduct.id }
            showToast("Đã đăng món đồ lên Tủ đồ SWAPSHE thành công! ✨")

            // Check against saved searches (Flow E: Smart matching alert)
            val matchingSearches = _savedSearches.value.filter { s ->
                s.notifyNewItem && (
                    s.style.isNullOrBlank() || s.style.equals(updatedProduct.style, ignoreCase = true) ||
                    s.size.isNullOrBlank() || s.size.equals(updatedProduct.size, ignoreCase = true) ||
                    (s.maxPrice != null && updatedProduct.price <= s.maxPrice)
                )
            }
            if (matchingSearches.isNotEmpty()) {
                val newNotif = AppNotification(
                    id = "notif_${System.currentTimeMillis()}",
                    title = "✨ Đồ mới hợp gu bạn vừa lên kệ!",
                    message = "Món '${updatedProduct.title}' (Size ${updatedProduct.size}) vừa khớp với bộ lọc bạn đã lưu.",
                    timestamp = "Vừa xong",
                    isRead = false,
                    type = "saved_search_match"
                )
                _notifications.value = listOf(newNotif) + _notifications.value
            }
        } else {
            showToast("Đã lưu bản nháp vào tủ đồ của bạn.")
        }
    }

    fun deleteDraft(productId: String) {
        _myCloset.value = _myCloset.value.filter { it.id != productId }
        showToast("Đã xóa món đồ khỏi tủ đồ.")
    }

    fun updateSellerOrderStatus(orderId: String, nextStatus: OrderStatus) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _sellerOrders.value = _sellerOrders.value.map { order ->
            if (order.id == orderId) {
                val desc = when (nextStatus) {
                    OrderStatus.PREPARING -> "Bạn đã xác nhận chuẩn bị gói hàng"
                    OrderStatus.SHIPPED -> "Bạn đã bàn giao gói hàng cho bưu tá GHN"
                    OrderStatus.DELIVERED -> "Bưu tá đã giao hàng thành công đến người mua"
                    OrderStatus.COMPLETED -> "Đơn hàng đã hoàn tất giao dịch"
                    OrderStatus.CANCELLED -> "Người bán hủy đơn"
                    else -> "Cập nhật trạng thái"
                }
                val newEvent = TimelineEvent(nextStatus.label, desc, timeNow, true)
                order.copy(status = nextStatus, timeline = order.timeline + newEvent)
            } else order
        }
        showToast("Đã cập nhật trạng thái đơn bán thành công.")
    }

    // --- DIRECT SWAP OPERATIONS (F26 - F29) ---
    fun sendSwapOffer(requestedProduct: Product, offeredProduct: Product, note: String) {
        val timeNow = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(Date())
        val newOffer = SwapOffer(
            id = "swap_${System.currentTimeMillis()}",
            senderId = _currentUser.value.id,
            senderName = _currentUser.value.name,
            senderAvatar = _currentUser.value.avatarUrl,
            recipientId = requestedProduct.seller.id,
            recipientName = requestedProduct.seller.name,
            offeredProduct = offeredProduct,
            requestedProduct = requestedProduct,
            note = note,
            status = SwapOfferStatus.PENDING,
            createdAt = timeNow
        )

        _swapOffers.value = listOf(newOffer) + _swapOffers.value
        showToast("Đã gửi đề nghị trao đổi 1-1! Chờ người bán phản hồi nhé 🔄")
    }

    fun acceptSwapOffer(offerId: String): Boolean {
        val offer = _swapOffers.value.find { it.id == offerId } ?: return false

        // Check availability of both items (Atomic Conflict Check)
        val isRequestedAvailable = _myCloset.value.find { it.id == offer.requestedProduct.id }?.status == ProductStatus.AVAILABLE ||
                _products.value.find { it.id == offer.requestedProduct.id }?.status == ProductStatus.AVAILABLE
        val isOfferedAvailable = _products.value.find { it.id == offer.offeredProduct.id }?.status == ProductStatus.AVAILABLE ||
                _myCloset.value.find { it.id == offer.offeredProduct.id }?.status == ProductStatus.AVAILABLE

        if (!isRequestedAvailable || !isOfferedAvailable) {
            _swapOffers.value = _swapOffers.value.map {
                if (it.id == offerId) it.copy(status = SwapOfferStatus.CONFLICT) else it
            }
            showToast("Rất tiếc! Một trong hai món đồ đã không còn khả dụng để trao đổi.")
            return false
        }

        // Lock both products atomically as RESERVED / SWAPPED
        _myCloset.value = _myCloset.value.map {
            if (it.id == offer.requestedProduct.id || it.id == offer.offeredProduct.id) {
                it.copy(status = ProductStatus.RESERVED)
            } else it
        }
        _products.value = _products.value.map {
            if (it.id == offer.requestedProduct.id || it.id == offer.offeredProduct.id) {
                it.copy(status = ProductStatus.RESERVED)
            } else it
        }

        val txId = "tx_swap_${System.currentTimeMillis()}"
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        val transaction = SwapTransaction(
            id = txId,
            offerId = offer.id,
            userAId = offer.senderId,
            userAName = offer.senderName,
            userBId = _currentUser.value.id,
            userBName = _currentUser.value.name,
            productA = offer.offeredProduct,
            productB = offer.requestedProduct,
            shipmentAtoB = TwoWayShipment(
                id = "ship_${System.currentTimeMillis()}_A2B",
                fromUserId = offer.senderId,
                toUserId = _currentUser.value.id,
                trackingCodeDemo = "DEMO-SHIP-A2B-${(1000..9999).random()}"
            ),
            shipmentBtoA = TwoWayShipment(
                id = "ship_${System.currentTimeMillis()}_B2A",
                fromUserId = _currentUser.value.id,
                toUserId = offer.senderId,
                trackingCodeDemo = "DEMO-SHIP-B2A-${(1000..9999).random()}"
            ),
            status = SwapTransactionStatus.AWAITING_DISPATCH,
            createdAt = timeNow,
            timeline = listOf(
                TimelineEvent("Đề nghị trao đổi được chấp thuận", "Cả 2 bên bắt đầu chuẩn bị gửi hàng", timeNow, true)
            )
        )

        _swapTransactions.value = listOf(transaction) + _swapTransactions.value
        _swapOffers.value = _swapOffers.value.map {
            if (it.id == offerId) it.copy(status = SwapOfferStatus.ACCEPTED, swapTransactionId = txId) else it
        }
        showToast("Đã chấp nhận đề nghị đổi đồ! Giao dịch 2 chiều đã bắt đầu.")
        return true
    }

    fun rejectSwapOffer(offerId: String) {
        _swapOffers.value = _swapOffers.value.map {
            if (it.id == offerId) it.copy(status = SwapOfferStatus.REJECTED) else it
        }
        showToast("Đã từ chối lời mời đổi đồ.")
    }

    fun cancelSwapOffer(offerId: String) {
        _swapOffers.value = _swapOffers.value.map {
            if (it.id == offerId) it.copy(status = SwapOfferStatus.CANCELLED) else it
        }
        showToast("Đã hủy lời mời đổi đồ.")
    }

    fun dispatchSwapShipment(transactionId: String, isUserA: Boolean) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _swapTransactions.value = _swapTransactions.value.map { tx ->
            if (tx.id == transactionId) {
                val updatedA2B = if (isUserA) tx.shipmentAtoB.copy(isDispatched = true, dispatchedAt = timeNow) else tx.shipmentAtoB
                val updatedB2A = if (!isUserA) tx.shipmentBtoA.copy(isDispatched = true, dispatchedAt = timeNow) else tx.shipmentBtoA
                val bothDispatched = updatedA2B.isDispatched && updatedB2A.isDispatched
                val newStatus = if (bothDispatched) SwapTransactionStatus.BOTH_SHIPPED else SwapTransactionStatus.PARTIALLY_SHIPPED

                tx.copy(
                    shipmentAtoB = updatedA2B,
                    shipmentBtoA = updatedB2A,
                    status = newStatus,
                    timeline = tx.timeline + TimelineEvent(
                        if (isUserA) "${tx.userAName} đã gửi hàng" else "Bạn đã gửi hàng",
                        "Đã tạo mã vận đơn giả lập",
                        timeNow,
                        true
                    )
                )
            } else tx
        }
        showToast("Đã ghi nhận gửi hàng thành công!")
    }

    fun confirmSwapReceived(transactionId: String, isUserA: Boolean) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _swapTransactions.value = _swapTransactions.value.map { tx ->
            if (tx.id == transactionId) {
                // If User A confirms, it means A received B's shipment (shipmentBtoA)
                // If User B confirms, it means B received A's shipment (shipmentAtoB)
                val updatedA2B = if (!isUserA) tx.shipmentAtoB.copy(isReceivedConfirmed = true, receivedAt = timeNow, isDelivered = true) else tx.shipmentAtoB
                val updatedB2A = if (isUserA) tx.shipmentBtoA.copy(isReceivedConfirmed = true, receivedAt = timeNow, isDelivered = true) else tx.shipmentBtoA
                val bothReceived = updatedA2B.isReceivedConfirmed && updatedB2A.isReceivedConfirmed

                val newStatus = if (bothReceived) SwapTransactionStatus.COMPLETED else SwapTransactionStatus.PARTIALLY_RECEIVED

                if (bothReceived) {
                    // Mark products as SOLD/EXCHANGED
                    _myCloset.value = _myCloset.value.map {
                        if (it.id == tx.productA.id || it.id == tx.productB.id) it.copy(status = ProductStatus.SOLD) else it
                    }
                }

                tx.copy(
                    shipmentAtoB = updatedA2B,
                    shipmentBtoA = updatedB2A,
                    status = newStatus,
                    timeline = tx.timeline + TimelineEvent(
                        if (bothReceived) "Trao đổi hoàn tất 100%" else (if (isUserA) "${tx.userAName} đã xác nhận nhận đồ" else "Bạn đã xác nhận nhận đồ"),
                        "Kiểm tra tình trạng hàng hóa thành công",
                        timeNow,
                        true
                    )
                )
            } else tx
        }
        showToast("Đã xác nhận nhận đồ thành công!")
    }

    // --- TRUST & REPORT (F30) ---
    fun submitDispute(targetType: DisputeTargetType, targetId: String, targetTitle: String, reason: String, description: String): DisputeReport {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val newDispute = DisputeReport(
            id = "case_${System.currentTimeMillis()}",
            reporterId = _currentUser.value.id,
            reporterName = _currentUser.value.name,
            targetType = targetType,
            targetId = targetId,
            targetTitle = targetTitle,
            reason = reason,
            description = description,
            status = DisputeStatus.OPEN,
            createdAt = timeNow
        )

        _disputes.value = listOf(newDispute) + _disputes.value

        // If target is Order, update order to DISPUTED to pause auto-completion
        if (targetType == DisputeTargetType.ORDER) {
            _orders.value = _orders.value.map {
                if (it.id == targetId) it.copy(status = OrderStatus.DISPUTED) else it
            }
            _sellerOrders.value = _sellerOrders.value.map {
                if (it.id == targetId) it.copy(status = OrderStatus.DISPUTED) else it
            }
        } else if (targetType == DisputeTargetType.SWAP) {
            _swapTransactions.value = _swapTransactions.value.map {
                if (it.id == targetId) it.copy(status = SwapTransactionStatus.DISPUTED) else it
            }
        }

        showToast("Đã gửi khiếu nại (Mã: #${newDispute.id}). CSKH SWAPSHE sẽ xử lý trong 24h.")
        return newDispute
    }

    // Helper for testing order status transitions
    fun advanceOrderStatus(orderId: String) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                val nextStatus = when (order.status) {
                    OrderStatus.PLACED -> OrderStatus.PAID
                    OrderStatus.PAID -> OrderStatus.PREPARING
                    OrderStatus.PREPARING -> OrderStatus.SHIPPED
                    OrderStatus.SHIPPED -> OrderStatus.DELIVERED
                    OrderStatus.DELIVERED -> OrderStatus.COMPLETED
                    else -> order.status
                }
                val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                val newEvent = TimelineEvent(nextStatus.label, "Cập nhật tiến trình đơn", timeNow, true)
                order.copy(status = nextStatus, timeline = order.timeline + newEvent)
            } else order
        }
    }

    companion object {
        val instance by lazy { SwapSheRepository() }
    }
}
