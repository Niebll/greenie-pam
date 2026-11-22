package com.example.greeniepam.feature.profile

import com.example.greeniepam.feature.profile.ui.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel { ProfileViewModel() }
}
