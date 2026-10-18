package com.swapshe.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.di.AppContainer
import com.swapshe.app.model.BankPayoutAccount
import com.swapshe.app.model.SheTrustProfile
import com.swapshe.app.model.Wallet
import kotlinx.coroutines.flow.StateFlow

class WalletViewModel(
    private val repository: ISwapSheRepository = AppContainer.repository
) : ViewModel() {

    val wallet: StateFlow<Wallet> = repository.wallet
    val trustProfile: StateFlow<SheTrustProfile> = repository.sheTrustProfile

    fun withdrawFunds(amount: Long, bank: BankPayoutAccount): Boolean {
        return repository.withdrawFunds(amount, bank)
    }

    fun showToast(message: String) {
        repository.showToast(message)
    }

    companion object {
        fun provideFactory(
            repository: ISwapSheRepository = AppContainer.repository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WalletViewModel(repository) as T
            }
        }
    }
}
