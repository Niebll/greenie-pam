package com.example.greeniepam.feature.schedule.ui

import androidx.lifecycle.ViewModel
import com.example.greeniepam.feature.schedule.data.model.ScheduleTask
import com.example.greeniepam.feature.schedule.data.model.TaskStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class ScheduleViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    private val allTasks = mutableMapOf<LocalDate, MutableList<ScheduleTask>>()

    init {
        val today = LocalDate.now()
        val sampleTasks = mutableListOf(
            ScheduleTask(
                id = "t1",
                plantName = "Monstera",
                actionName = "Siram Monstera",
                time = "08.00",
                date = today,
                status = TaskStatus.PENDING
            ),
            ScheduleTask(
                id = "t2",
                plantName = "Pothos",
                actionName = "Pupuk Pothos",
                time = "10.00",
                date = today,
                status = TaskStatus.PENDING
            ),
            ScheduleTask(
                id = "t3",
                plantName = "Lidah mertua",
                actionName = "Siram Lidah mertua",
                time = "07.30",
                date = today,
                status = TaskStatus.DONE
            )
        )
        allTasks[today] = sampleTasks

        val sampleDate = LocalDate.of(2026, 10, 13)
        allTasks[sampleDate] = mutableListOf(
            ScheduleTask(id = "to1", plantName = "Monstera", actionName = "Siram Monstera", time = "08.15", date = sampleDate, status = TaskStatus.DONE),
            ScheduleTask(id = "to2", plantName = "Pothos", actionName = "Pupuk Pothos", time = "10.00", date = sampleDate, status = TaskStatus.OVERDUE),
            ScheduleTask(id = "to3", plantName = "Lidah mertua", actionName = "Siram Lidah mertua", time = "07.30", date = sampleDate, status = TaskStatus.DONE)
        )

        updateTasksForDate(today)
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        updateTasksForDate(date)
    }

    fun onJumpToToday() {
        val today = LocalDate.now()
        _uiState.update { it.copy(selectedDate = today, currentMonth = today) }
        updateTasksForDate(today)
    }

    fun onToggleTask(taskId: String) {
        val currentDate = _uiState.value.selectedDate
        val tasks = allTasks[currentDate] ?: return
        val taskIndex = tasks.indexOfFirst { it.id == taskId }
        if (taskIndex != -1) {
            val task = tasks[taskIndex]
            val newStatus = if (task.status == TaskStatus.DONE) TaskStatus.PENDING else TaskStatus.DONE
            tasks[taskIndex] = task.copy(status = newStatus)

            val toastMsg = if (newStatus == TaskStatus.DONE) "${task.actionName} selesai\nMantap! 1 tugas lagi untuk lanjutkan streak🔥" else "Status diperbarui"
            _uiState.update {
                it.copy(
                    tasksForSelectedDate = tasks.toList(),
                    toastMessage = toastMsg,
                    isToastSuccess = true
                )
            }
        }
    }

    fun dismissToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    private fun updateTasksForDate(date: LocalDate) {
        val tasks = allTasks[date] ?: emptyList()
        _uiState.update { it.copy(tasksForSelectedDate = tasks) }
    }
}
