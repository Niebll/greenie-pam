package com.example.greeniepam.feature.home

import com.example.greeniepam.feature.home.ui.feed.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { HomeViewModel() }
}
