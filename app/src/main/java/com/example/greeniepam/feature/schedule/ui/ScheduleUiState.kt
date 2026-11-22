package com.example.greeniepam.feature.schedule.ui

import com.example.greeniepam.feature.schedule.data.model.ScheduleTask
import com.example.greeniepam.feature.schedule.data.model.StreakInfo
import java.time.LocalDate

data class ScheduleUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonth: LocalDate = LocalDate.now(),
    val streakInfo: StreakInfo = StreakInfo(
        streakDays = 0,
        tasksRemainingToday = 0,
        weeklyStatus = emptyList(),
    ),
    val tasksForSelectedDate: List<ScheduleTask> = emptyList(),
    /** Nama tanaman user, untuk pilihan saat menambah jadwal. */
    val plantNames: List<String> = emptyList(),
    val showAddTaskDialog: Boolean = false,
    val toastMessage: String? = null,
    val isToastSuccess: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
)
