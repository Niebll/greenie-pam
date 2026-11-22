package com.example.greeniepam.feature.journal

import com.example.greeniepam.feature.journal.ui.create.BuatJurnalViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val journalModule = module {
    viewModel { BuatJurnalViewModel(get(), get()) }
}
