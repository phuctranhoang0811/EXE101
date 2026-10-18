package com.swapshe.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.di.AppContainer
import com.swapshe.app.model.Product
import com.swapshe.app.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProductDetailViewModel(
    val productId: String,
    private val repository: ISwapSheRepository = AppContainer.repository
) : ViewModel() {

    val user: StateFlow<User> = repository.currentUser
    val products: StateFlow<List<Product>> = repository.products

    val product: StateFlow<Product?> = repository.products
        .map { list -> list.find { it.id == productId } ?: list.firstOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val productRatingsAndNotes: StateFlow<Map<String, Pair<Float, String>>> =
        repository.productRatingsAndNotes

    fun toggleLike(targetProductId: String) {
        repository.toggleLike(targetProductId)
    }

    fun saveProductRatingAndNotes(targetProductId: String, rating: Float, notes: String) {
        repository.saveProductRatingAndNotes(targetProductId, rating, notes)
    }

    fun prepareCheckout(targetProduct: Product) {
        repository.setPendingCheckout(targetProduct)
    }

    fun showToast(message: String) {
        repository.showToast(message)
    }

    fun calculateFitScore(currentUser: User, targetProduct: Product): Int {
        return repository.calculateFitScore(currentUser, targetProduct)
    }

    companion object {
        fun provideFactory(
            productId: String,
            repository: ISwapSheRepository = AppContainer.repository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductDetailViewModel(productId, repository) as T
            }
        }
    }
}
