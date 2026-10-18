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
import com.swapshe.app.model.UserRole
import com.swapshe.app.model.ChatMessage
import com.swapshe.app.model.Wallet
import com.swapshe.app.model.WalletTransaction
import com.swapshe.app.model.WalletTransactionType
import com.swapshe.app.model.BankPayoutAccount
import com.swapshe.app.model.EkycStatus
import com.swapshe.app.model.EkycDocument
import com.swapshe.app.model.SheTrustProfile
import com.swapshe.app.model.TrustBadgeType
import com.swapshe.app.model.DisputeOffer
import com.swapshe.app.model.DisputeOfferType
import com.swapshe.app.model.SwapSheGrade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SwapSheRepository private constructor() : ISwapSheRepository {

    // Role Switching for Demo & QA Architect Testing
    private val _currentRole = MutableStateFlow(UserRole.BUYER)
    override val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // SQLite Database Helper for User Wardrobe/Closet CRUD
    private var dbHelper: com.swapshe.app.data.db.SwapSheDbHelper? = null

    override fun initDatabase(context: android.content.Context) {
        if (dbHelper == null) {
            val helper = com.swapshe.app.data.db.SwapSheDbHelper(context.applicationContext)
            dbHelper = helper

            // Seed initial items if database is empty on first run
            if (helper.getProductCount() == 0) {
                // Seed My Closet items
                MockData.myClosetInitial.forEach { helper.insertProduct(it) }
                // Seed Seller Linh items
                MockData.products.filter { it.seller.id == "seller_linh" }.forEach {
                    helper.insertProduct(it.copy(ownerId = "seller_linh"))
                }
            }
            refreshClosetFromDb()
        }
    }

    private fun refreshClosetFromDb() {
        val helper = dbHelper ?: return
        val currentUserId = _currentUser.value.id
        val items = helper.getProductsByOwner(currentUserId)
        if (items.isNotEmpty()) {
            _myCloset.value = items
        } else if (_currentRole.value == UserRole.SELLER) {
            _myCloset.value = helper.getProductsByOwner("seller_linh")
        } else {
            _myCloset.value = helper.getProductsByOwner("user_my")
        }
    }

    override fun setRole(role: UserRole) {
        _currentRole.value = role
        val newUser = when (role) {
            UserRole.BUYER -> MockData.demoBuyerUser
            UserRole.SELLER -> MockData.demoSellerUser
            UserRole.ADMIN -> MockData.demoAdminUser
        }
        _currentUser.value = newUser
        _wallet.value = newUser.wallet
        _sheTrustProfile.value = newUser.trustProfile

        val helper = dbHelper
        if (helper != null) {
            val userItems = helper.getProductsByOwner(newUser.id)
            if (userItems.isNotEmpty()) {
                _myCloset.value = userItems
            } else if (role == UserRole.SELLER) {
                _myCloset.value = helper.getProductsByOwner("seller_linh").ifEmpty {
                    MockData.products.filter { it.seller.id == "seller_linh" } + MockData.myClosetInitial
                }
            } else {
                _myCloset.value = helper.getProductsByOwner("user_my").ifEmpty {
                    MockData.myClosetInitial
                }
            }
        } else {
            if (role == UserRole.SELLER) {
                _myCloset.value = MockData.products.filter { it.seller.id == "seller_linh" } + MockData.myClosetInitial
            } else {
                _myCloset.value = MockData.myClosetInitial
            }
        }

        showToast("Đã chuyển sang: ${role.icon} ${newUser.name}")
    }

    // Flow H & Flow C: Chat & Anti-Scam filter
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                id = "msg_01",
                senderId = "seller_01",
                senderName = "Vintage Chic Boutique",
                text = "Chào bạn! Chiếc áo này là hàng tuyển Nhật còn mới 99% nguyên tag nhé.",
                timestamp = "10:15",
                isFromMe = false
            ),
            ChatMessage(
                id = "msg_02",
                senderId = "user_me",
                senderName = "Hoàng My",
                text = "Dạ áo này ngực 86cm mặc vừa không shop?",
                timestamp = "10:17",
                isFromMe = true
            ),
            ChatMessage(
                id = "msg_03",
                senderId = "seller_01",
                senderName = "Vintage Chic Boutique",
                text = "Vừa đẹp luôn nha bạn ơi! Áo co giãn nhẹ dáng cực tôn dáng.",
                timestamp = "10:18",
                isFromMe = false
            )
        )
    )
    override val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _currentUser = MutableStateFlow(MockData.initialUser)
    override val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Personal Product Ratings & Notes
    private val _productRatingsAndNotes = MutableStateFlow<Map<String, Pair<Float, String>>>(
        mapOf(
            "p1" to Pair(4.5f, "Form áo vừa vặn, màu be phối đồ vintage rất đẹp."),
            "p2" to Pair(5.0f, "Chất dạ tweed dày dặn, cúc ngọc trai sang chảnh.")
        )
    )
    override val productRatingsAndNotes: StateFlow<Map<String, Pair<Float, String>>> = _productRatingsAndNotes.asStateFlow()

    // Flow G: Escrow & Wallet State
    private val _wallet = MutableStateFlow(
        Wallet(
            availableBalance = 1850000L,
            escrowHoldBalance = 420000L,
            pendingWithdrawal = 0L,
            transactions = listOf(
                WalletTransaction(
                    id = "tx_01",
                    type = WalletTransactionType.RELEASE_EARNING,
                    amount = 304000L,
                    title = "Doanh thu bán đồ (Đã trừ 5% phí sàn)",
                    description = "Đơn hàng #SW-20261014-002 đã hoàn tất bảo hiểm 48h",
                    timestamp = "14/10/2026 18:00",
                    isPositive = true,
                    orderCode = "SW-20261014-002"
                ),
                WalletTransaction(
                    id = "tx_02",
                    type = WalletTransactionType.ESCROW_HOLD,
                    amount = 420000L,
                    title = "Ký quỹ tạm giữ mua hàng",
                    description = "Đang tạm giữ bảo vệ đơn #SW-20261015-881 trong 48h",
                    timestamp = "15/10/2026 09:30",
                    isPositive = false,
                    orderCode = "SW-20261015-881"
                ),
                WalletTransaction(
                    id = "tx_03",
                    type = WalletTransactionType.WITHDRAW_BANK,
                    amount = 1200000L,
                    title = "Rút tiền về Vietcombank (*9812)",
                    description = "Lệnh rút tiền về tài khoản ngân hàng thành công",
                    timestamp = "12/10/2026 14:15",
                    isPositive = false
                )
            )
        )
    )
    override val wallet: StateFlow<Wallet> = _wallet.asStateFlow()

    // Flow H: She-Trust & eKYC State
    private val _sheTrustProfile = MutableStateFlow(SheTrustProfile())
    override val sheTrustProfile: StateFlow<SheTrustProfile> = _sheTrustProfile.asStateFlow()

    // Public explore products
    private val _products = MutableStateFlow(MockData.products)
    override val products: StateFlow<List<Product>> = _products.asStateFlow()

    // User's own closet (F22)
    private val _myCloset = MutableStateFlow(MockData.myClosetInitial)
    override val myCloset: StateFlow<List<Product>> = _myCloset.asStateFlow()

    // Buyer orders (F18)
    private val _orders = MutableStateFlow(MockData.initialOrders)
    override val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Seller incoming orders (F24)
    private val _sellerOrders = MutableStateFlow(MockData.initialSellerOrders)
    override val sellerOrders: StateFlow<List<Order>> = _sellerOrders.asStateFlow()

    // Direct Swap Offers (F28)
    private val _swapOffers = MutableStateFlow(MockData.initialSwapOffers)
    override val swapOffers: StateFlow<List<SwapOffer>> = _swapOffers.asStateFlow()

    // Active Swap Transactions (F29)
    private val _swapTransactions = MutableStateFlow(MockData.initialSwapTransactions)
    override val swapTransactions: StateFlow<List<SwapTransaction>> = _swapTransactions.asStateFlow()

    // Disputes and Reports (F30)
    private val _disputes = MutableStateFlow(MockData.initialDisputes)
    override val disputes: StateFlow<List<DisputeReport>> = _disputes.asStateFlow()

    private val _pendingCheckoutProduct = MutableStateFlow<Product?>(null)
    override val pendingCheckoutProduct: StateFlow<Product?> = _pendingCheckoutProduct.asStateFlow()

    private val _currentAddress = MutableStateFlow(MockData.defaultAddress)
    override val currentAddress: StateFlow<ShippingAddress> = _currentAddress.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    override val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _notifications = MutableStateFlow(MockData.notifications)
    override val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

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
    override val savedSearches: StateFlow<List<com.swapshe.app.model.SavedSearch>> = _savedSearches.asStateFlow()

    override fun addSavedSearch(query: String, style: String?, size: String?, maxPrice: Long?) {
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

    override fun removeSavedSearch(id: String) {
        _savedSearches.value = _savedSearches.value.filter { it.id != id }
        showToast("Đã xóa bộ lọc tìm kiếm.")
    }

    override fun toggleSavedSearchAlert(id: String) {
        _savedSearches.value = _savedSearches.value.map {
            if (it.id == id) it.copy(notifyNewItem = !it.notifyNewItem) else it
        }
    }

    override fun markNotificationAsRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    override fun showToast(message: String) {
        _toastMessage.value = message
    }

    override fun clearToast() {
        _toastMessage.value = null
    }

    override fun setGuestMode() {
        _currentUser.value = User(
            id = "guest_temp",
            name = "Guest Traveler",
            email = "",
            isGuest = true
        )
    }

    override fun login(email: String, name: String) {
        _currentUser.value = MockData.initialUser.copy(
            email = email,
            name = name,
            isGuest = false
        )
    }

    override fun register(email: String, name: String) {
        _currentUser.value = User(
            id = "user_${System.currentTimeMillis()}",
            name = name,
            email = email,
            isGuest = false,
            savedAddresses = listOf(MockData.defaultAddress)
        )
    }

    override fun logout() {
        setGuestMode()
    }

    override fun updatePreferences(
        styles: Set<String>?,
        size: String?,
        colors: Set<String>?,
        measurements: BodyMeasurements?,
        skinTone: String?
    ) {
        val curr = _currentUser.value
        _currentUser.value = curr.copy(
            stylePreferences = styles ?: curr.stylePreferences,
            sizePreference = size ?: curr.sizePreference,
            colorPreferences = colors ?: curr.colorPreferences,
            bodyMeasurements = measurements ?: curr.bodyMeasurements,
            skinTone = skinTone ?: curr.skinTone
        )
    }

    override fun toggleLike(productId: String) {
        _products.value = _products.value.map { product ->
            if (product.id == productId) {
                product.copy(isLiked = !product.isLiked)
            } else product
        }
    }

    override fun setPendingCheckout(product: Product?) {
        _pendingCheckoutProduct.value = product
    }

    override fun updateShippingAddress(address: ShippingAddress) {
        _currentAddress.value = address
    }

    // --- BUYER ORDER OPERATIONS ---
    override fun createOrder(product: Product, address: ShippingAddress, isCod: Boolean): Order {
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
                    if (isCod) "Đơn hàng đã ghi nhận phương thức thanh toán khi nhận hàng" else "Đã thanh toán qua Ví Ký Quỹ Escrow an toàn",
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

        // Two-way synchronization: Both Buyer and Seller lists see the order
        _orders.value = listOf(newOrder) + _orders.value
        _sellerOrders.value = listOf(newOrder) + _sellerOrders.value

        // Flow G: Escrow Hold on order payment
        val currWallet = _wallet.value
        val holdTx = WalletTransaction(
            id = "tx_hold_${System.currentTimeMillis()}",
            type = WalletTransactionType.ESCROW_HOLD,
            amount = product.price,
            title = "Ký quỹ tạm giữ đơn #${newOrder.internalCode}",
            description = "Tiền được bảo vệ trong Ví Escrow SwapShe trong 48h",
            timestamp = timeNow,
            isPositive = false,
            orderCode = newOrder.internalCode
        )
        _wallet.value = currWallet.copy(
            escrowHoldBalance = currWallet.escrowHoldBalance + product.price,
            transactions = listOf(holdTx) + currWallet.transactions
        )

        _pendingCheckoutProduct.value = null
        return newOrder
    }

    override fun confirmDelivery(orderId: String) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        var releasedAmount = 0L
        var orderCode = ""

        val timelineUpdater: (Order) -> Order = { order ->
            if (order.id == orderId) {
                orderCode = order.internalCode
                val commission = (order.subtotal * 0.05).toLong()
                releasedAmount = order.subtotal - commission

                val updatedTimeline = order.timeline + TimelineEvent(
                    "Order completed & Escrow Released",
                    "Người mua xác nhận hàng • Tiền đã chuyển vào Ví SwapShe của người bán (Đã trừ 5% phí sàn)",
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

        // Update both buyer and seller order records
        _orders.value = _orders.value.map(timelineUpdater)
        _sellerOrders.value = _sellerOrders.value.map(timelineUpdater)

        if (releasedAmount > 0) {
            val currWallet = _wallet.value
            val releaseTx = WalletTransaction(
                id = "tx_rel_${System.currentTimeMillis()}",
                type = WalletTransactionType.RELEASE_EARNING,
                amount = releasedAmount,
                title = "Doanh thu đơn $orderCode (Đã trừ 5% phí sàn)",
                description = "Ví Ký Quỹ giải ngân sau khi người mua xác nhận thành công",
                timestamp = timeNow,
                isPositive = true,
                orderCode = orderCode
            )
            _wallet.value = currWallet.copy(
                availableBalance = currWallet.availableBalance + releasedAmount,
                escrowHoldBalance = (currWallet.escrowHoldBalance - (releasedAmount * 100 / 95)).coerceAtLeast(0L),
                transactions = listOf(releaseTx) + currWallet.transactions
            )
        }

        showToast("Đã xác nhận nhận hàng! Tiền ký quỹ đã được giải ngân cho người bán.")
    }

    override fun reviewOrder(orderId: String, rating: Int, comment: String) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(review = OrderReview(rating, comment))
            } else order
        }
        _sellerOrders.value = _sellerOrders.value.map { order ->
            if (order.id == orderId) {
                order.copy(review = OrderReview(rating, comment))
            } else order
        }
        showToast("Cảm ơn bạn đã gửi đánh giá!")
    }

    override fun saveProductRatingAndNotes(productId: String, rating: Float, notes: String) {
        _productRatingsAndNotes.value = _productRatingsAndNotes.value + (productId to Pair(rating, notes))
        showToast("Đã lưu đánh giá & ghi chú thành công!")
    }

    // --- SELLER CLOSET (F22 - F25) ---
    override fun saveListing(product: Product, isPublish: Boolean) {
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

        // Persist to SQLite Database (Create / Update)
        dbHelper?.insertProduct(updatedProduct)

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
                    type = "saved_search_match",
                    targetProductId = updatedProduct.id
                )
                _notifications.value = listOf(newNotif) + _notifications.value
            }
        } else {
            showToast("Đã lưu bản nháp vào tủ đồ của bạn.")
        }
    }

    override fun deleteDraft(productId: String) {
        // Delete from SQLite Database (Delete CRUD)
        dbHelper?.deleteProduct(productId)
        _myCloset.value = _myCloset.value.filter { it.id != productId }
        showToast("Đã xóa món đồ khỏi tủ đồ.")
    }

    override fun updateSellerOrderStatus(orderId: String, nextStatus: OrderStatus) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val statusMapper: (Order) -> Order = { order ->
            if (order.id == orderId) {
                val desc = when (nextStatus) {
                    OrderStatus.PREPARING -> "Người bán đã xác nhận chuẩn bị gói hàng"
                    OrderStatus.SHIPPED -> "Người bán đã bàn giao gói hàng cho bưu tá GHN"
                    OrderStatus.DELIVERED -> "Bưu tá đã giao hàng thành công đến người mua (Bắt đầu 48h bảo hiểm)"
                    OrderStatus.COMPLETED -> "Đơn hàng đã hoàn tất giao dịch"
                    OrderStatus.CANCELLED -> "Đơn hàng đã bị hủy"
                    else -> "Cập nhật trạng thái"
                }
                val newEvent = TimelineEvent(nextStatus.label, desc, timeNow, true)
                order.copy(status = nextStatus, timeline = order.timeline + newEvent)
            } else order
        }

        // Synchronize both sellerOrders and buyer orders
        _sellerOrders.value = _sellerOrders.value.map(statusMapper)
        _orders.value = _orders.value.map(statusMapper)
        showToast("Đã cập nhật trạng thái đơn hàng: ${nextStatus.label}")
    }

    // --- DIRECT SWAP OPERATIONS (F26 - F29) ---
    override fun sendSwapOffer(requestedProduct: Product, offeredProduct: Product, note: String) {
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
        showToast("Đã gửi đề nghị trao đổi 1-1! Chờ người bán phản hồi nhé ✨")
    }

    override fun acceptSwapOffer(offerId: String): String? {
        val offer = _swapOffers.value.find { it.id == offerId } ?: return null

        // Check availability: only block if explicitly SOLD
        val isRequestedSold = _products.value.find { it.id == offer.requestedProduct.id }?.status == ProductStatus.SOLD
        val isOfferedSold = _products.value.find { it.id == offer.offeredProduct.id }?.status == ProductStatus.SOLD

        if (isRequestedSold || isOfferedSold) {
            _swapOffers.value = _swapOffers.value.map {
                if (it.id == offerId) it.copy(status = SwapOfferStatus.CONFLICT) else it
            }
            showToast("Rất tiếc! Một trong hai món đồ đã được bán trước đó.")
            return null
        }

        // Lock both products atomically as RESERVED for Swap
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
            userBId = offer.recipientId,
            userBName = offer.recipientName,
            productA = offer.offeredProduct,
            productB = offer.requestedProduct,
            shipmentAtoB = TwoWayShipment(
                id = "ship_${System.currentTimeMillis()}_A2B",
                fromUserId = offer.senderId,
                toUserId = offer.recipientId,
                trackingCodeDemo = "GHN-SWAP-A2B-${(1000..9999).random()}"
            ),
            shipmentBtoA = TwoWayShipment(
                id = "ship_${System.currentTimeMillis()}_B2A",
                fromUserId = offer.recipientId,
                toUserId = offer.senderId,
                trackingCodeDemo = "GHN-SWAP-B2A-${(1000..9999).random()}"
            ),
            status = SwapTransactionStatus.AWAITING_DISPATCH,
            createdAt = timeNow,
            isEscrowDepositedA = false,
            isEscrowDepositedB = false,
            escrowDepositAmount = 50_000L,
            timeline = listOf(
                TimelineEvent("Đề nghị trao đổi được chấp thuận", "Cả 2 bên chuẩn bị đặt cọc bảo đảm Escrow 50.000đ và gửi hàng", timeNow, true)
            )
        )

        _swapTransactions.value = listOf(transaction) + _swapTransactions.value
        _swapOffers.value = _swapOffers.value.map {
            if (it.id == offerId) it.copy(status = SwapOfferStatus.ACCEPTED, swapTransactionId = txId) else it
        }
        showToast("Đã chấp nhận đề nghị đổi đồ! Giao dịch 2 chiều đã bắt đầu.")
        return txId
    }

    override fun depositSwapEscrow(transactionId: String, isUserA: Boolean) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _swapTransactions.value = _swapTransactions.value.map { tx ->
            if (tx.id == transactionId) {
                val updatedA = if (isUserA) true else tx.isEscrowDepositedA
                val updatedB = if (!isUserA) true else tx.isEscrowDepositedB
                val bothDone = updatedA && updatedB
                tx.copy(
                    isEscrowDepositedA = updatedA,
                    isEscrowDepositedB = updatedB,
                    timeline = tx.timeline + TimelineEvent(
                        if (isUserA) "${tx.userAName} đã cọc bảo đảm Escrow 50.000đ" else "Bạn đã cọc bảo đảm Escrow 50.000đ",
                        if (bothDone) "Hai bên đã cọc Escrow 100%! Tiến hành bàn giao bưu tá." else "Chờ bên còn lại hoàn tất ký quỹ.",
                        timeNow,
                        true
                    )
                )
            } else tx
        }
        showToast("✓ Đã ký quỹ cọc Escrow 50.000đ thành công!")
    }

    override fun rejectSwapOffer(offerId: String) {
        _swapOffers.value = _swapOffers.value.map {
            if (it.id == offerId) it.copy(status = SwapOfferStatus.REJECTED) else it
        }
        showToast("Đã từ chối lời mời đổi đồ.")
    }

    override fun cancelSwapOffer(offerId: String) {
        _swapOffers.value = _swapOffers.value.map {
            if (it.id == offerId) it.copy(status = SwapOfferStatus.CANCELLED) else it
        }
        showToast("Đã hủy lời mời đổi đồ.")
    }

    override fun dispatchSwapShipment(transactionId: String, isUserA: Boolean) {
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

    override fun confirmSwapReceived(transactionId: String, isUserA: Boolean) {
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
    override fun submitDispute(targetType: DisputeTargetType, targetId: String, targetTitle: String, reason: String, description: String): DisputeReport {
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

    // Extended Dispute with Media proof and Escrow Freeze
    override fun submitDisputeWithMedia(
        targetType: DisputeTargetType,
        targetId: String,
        targetTitle: String,
        reason: String,
        description: String,
        evidencePhotos: List<String>,
        evidenceVideoUrl: String?,
        requestedAmount: Long?
    ): DisputeReport {
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
            status = DisputeStatus.UNDER_REVIEW,
            createdAt = timeNow,
            evidencePhotos = evidencePhotos,
            evidenceVideoUrl = evidenceVideoUrl,
            requestedRefundAmount = requestedAmount,
            isEscrowFrozen = true
        )

        _disputes.value = listOf(newDispute) + _disputes.value

        // Freeze Order and Escrow
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

        showToast("🛡️ Đã gửi khiếu nại & ĐÓNG BĂNG tiền Ký Quỹ Escrow! Người bán có 24h để thương lượng.")
        return newDispute
    }

    override fun sellerRespondDispute(disputeId: String, offerType: DisputeOfferType, partialAmount: Long?, message: String) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _disputes.value = _disputes.value.map { d ->
            if (d.id == disputeId) {
                d.copy(
                    sellerOffer = DisputeOffer(
                        type = offerType,
                        amount = partialAmount,
                        note = message,
                        timestamp = timeNow
                    )
                )
            } else d
        }
        showToast("Người bán đã gửi phản hồi thương lượng cho khiếu nại.")
    }

    override fun buyerAcceptDisputeOffer(disputeId: String) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _disputes.value = _disputes.value.map { d ->
            if (d.id == disputeId) {
                val refund = d.sellerOffer?.amount ?: d.requestedRefundAmount ?: 100000L
                val currWallet = _wallet.value
                val refundTx = WalletTransaction(
                    id = "tx_rf_${System.currentTimeMillis()}",
                    type = WalletTransactionType.DISPUTE_REFUND,
                    amount = refund,
                    title = "Hoàn tiền khiếu nại thành công",
                    description = "Hai bên đã đồng thuận phương án: ${d.sellerOffer?.type?.label}",
                    timestamp = timeNow,
                    isPositive = true
                )
                _wallet.value = currWallet.copy(
                    availableBalance = currWallet.availableBalance + refund,
                    escrowHoldBalance = (currWallet.escrowHoldBalance - refund).coerceAtLeast(0L),
                    transactions = listOf(refundTx) + currWallet.transactions
                )
                d.copy(
                    status = DisputeStatus.RESOLVED,
                    resolutionNote = "Hai bên đã đồng thuận giải quyết khiếu nại."
                )
            } else d
        }
        showToast("Đã chấp thuận giải pháp! Tiền đã được hoàn về Ví SwapShe của bạn.")
    }

    override fun escalateDisputeToAdmin(disputeId: String) {
        _disputes.value = _disputes.value.map { d ->
            if (d.id == disputeId) {
                d.copy(
                    status = DisputeStatus.UNDER_REVIEW,
                    resolutionNote = "Đã chuyển hồ sơ cho SwapShe Admin phân xử dựa trên video đóng gói & unbox."
                )
            } else d
        }
        showToast("Đã gửi yêu cầu Admin SwapShe đứng ra phân xử!")
    }

    override fun adminResolveDispute(disputeId: String, returnApproved: Boolean) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _disputes.value = _disputes.value.map { d ->
            if (d.id == disputeId) {
                if (returnApproved) {
                    val returnCode = "GHN-RETURN-${(100000..999999).random()}"
                    d.copy(
                        status = DisputeStatus.RESOLVED,
                        adminVerdict = "SwapShe Admin phán quyết: Chấp nhận trả hàng. Mã vận đơn hoàn: $returnCode.",
                        returnTrackingCode = returnCode,
                        resolutionNote = "Admin phê duyệt hoàn tiền sau khi người bán nhận lại hàng."
                    )
                } else {
                    d.copy(
                        status = DisputeStatus.REJECTED,
                        adminVerdict = "SwapShe Admin phán quyết: Bằng chứng sản phẩm đúng mô tả ban đầu của người bán. Đơn hàng tiếp tục giải ngân.",
                        resolutionNote = "Admin bác bỏ khiếu nại."
                    )
                }
            } else d
        }
        showToast("Admin đã đưa ra phán quyết giải quyết tranh chấp.")
    }

    override fun sellerRejectDispute(disputeId: String, reason: String) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _disputes.value = _disputes.value.map { d ->
            if (d.id == disputeId) {
                d.copy(
                    sellerOffer = DisputeOffer(
                        type = DisputeOfferType.REJECT,
                        amount = 0L,
                        note = reason,
                        timestamp = timeNow
                    ),
                    resolutionNote = "Người bán từ chối khiếu nại. Chuyển hồ sơ cho SwapShe Admin xử lý."
                )
            } else d
        }
        showToast("Người bán đã từ chối khiếu nại. Đã chuyển hồ sơ cho SwapShe Admin.")
    }

    override fun sellerConfirmReturnReceived(disputeId: String) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _disputes.value = _disputes.value.map { d ->
            if (d.id == disputeId) {
                val refund = d.requestedRefundAmount ?: 420000L
                val currWallet = _wallet.value
                val refundTx = WalletTransaction(
                    id = "tx_ret_${System.currentTimeMillis()}",
                    type = WalletTransactionType.DISPUTE_REFUND,
                    amount = refund,
                    title = "Hoàn tiền 100% sau khi trả hàng",
                    description = "Người bán đã nhận lại kiện hàng trả • Ví Escrow hoàn tiền cho Buyer",
                    timestamp = timeNow,
                    isPositive = true
                )
                _wallet.value = currWallet.copy(
                    availableBalance = currWallet.availableBalance + refund,
                    escrowHoldBalance = (currWallet.escrowHoldBalance - refund).coerceAtLeast(0L),
                    transactions = listOf(refundTx) + currWallet.transactions
                )
                d.copy(
                    status = DisputeStatus.RESOLVED,
                    isSellerReturnConfirmed = true,
                    resolutionNote = "Người bán xác nhận đã nhận lại hàng. Hoàn tất hoàn tiền 100% qua Escrow."
                )
            } else d
        }
        showToast("✓ Người bán đã xác nhận nhận lại hàng! Tiền ký quỹ đã hoàn về ví Buyer.")
    }

    // Flow G: Payout Withdrawal
    override fun withdrawFunds(amount: Long, bank: BankPayoutAccount): Boolean {
        val curr = _wallet.value
        if (amount <= 0 || amount > curr.availableBalance) {
            showToast("Số dư khả dụng không đủ để rút số tiền này.")
            return false
        }
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val newTx = WalletTransaction(
            id = "tx_w_${System.currentTimeMillis()}",
            type = WalletTransactionType.WITHDRAW_BANK,
            amount = amount,
            title = "Rút tiền về ${bank.bankName} (*${bank.accountNumber.takeLast(4)})",
            description = "Lệnh rút tiền đang được ngân hàng đối soát chuyển khoản",
            timestamp = timeNow,
            isPositive = false
        )
        _wallet.value = curr.copy(
            availableBalance = curr.availableBalance - amount,
            bankAccount = bank,
            transactions = listOf(newTx) + curr.transactions
        )
        showToast("Đã tạo lệnh rút tiền về tài khoản ngân hàng thành công!")
        return true
    }

    // Flow H: eKYC & Trust Score
    override fun submitEkyc(doc: EkycDocument) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val updatedProfile = _sheTrustProfile.value.copy(
            status = EkycStatus.VERIFIED,
            score = (_sheTrustProfile.value.score + 10).coerceAtMost(100),
            badge = TrustBadgeType.QUEEN_CLOSET,
            document = doc.copy(verifiedDate = timeNow)
        )
        _sheTrustProfile.value = updatedProfile
        _currentUser.value = _currentUser.value.copy(
            trustProfile = updatedProfile
        )
        showToast("🎉 Định danh eKYC thành công! Đạt huy hiệu Queen Closet 👑 & Điểm tín nhiệm 98/100")
    }

    // Flow E: Fit Score calculation
    override fun calculateFitScore(user: User, product: Product): Int {
        var score = 86
        if (user.sizePreference != null && user.sizePreference.equals(product.size, ignoreCase = true)) {
            score += 5
        }
        if (user.stylePreferences.any { it.equals(product.style, ignoreCase = true) }) {
            score += 4
        }
        val userBust = user.bodyMeasurements?.bustCm
        val prodBust = product.measurements.bust
        if (userBust != null && prodBust != null) {
            val diff = Math.abs(userBust - prodBust)
            if (diff <= 3) score += 4
        }
        return score.coerceIn(75, 99)
    }

    // Flow H: Anti-scam keywords detection & Chat send
    override fun checkMessageForOutPlatformScam(text: String): String? {
        val lower = text.lowercase()
        val riskKeywords = listOf("chuyển khoản riêng", "stk", "zalo", "ngân hàng riêng", "chuyển riêng", "ck riêng", "số tài khoản", "09", "03", "07", "08")
        for (k in riskKeywords) {
            if (lower.contains(k)) {
                return "⚠️ CẢNH BÁO AN TOÀN SWAPSHE: Phát hiện dấu hiệu chia sẻ liên lạc/chuyển khoản ngoài sàn! Mọi giao dịch ngoài Ví Escrow sẽ KHÔNG ĐƯỢC BẢO VỆ và có nguy cơ bị khóa tài khoản."
            }
        }
        return null
    }

    override fun sendChatMessage(text: String, recipientName: String): ChatMessage {
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val scamWarning = checkMessageForOutPlatformScam(text)
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderId = _currentUser.value.id,
            senderName = _currentUser.value.name,
            text = text,
            timestamp = timeNow,
            isFromMe = true,
            isWarning = scamWarning != null,
            warningText = scamWarning
        )
        _chatMessages.value = _chatMessages.value + newMsg
        if (scamWarning != null) {
            showToast("⚠️ Phát hiện từ khóa nhạy cảm ngoài sàn!")
        }
        return newMsg
    }

    // Helper for testing order status transitions (Syncs both Buyer and Seller)
    override fun advanceOrderStatus(orderId: String) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        var nextStatusToToast: OrderStatus? = null

        val mapper: (Order) -> Order = { order ->
            if (order.id == orderId) {
                val nextStatus = when (order.status) {
                    OrderStatus.PLACED -> OrderStatus.PAID
                    OrderStatus.PAID -> OrderStatus.PREPARING
                    OrderStatus.PREPARING -> OrderStatus.SHIPPED
                    OrderStatus.SHIPPED -> OrderStatus.DELIVERED
                    OrderStatus.DELIVERED -> OrderStatus.COMPLETED
                    else -> order.status
                }
                nextStatusToToast = nextStatus
                val newEvent = TimelineEvent(nextStatus.label, "Mô phỏng chuyển bước giao vận: ${nextStatus.label}", timeNow, true)
                order.copy(status = nextStatus, timeline = order.timeline + newEvent)
            } else order
        }

        _orders.value = _orders.value.map(mapper)
        _sellerOrders.value = _sellerOrders.value.map(mapper)
        nextStatusToToast?.let {
            showToast("⚡ [Dev Simulator] Đơn chuyển sang: ${it.label}")
        }
    }

    // Flow D: Admin Dispute Arbitration Decision
    override fun resolveDisputeByAdmin(disputeId: String, refundToBuyer: Boolean, note: String) {
        val timeNow = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val dispute = _disputes.value.find { it.id == disputeId } ?: return

        _disputes.value = _disputes.value.map { d ->
            if (d.id == disputeId) {
                d.copy(
                    status = DisputeStatus.RESOLVED,
                    resolutionNote = if (refundToBuyer)
                        "Trọng tài SwapShe phán quyết: Chấp thuận khiếu nại. Hoàn tiền 100% (${d.requestedAmount}đ) cho Người mua. $note"
                    else
                        "Trọng tài SwapShe phán quyết: Bác khiếu nại. Giải ngân doanh thu cho Người bán. $note"
                )
            } else d
        }

        if (refundToBuyer) {
            // Escrow hoàn 100% tiền tạm giữ cho Buyer
            _wallet.value = _wallet.value.copy(
                availableBalance = _wallet.value.availableBalance + dispute.requestedAmount,
                escrowHoldBalance = maxOf(0L, _wallet.value.escrowHoldBalance - dispute.requestedAmount),
                transactions = listOf(
                    WalletTransaction(
                        id = "tx_admin_refund_${System.currentTimeMillis()}",
                        type = WalletTransactionType.DISPUTE_REFUND,
                        amount = dispute.requestedAmount,
                        title = "Trọng tài hoàn tiền khiếu nại 100%",
                        description = "Phán quyết trọng tài cho ${dispute.targetTitle}",
                        timestamp = timeNow,
                        isPositive = true,
                        orderCode = dispute.targetId
                    )
                ) + _wallet.value.transactions
            )
            showToast("⚖️ Trọng tài đã phán quyết: Hoàn 100% tiền Escrow cho Người mua!")
        } else {
            showToast("⚖️ Trọng tài đã phán quyết: Bác khiếu nại & Giải ngân cho Người bán!")
        }
    }

    companion object {
        val instance by lazy { SwapSheRepository() }
    }
}
