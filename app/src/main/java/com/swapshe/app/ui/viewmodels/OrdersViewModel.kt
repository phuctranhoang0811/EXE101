package com.swapshe.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.di.AppContainer
import com.swapshe.app.model.Order
import com.swapshe.app.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class OrdersViewModel(
    private val repository: ISwapSheRepository = AppContainer.repository
) : ViewModel() {

    val orders: StateFlow<List<Order>> = repository.orders

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _showReviewDialog = MutableStateFlow(false)
    val showReviewDialog: StateFlow<Boolean> = _showReviewDialog.asStateFlow()

    private val _reviewRating = MutableStateFlow(5)
    val reviewRating: StateFlow<Int> = _reviewRating.asStateFlow()

    private val _reviewComment = MutableStateFlow("")
    val reviewComment: StateFlow<String> = _reviewComment.asStateFlow()

    val filteredOrders: StateFlow<List<Order>> = combine(
        orders,
        _selectedTab
    ) { orderList, tabIndex ->
        when (tabIndex) {
            1 -> orderList.filter {
                it.status == OrderStatus.PAID ||
                        it.status == OrderStatus.PREPARING ||
                        it.status == OrderStatus.SHIPPED ||
                        it.status == OrderStatus.DELIVERED
            }
            2 -> orderList.filter { it.status == OrderStatus.COMPLETED }
            else -> orderList
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onTabSelect(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    fun openReviewDialog(initialRating: Int = 5, initialComment: String = "") {
        _reviewRating.value = initialRating
        _reviewComment.value = initialComment
        _showReviewDialog.value = true
    }

    fun closeReviewDialog() {
        _showReviewDialog.value = false
    }

    fun submitReview(orderId: String, rating: Float, comment: String) {
        val clampedRating = rating.toInt().coerceIn(1, 5)
        _reviewRating.value = clampedRating
        _reviewComment.value = comment
        repository.reviewOrder(orderId, clampedRating, comment)
        _showReviewDialog.value = false
    }

    fun confirmDelivery(orderId: String) {
        repository.confirmDelivery(orderId)
    }

    fun advanceOrderStatus(orderId: String) {
        repository.advanceOrderStatus(orderId)
    }

    companion object {
        fun provideFactory(
            repository: ISwapSheRepository = AppContainer.repository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OrdersViewModel(repository) as T
            }
        }
    }
}
