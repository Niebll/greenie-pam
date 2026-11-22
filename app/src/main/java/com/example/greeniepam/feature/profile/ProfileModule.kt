package com.example.greeniepam.feature.profile

import com.example.greeniepam.feature.profile.ui.edit_profile.EditProfileViewModel
import com.example.greeniepam.feature.profile.ui.favorites.FavoritesViewModel
import com.example.greeniepam.feature.profile.ui.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { EditProfileViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get()) }
}
