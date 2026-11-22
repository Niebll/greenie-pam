package com.example.greeniepam.feature.schedule

import com.example.greeniepam.feature.schedule.ui.ScheduleViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val scheduleModule = module {
    viewModel { ScheduleViewModel() }
}
