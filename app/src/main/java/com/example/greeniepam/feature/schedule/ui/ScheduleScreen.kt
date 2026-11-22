package com.example.greeniepam.feature.schedule.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import com.example.greeniepam.feature.schedule.ui.component.AddTaskDialog
import com.example.greeniepam.feature.schedule.ui.component.CalendarCard
import com.example.greeniepam.feature.schedule.ui.component.ScheduleToastView
import com.example.greeniepam.feature.schedule.ui.component.StreakCard
import com.example.greeniepam.feature.schedule.ui.component.TaskItem
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.refresh() }

    if (uiState.showAddTaskDialog) {
        AddTaskDialog(
            plantNames = uiState.plantNames,
            date = uiState.selectedDate,
            onDismiss = { viewModel.showAddTaskDialog(false) },
            onAddTask = viewModel::addTask,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .greenieCanvas()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = GreenieTheme.spacing.screenHorizontal,
                vertical = GreenieTheme.spacing.screenTop
            ),
            verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space16)
        ) {
            item {
                Column {
                    Text(
                        text = "Jadwal Rawat",
                        style = GreenieTheme.typography.heading28,
                        color = GreenieTheme.colors.text.primary
                    )
                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space4))
                    Text(
                        text = "Jaga streak, tanaman tetap sehat",
                        style = GreenieTheme.typography.body13,
                        color = GreenieTheme.colors.text.secondary
                    )
                }
            }

            item {
                StreakCard(streakInfo = uiState.streakInfo)
            }

            item {
                CalendarCard(
                    selectedDate = uiState.selectedDate,
                    currentMonth = uiState.currentMonth,
                    onDateSelected = { viewModel.onDateSelected(it) },
                    onJumpToToday = { viewModel.onJumpToToday() }
                )
            }

            item {
                val formatter = DateTimeFormatter.ofPattern("EEEE, d MMM", Locale("id", "ID"))
                val dateStr = uiState.selectedDate.format(formatter)
                val completedCount = uiState.tasksForSelectedDate.count { it.status == com.example.greeniepam.feature.schedule.data.model.TaskStatus.DONE }
                val totalCount = uiState.tasksForSelectedDate.size

                Column {
                    RowHeader(
                        title = "Tugas · $dateStr",
                        subtitle = "$completedCount dari $totalCount selesai"
                    )
                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space8))
                    val addShape = RoundedCornerShape(GreenieTheme.radius.full)
                    Row(
                        modifier = Modifier
                            .clip(addShape)
                            .border(1.dp, GreenieTheme.colors.border.accent, addShape)
                            .clickable { viewModel.showAddTaskDialog(true) }
                            .padding(horizontal = GreenieTheme.spacing.space14, vertical = GreenieTheme.spacing.space10),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            tint = GreenieTheme.colors.icon.accent,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(GreenieTheme.spacing.space6))
                        Text(
                            text = "Tambah jadwal",
                            style = GreenieTheme.typography.label12,
                            color = GreenieTheme.colors.text.accent,
                        )
                    }
                }
            }

            uiState.error?.let { error ->
                item {
                    Text(text = error, style = GreenieTheme.typography.body12Medium, color = GreenieTheme.colors.text.danger)
                }
            }

            if (uiState.tasksForSelectedDate.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                        .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada tugas perawatan di tanggal ini.",
                            style = GreenieTheme.typography.body13,
                            color = GreenieTheme.colors.text.secondary
                        )
                    }
                }
            } else {
                items(uiState.tasksForSelectedDate, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggleTask = { viewModel.onToggleTask(it) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        AnimatedVisibility(
            visible = uiState.toastMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = GreenieTheme.spacing.screenHorizontal, vertical = 24.dp)
        ) {
            uiState.toastMessage?.let { msg ->
                ScheduleToastView(
                    message = msg,
                    isSuccess = uiState.isToastSuccess,
                    onDismiss = { viewModel.dismissToast() }
                )
            }
        }
    }
}

@Composable
private fun RowHeader(
    title: String,
    subtitle: String
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = GreenieTheme.typography.heading18,
            color = GreenieTheme.colors.text.primary
        )
        Text(
            text = subtitle,
            style = GreenieTheme.typography.body12,
            color = GreenieTheme.colors.text.accent
        )
    }
}
