package com.example.greeniepam.feature.detail_post

import com.example.greeniepam.feature.detail_post.ui.DetailPostViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailPostModule = module {
    viewModel { DetailPostViewModel(get()) }
}
