package com.swapshe.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.di.AppContainer
import com.swapshe.app.model.ConditionType
import com.swapshe.app.model.Product
import com.swapshe.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ExploreUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val sortOrder: String = "newest", // "newest", "price_asc", "price_desc"
    val isFilterSheetOpen: Boolean = false,
    val filterSize: String? = null,
    val filterCondition: ConditionType? = null,
    val filterSwapOnly: Boolean = false
)

class ExploreViewModel(
    initialCategory: String? = null,
    private val repository: ISwapSheRepository = AppContainer.repository
) : ViewModel() {

    val user: StateFlow<User> = repository.currentUser

    private val _uiState = MutableStateFlow(
        ExploreUiState(selectedCategory = initialCategory ?: "All")
    )
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    val resultList: StateFlow<List<Product>> = combine(
        repository.products,
        _uiState
    ) { products, state ->
        val filtered = products.filter { product ->
            val matchesQuery = state.searchQuery.isBlank() ||
                    product.title.contains(state.searchQuery, ignoreCase = true) ||
                    product.brand.contains(state.searchQuery, ignoreCase = true) ||
                    product.style.contains(state.searchQuery, ignoreCase = true)

            val matchesCategory = state.selectedCategory == "All" ||
                    product.category.equals(state.selectedCategory, ignoreCase = true)
            val matchesSize = state.filterSize == null ||
                    product.size.equals(state.filterSize, ignoreCase = true)
            val matchesCondition = state.filterCondition == null ||
                    product.condition == state.filterCondition
            val matchesSwap = !state.filterSwapOnly || product.canSwap

            matchesQuery && matchesCategory && matchesSize && matchesCondition && matchesSwap
        }

        when (state.sortOrder) {
            "price_asc" -> filtered.sortedBy { it.price }
            "price_desc" -> filtered.sortedByDescending { it.price }
            else -> filtered
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelect(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onSortOrderCycle() {
        val next = when (_uiState.value.sortOrder) {
            "newest" -> "price_asc"
            "price_asc" -> "price_desc"
            else -> "newest"
        }
        _uiState.value = _uiState.value.copy(sortOrder = next)
    }

    fun setSortOrder(order: String) {
        _uiState.value = _uiState.value.copy(sortOrder = order)
    }

    fun toggleFilterSheet(open: Boolean) {
        _uiState.value = _uiState.value.copy(isFilterSheetOpen = open)
    }

    fun updateFilters(size: String?, condition: ConditionType?, swapOnly: Boolean) {
        _uiState.value = _uiState.value.copy(
            filterSize = size,
            filterCondition = condition,
            filterSwapOnly = swapOnly
        )
    }

    fun clearAllFilters() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            selectedCategory = "All",
            filterSize = null,
            filterCondition = null,
            filterSwapOnly = false
        )
    }

    fun toggleLike(productId: String) {
        repository.toggleLike(productId)
    }

    fun saveSearchCriteria(
        keyword: String,
        category: String?,
        style: String?,
        size: String?,
        maxPrice: Long?
    ) {
        repository.addSavedSearch(keyword.ifBlank { category ?: "Tất cả" }, style, size, maxPrice)
    }

    companion object {
        fun provideFactory(
            initialCategory: String?,
            repository: ISwapSheRepository = AppContainer.repository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ExploreViewModel(initialCategory, repository) as T
            }
        }
    }
}
