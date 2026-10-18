package com.swapshe.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.di.AppContainer
import com.swapshe.app.model.AppNotification
import com.swapshe.app.model.BodyMeasurements
import com.swapshe.app.model.Product
import com.swapshe.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val selectedCategory: String = "All",
    val selectedSize: String = "All",
    val showNotificationsSheet: Boolean = false,
    val showFitAssistant: Boolean = false,
    val showVirtualFitStudio: Boolean = false,
    val customFitAppliedInfo: String? = null
)

class HomeViewModel(
    private val repository: ISwapSheRepository = AppContainer.repository
) : ViewModel() {

    val products: StateFlow<List<Product>> = repository.products
    val user: StateFlow<User> = repository.currentUser
    val notifications: StateFlow<List<AppNotification>> = repository.notifications

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        products,
        _uiState
    ) { prodList, state ->
        prodList.filter { product ->
            val matchCategory = state.selectedCategory == "All" ||
                    product.category.equals(state.selectedCategory, ignoreCase = true)
            val matchSize = if (state.selectedSize == "All") {
                true
            } else {
                product.size.equals(state.selectedSize, ignoreCase = true) ||
                        product.size.equals("Free", ignoreCase = true)
            }
            matchCategory && matchSize
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onCategorySelect(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onSizeSelect(size: String) {
        val newInfo = if (size == "All") null else _uiState.value.customFitAppliedInfo
        _uiState.value = _uiState.value.copy(
            selectedSize = size,
            customFitAppliedInfo = newInfo
        )
    }

    fun toggleNotificationsSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(showNotificationsSheet = show)
    }

    fun toggleFitAssistant(show: Boolean) {
        _uiState.value = _uiState.value.copy(showFitAssistant = show)
    }

    fun toggleVirtualFitStudio(show: Boolean) {
        _uiState.value = _uiState.value.copy(showVirtualFitStudio = show)
    }

    fun applyCustomFit(
        size: String,
        heightCm: Int,
        weightKg: Int,
        bustCm: Int?,
        waistCm: Int?,
        hipsCm: Int?,
        skinTone: String
    ) {
        repository.updatePreferences(
            size = size,
            measurements = BodyMeasurements(
                heightCm = heightCm,
                weightKg = weightKg,
                bustCm = bustCm,
                waistCm = waistCm,
                hipsCm = hipsCm
            ),
            skinTone = skinTone
        )
        val toneLabel = skinTone.substringBefore(" (")
        _uiState.value = _uiState.value.copy(
            selectedSize = size,
            customFitAppliedInfo = "Chuẩn dáng: Size $size (${heightCm}cm • ${weightKg}kg • $toneLabel)",
            showFitAssistant = false
        )
    }

    fun applyVirtualFitMeasurements(heightCm: Int, bustCm: Int, waistCm: Int, hipsCm: Int) {
        val currentWeight = user.value.bodyMeasurements?.weightKg ?: 48
        repository.updatePreferences(
            measurements = BodyMeasurements(
                heightCm = heightCm,
                weightKg = currentWeight,
                bustCm = bustCm,
                waistCm = waistCm,
                hipsCm = hipsCm
            )
        )
    }

    fun clearFitFilter() {
        _uiState.value = _uiState.value.copy(
            selectedSize = "All",
            selectedCategory = "All",
            customFitAppliedInfo = null
        )
    }

    fun toggleLike(productId: String) {
        repository.toggleLike(productId)
    }

    fun markNotificationAsRead(id: String) {
        repository.markNotificationAsRead(id)
    }

    companion object {
        fun provideFactory(
            repository: ISwapSheRepository = AppContainer.repository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(repository) as T
            }
        }
    }
}
