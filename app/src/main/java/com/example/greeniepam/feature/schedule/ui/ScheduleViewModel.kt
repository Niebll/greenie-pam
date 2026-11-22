package com.example.greeniepam.feature.schedule.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greeniepam.core.data.PlantRepository
import com.example.greeniepam.core.util.toUserMessage
import com.example.greeniepam.feature.schedule.data.CareTaskRow
import com.example.greeniepam.feature.schedule.data.ScheduleRepository
import com.example.greeniepam.feature.schedule.data.model.DayStreakStatus
import com.example.greeniepam.feature.schedule.data.model.ScheduleTask
import com.example.greeniepam.feature.schedule.data.model.StreakInfo
import com.example.greeniepam.feature.schedule.data.model.TaskStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val plantRepository: PlantRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    private var allTasks: List<CareTaskRow> = emptyList()

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                allTasks = scheduleRepository.getMyTasks()
                val plantNames = runCatching { plantRepository.getMyPlants().map { it.name } }.getOrDefault(emptyList())
                _uiState.update { it.copy(plantNames = plantNames) }
                publishTasks()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        publishTasks()
    }

    fun onJumpToToday() {
        val today = LocalDate.now()
        _uiState.update { it.copy(selectedDate = today, currentMonth = today) }
        publishTasks()
    }

    fun onToggleTask(taskId: String) {
        val id = taskId.toLongOrNull() ?: return
        val task = allTasks.find { it.id == id } ?: return
        val newDone = !task.isDone

        setDoneLocally(id, newDone)
        val remaining = tasksOn(LocalDate.now()).count { !it.isDone }
        val toastMsg = when {
            !newDone -> "Status diperbarui"
            remaining == 0 -> "${task.actionName} selesai\nSemua tugas hari ini beres! 🔥"
            else -> "${task.actionName} selesai\nMantap! $remaining tugas lagi untuk lanjutkan streak🔥"
        }
        _uiState.update { it.copy(toastMessage = toastMsg, isToastSuccess = true) }

        viewModelScope.launch {
            try {
                scheduleRepository.setDone(id, newDone)
            } catch (e: Exception) {
                setDoneLocally(id, !newDone)
                _uiState.update { it.copy(toastMessage = e.toUserMessage(), isToastSuccess = false) }
            }
        }
    }

    fun showAddTaskDialog(show: Boolean) {
        _uiState.update { it.copy(showAddTaskDialog = show) }
    }

    fun addTask(plantName: String, actionName: String, time: String) {
        val date = _uiState.value.selectedDate
        viewModelScope.launch {
            try {
                val row = scheduleRepository.addTask(plantName, actionName, time, date.toString())
                allTasks = allTasks + row
                _uiState.update {
                    it.copy(showAddTaskDialog = false, toastMessage = "Jadwal ditambahkan", isToastSuccess = true)
                }
                publishTasks()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(showAddTaskDialog = false, toastMessage = e.toUserMessage(), isToastSuccess = false)
                }
            }
        }
    }

    fun dismissToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    /* ---------- hitung ulang tampilan dari allTasks ---------- */

    private fun setDoneLocally(id: Long, isDone: Boolean) {
        allTasks = allTasks.map { if (it.id == id) it.copy(isDone = isDone) else it }
        publishTasks()
    }

    private fun tasksOn(date: LocalDate): List<CareTaskRow> =
        allTasks.filter { it.taskDate == date.toString() }

    private fun publishTasks() {
        val today = LocalDate.now()
        val selected = _uiState.value.selectedDate
        _uiState.update {
            it.copy(
                tasksForSelectedDate = tasksOn(selected)
                    .sortedBy { row -> row.taskTime }
                    .map { row -> row.toScheduleTask(today) },
                streakInfo = buildStreak(today),
            )
        }
    }

    private fun CareTaskRow.toScheduleTask(today: LocalDate): ScheduleTask {
        val date = LocalDate.parse(taskDate)
        val status = when {
            isDone -> TaskStatus.DONE
            date.isBefore(today) -> TaskStatus.OVERDUE
            else -> TaskStatus.PENDING
        }
        return ScheduleTask(
            id = id.toString(),
            plantName = plantName,
            actionName = actionName,
            time = taskTime,
            date = date,
            status = status,
        )
    }

    /** Hari dianggap "selesai" kalau punya tugas dan semuanya sudah dikerjakan. */
    private fun isDayCompleted(date: LocalDate): Boolean {
        val tasks = tasksOn(date)
        return tasks.isNotEmpty() && tasks.all { it.isDone }
    }

    private fun buildStreak(today: LocalDate): StreakInfo {
        val earliest = allTasks.minOfOrNull { it.taskDate }?.let(LocalDate::parse)

        // Hitung mundur dari hari ini; hari tanpa tugas dilewati,
        // hari ini yang belum selesai tidak memutus streak.
        var streak = 0
        var day = today
        while (earliest != null && !day.isBefore(earliest)) {
            val tasks = tasksOn(day)
            if (tasks.isNotEmpty()) {
                if (tasks.all { it.isDone }) streak++
                else if (day != today) break
            }
            day = day.minusDays(1)
        }

        val monday = today.with(DayOfWeek.MONDAY)
        val dayNames = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
        val weekly = dayNames.mapIndexed { index, name ->
            val date = monday.plusDays(index.toLong())
            DayStreakStatus(dayName = name, isCompleted = isDayCompleted(date), isToday = date == today)
        }

        return StreakInfo(
            streakDays = streak,
            tasksRemainingToday = tasksOn(today).count { !it.isDone },
            weeklyStatus = weekly,
        )
    }
}
