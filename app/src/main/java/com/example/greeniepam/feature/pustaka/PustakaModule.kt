package com.example.greeniepam.feature.pustaka

import com.example.greeniepam.feature.pustaka.ui.detail.PustakaDetailViewModel
import com.example.greeniepam.feature.pustaka.ui.list.PustakaViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val pustakaModule = module {
    viewModel { PustakaViewModel(get()) }
    viewModel { PustakaDetailViewModel(get(), get()) }
}
