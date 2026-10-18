package com.swapshe.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.di.AppContainer
import com.swapshe.app.model.Order
import com.swapshe.app.model.Product
import com.swapshe.app.model.ProductStatus
import com.swapshe.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MyClosetViewModel(
    private val repository: ISwapSheRepository = AppContainer.repository
) : ViewModel() {

    val user: StateFlow<User> = repository.currentUser
    val closetProducts: StateFlow<List<Product>> = repository.myCloset
    val sellerOrders: StateFlow<List<Order>> = repository.sellerOrders

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    private val _showEkycModal = MutableStateFlow(false)
    val showEkycModal: StateFlow<Boolean> = _showEkycModal.asStateFlow()

    val filteredList: StateFlow<List<Product>> = combine(
        closetProducts,
        _selectedTabIndex
    ) { products, tabIndex ->
        when (tabIndex) {
            1 -> products.filter { it.status == ProductStatus.AVAILABLE }
            2 -> products.filter { it.status == ProductStatus.RESERVED }
            3 -> products.filter { it.status == ProductStatus.SOLD }
            4 -> products.filter { it.status == ProductStatus.DRAFT }
            else -> products
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onTabSelect(tabIndex: Int) {
        _selectedTabIndex.value = tabIndex
    }

    fun setShowEkycModal(show: Boolean) {
        _showEkycModal.value = show
    }

    fun deleteProduct(productId: String) {
        repository.deleteDraft(productId)
    }

    companion object {
        fun provideFactory(
            repository: ISwapSheRepository = AppContainer.repository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MyClosetViewModel(repository) as T
            }
        }
    }
}
