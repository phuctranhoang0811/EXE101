package com.swapshe.app.data

import android.content.Context
import com.swapshe.app.model.AppNotification
import com.swapshe.app.model.BankPayoutAccount
import com.swapshe.app.model.BodyMeasurements
import com.swapshe.app.model.ChatMessage
import com.swapshe.app.model.DisputeOfferType
import com.swapshe.app.model.DisputeReport
import com.swapshe.app.model.DisputeTargetType
import com.swapshe.app.model.EkycDocument
import com.swapshe.app.model.Order
import com.swapshe.app.model.OrderStatus
import com.swapshe.app.model.Product
import com.swapshe.app.model.SavedSearch
import com.swapshe.app.model.SheTrustProfile
import com.swapshe.app.model.ShippingAddress
import com.swapshe.app.model.SwapOffer
import com.swapshe.app.model.SwapTransaction
import com.swapshe.app.model.User
import com.swapshe.app.model.UserRole
import com.swapshe.app.model.Wallet
import kotlinx.coroutines.flow.StateFlow

interface ISwapSheRepository {
    val currentRole: StateFlow<UserRole>
    val currentUser: StateFlow<User>
    val chatMessages: StateFlow<List<ChatMessage>>
    val productRatingsAndNotes: StateFlow<Map<String, Pair<Float, String>>>
    val wallet: StateFlow<Wallet>
    val sheTrustProfile: StateFlow<SheTrustProfile>
    val products: StateFlow<List<Product>>
    val myCloset: StateFlow<List<Product>>
    val orders: StateFlow<List<Order>>
    val sellerOrders: StateFlow<List<Order>>
    val swapOffers: StateFlow<List<SwapOffer>>
    val swapTransactions: StateFlow<List<SwapTransaction>>
    val disputes: StateFlow<List<DisputeReport>>
    val pendingCheckoutProduct: StateFlow<Product?>
    val currentAddress: StateFlow<ShippingAddress>
    val toastMessage: StateFlow<String?>
    val notifications: StateFlow<List<AppNotification>>
    val savedSearches: StateFlow<List<SavedSearch>>

    fun initDatabase(context: Context)
    fun setRole(role: UserRole)
    fun addSavedSearch(query: String, style: String?, size: String?, maxPrice: Long?)
    fun removeSavedSearch(id: String)
    fun toggleSavedSearchAlert(id: String)
    fun markNotificationAsRead(id: String)
    fun showToast(message: String)
    fun clearToast()
    fun setGuestMode()
    fun login(email: String, name: String = "Hoàng My")
    fun register(email: String, name: String)
    fun logout()
    fun updatePreferences(
        styles: Set<String>? = null,
        size: String? = null,
        colors: Set<String>? = null,
        measurements: BodyMeasurements? = null,
        skinTone: String? = null
    )
    fun toggleLike(productId: String)
    fun setPendingCheckout(product: Product?)
    fun updateShippingAddress(address: ShippingAddress)
    fun confirmDelivery(orderId: String)
    fun acceptSwapOffer(offerId: String): String?
    fun createOrder(product: Product, address: ShippingAddress, isCod: Boolean = false): Order
    fun reviewOrder(orderId: String, rating: Int, comment: String)
    fun saveProductRatingAndNotes(productId: String, rating: Float, notes: String)
    fun saveListing(product: Product, isPublish: Boolean)
    fun deleteDraft(productId: String)
    fun updateSellerOrderStatus(orderId: String, nextStatus: OrderStatus)
    fun sendSwapOffer(requestedProduct: Product, offeredProduct: Product, note: String)
    fun depositSwapEscrow(transactionId: String, isUserA: Boolean)
    fun rejectSwapOffer(offerId: String)
    fun cancelSwapOffer(offerId: String)
    fun dispatchSwapShipment(transactionId: String, isUserA: Boolean)
    fun confirmSwapReceived(transactionId: String, isUserA: Boolean)
    fun submitDispute(targetType: DisputeTargetType, targetId: String, targetTitle: String, reason: String, description: String): DisputeReport
    fun submitDisputeWithMedia(
        targetType: DisputeTargetType,
        targetId: String,
        targetTitle: String,
        reason: String,
        description: String,
        evidencePhotos: List<String> = emptyList(),
        evidenceVideoUrl: String? = null,
        requestedAmount: Long? = null
    ): DisputeReport
    fun sellerRespondDispute(disputeId: String, offerType: DisputeOfferType, partialAmount: Long?, message: String)
    fun buyerAcceptDisputeOffer(disputeId: String)
    fun escalateDisputeToAdmin(disputeId: String)
    fun adminResolveDispute(disputeId: String, returnApproved: Boolean)
    fun sellerRejectDispute(disputeId: String, reason: String = "Sản phẩm gửi đi hoàn toàn nguyên vẹn đúng mô tả")
    fun sellerConfirmReturnReceived(disputeId: String)
    fun withdrawFunds(amount: Long, bank: BankPayoutAccount): Boolean
    fun submitEkyc(doc: EkycDocument)
    fun calculateFitScore(user: User, product: Product): Int
    fun checkMessageForOutPlatformScam(text: String): String?
    fun sendChatMessage(text: String, recipientName: String = "Người bán"): ChatMessage
    fun advanceOrderStatus(orderId: String)
    fun resolveDisputeByAdmin(disputeId: String, refundToBuyer: Boolean, note: String = "")
}
