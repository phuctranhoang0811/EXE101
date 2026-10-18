package com.swapshe.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.data.MockData
import com.swapshe.app.di.AppContainer
import com.swapshe.app.model.Order
import com.swapshe.app.model.Product
import com.swapshe.app.model.ShippingAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val repository: ISwapSheRepository = AppContainer.repository
) : ViewModel() {

    val product: StateFlow<Product> = repository.pendingCheckoutProduct
        .map { it ?: MockData.products.first() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MockData.products.first()
        )

    val currentAddress: StateFlow<ShippingAddress> = repository.currentAddress

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _failureError = MutableStateFlow<String?>(null)
    val failureError: StateFlow<String?> = _failureError.asStateFlow()

    fun updateAddress(
        fullName: String,
        phone: String,
        province: String,
        ward: String,
        detailAddress: String
    ): Boolean {
        if (fullName.isBlank() || phone.isBlank() || province.isBlank() || detailAddress.isBlank()) {
            return false
        }
        val newAddress = ShippingAddress(
            fullName = fullName,
            phone = phone,
            province = province,
            ward = ward,
            detailAddress = detailAddress
        )
        repository.updateShippingAddress(newAddress)
        return true
    }

    fun simulatePaymentSuccess(onSuccess: (orderId: String) -> Unit) {
        _isProcessing.value = true
        _failureError.value = null
        viewModelScope.launch {
            val createdOrder = repository.createOrder(
                product = product.value,
                address = currentAddress.value
            )
            _isProcessing.value = false
            onSuccess(createdOrder.id)
        }
    }

    fun simulatePaymentFailure(reason: String) {
        _isProcessing.value = false
        _failureError.value = reason
    }

    fun clearError() {
        _failureError.value = null
    }

    companion object {
        fun provideFactory(
            repository: ISwapSheRepository = AppContainer.repository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CheckoutViewModel(repository) as T
            }
        }
    }
}
