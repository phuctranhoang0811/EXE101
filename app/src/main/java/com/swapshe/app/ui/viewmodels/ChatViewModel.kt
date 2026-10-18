package com.swapshe.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.di.AppContainer
import com.swapshe.app.model.ChatMessage
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel(
    private val repository: ISwapSheRepository = AppContainer.repository
) : ViewModel() {

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages

    fun sendMessage(text: String, recipientName: String = "Người bán"): ChatMessage {
        return repository.sendChatMessage(text, recipientName)
    }

    fun checkMessageForScam(text: String): String? {
        return repository.checkMessageForOutPlatformScam(text)
    }

    companion object {
        fun provideFactory(
            repository: ISwapSheRepository = AppContainer.repository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatViewModel(repository) as T
            }
        }
    }
}
