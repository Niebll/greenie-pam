package com.example.greeniepam.feature.schedule.data.model

import java.time.LocalDate

enum class TaskStatus {
    PENDING,
    OVERDUE,
    DONE
}

data class ScheduleTask(
    val id: String,
    val plantName: String,
    val actionName: String,
    val time: String,
    val date: LocalDate,
    val status: TaskStatus
)

data class StreakInfo(
    val streakDays: Int,
    val tasksRemainingToday: Int,
    val weeklyStatus: List<DayStreakStatus>
)

data class DayStreakStatus(
    val dayName: String, // "Sen", "Sel", "Rab", dll
    val isCompleted: Boolean,
    val isToday: Boolean = false
)

data class DayCareStatus(
    val date: LocalDate,
    val hasTasks: Boolean,
    val isAllCompleted: Boolean,
    val hasOverdue: Boolean
)
