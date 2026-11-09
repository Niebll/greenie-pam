package com.example.greeniepam.feature.schedule.ui

import com.example.greeniepam.feature.schedule.data.model.DayStreakStatus
import com.example.greeniepam.feature.schedule.data.model.ScheduleTask
import com.example.greeniepam.feature.schedule.data.model.StreakInfo
import java.time.LocalDate

data class ScheduleUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonth: LocalDate = LocalDate.now(),
    val streakInfo: StreakInfo = StreakInfo(
        streakDays = 12,
        tasksRemainingToday = 2,
        weeklyStatus = listOf(
            DayStreakStatus("Sen", true),
            DayStreakStatus("Sel", true),
            DayStreakStatus("Rab", true),
            DayStreakStatus("Kam", true),
            DayStreakStatus("Jum", true),
            DayStreakStatus("Sab", true),
            DayStreakStatus("Min", false, isToday = true)
        )
    ),
    val tasksForSelectedDate: List<ScheduleTask> = emptyList(),
    val toastMessage: String? = null,
    val isToastSuccess: Boolean = true
)
