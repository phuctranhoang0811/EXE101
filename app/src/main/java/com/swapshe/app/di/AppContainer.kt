package com.swapshe.app.di

import com.swapshe.app.data.ISwapSheRepository
import com.swapshe.app.data.SwapSheRepository

object AppContainer {
    val repository: ISwapSheRepository by lazy {
        SwapSheRepository.instance
    }
}
