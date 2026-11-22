package com.example.greeniepam.feature.auth

import com.example.greeniepam.feature.auth.data.AuthRepository
import com.example.greeniepam.feature.auth.ui.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { AuthRepository(get()) }
    viewModel { AuthViewModel(get()) }
}
