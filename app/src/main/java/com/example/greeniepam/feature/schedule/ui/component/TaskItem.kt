package com.example.greeniepam.feature.schedule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.feature.schedule.data.model.ScheduleTask
import com.example.greeniepam.feature.schedule.data.model.TaskStatus

@Composable
fun TaskItem(
    task: ScheduleTask,
    onToggleTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(20.dp)
    val isDone = task.status == TaskStatus.DONE
    val isOverdue = task.status == TaskStatus.OVERDUE

    val borderColor = when {
        isDone -> GreenieTheme.colors.border.success
        isOverdue -> GreenieTheme.colors.border.dangerSoft
        else -> GreenieTheme.colors.border.subtle
    }

    val backgroundColor = GreenieTheme.colors.surface.card

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(backgroundColor)
            .border(1.dp, borderColor, cardShape)
            .clickable { onToggleTask(task.id) }
            .padding(GreenieTheme.spacing.space16),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = task.actionName,
                style = GreenieTheme.typography.title15,
                color = if (isDone) GreenieTheme.colors.text.secondary else GreenieTheme.colors.text.primary
            )

            val statusText = when {
                isDone -> "Selesai · ${task.time}"
                isOverdue -> "Terlewat"
                else -> "Hari ini · ${task.time}"
            }

            val statusColor = when {
                isDone -> GreenieTheme.colors.text.accent
                isOverdue -> GreenieTheme.colors.text.danger
                else -> GreenieTheme.colors.text.secondary
            }

            Text(
                text = statusText,
                style = GreenieTheme.typography.body12,
                color = statusColor
            )
        }

        val checkShape = CircleShape
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(checkShape)
                .background(
                    if (isDone) GreenieTheme.colors.surface.success
                    else Color.Transparent
                )
                .border(
                    width = 2.dp,
                    color = if (isDone) GreenieTheme.colors.border.success else GreenieTheme.colors.border.strong,
                    shape = checkShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Selesai",
                    tint = GreenieTheme.colors.text.onAccent,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
