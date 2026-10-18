package com.swapshe.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.di.AppContainer
import com.swapshe.app.model.Product
import com.swapshe.app.model.SavedSearch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WishlistViewModel(
    private val repository: ISwapSheRepository = AppContainer.repository
) : ViewModel() {

    val savedSearches: StateFlow<List<SavedSearch>> = repository.savedSearches

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    val wishlistItems: StateFlow<List<Product>> = repository.products
        .map { products -> products.filter { it.isLiked } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onTabSelect(index: Int) {
        _selectedTab.value = index
    }

    fun toggleLike(productId: String) {
        repository.toggleLike(productId)
    }

    fun toggleSavedSearchAlert(searchId: String) {
        repository.toggleSavedSearchAlert(searchId)
    }

    fun removeSavedSearch(searchId: String) {
        repository.removeSavedSearch(searchId)
    }

    companion object {
        fun provideFactory(
            repository: ISwapSheRepository = AppContainer.repository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WishlistViewModel(repository) as T
            }
        }
    }
}
