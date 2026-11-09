package com.example.greeniepam.feature.schedule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarCard(
    selectedDate: LocalDate,
    currentMonth: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onJumpToToday: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(24.dp)
    val yearMonth = YearMonth.from(currentMonth)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.withDayOfMonth(1)
    val dayOfWeekValue = firstDayOfMonth.dayOfWeek.value // 1 (Mon) to 7 (Sun)
    val offset = (dayOfWeekValue % 7) // Sunday start = 0 or Monday start

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(GreenieTheme.colors.surface.card)
            .border(1.dp, GreenieTheme.colors.border.subtle, cardShape)
            .padding(GreenieTheme.spacing.space16)
    ) {
        // Month Header & Jump to Today
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val monthName = currentMonth.month.getDisplayName(TextStyle.FULL, Locale("id", "ID"))
            Text(
                text = "$monthName ${currentMonth.year}",
                style = GreenieTheme.typography.heading18,
                color = GreenieTheme.colors.text.primary
            )

            // Jump to Today Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(GreenieTheme.radius.full))
                    .background(GreenieTheme.colors.surface.accentSoft)
                    .clickable(onClick = onJumpToToday)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Kembali ke hari ini",
                    style = GreenieTheme.typography.label11,
                    color = GreenieTheme.colors.text.accent
                )
            }
        }

        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space12))

        // Weekday Header Row
        val weekdays = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            weekdays.forEach { day ->
                Text(
                    text = day,
                    style = GreenieTheme.typography.label10,
                    color = GreenieTheme.colors.text.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space8))

        // Days Grid (6 rows of 7 days)
        val totalCells = ((daysInMonth + offset + 6) / 7) * 7
        val daysList = (1..totalCells).map { index ->
            val dayNumber = index - offset
            if (dayNumber in 1..daysInMonth) {
                currentMonth.withDayOfMonth(dayNumber)
            } else {
                null
            }
        }

        val weeks = daysList.chunked(7)
        weeks.forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                week.forEach { date ->
                    if (date != null) {
                        val isSelected = date == selectedDate
                        val isToday = date == LocalDate.now()
                        val dayShape = CircleShape

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(dayShape)
                                .background(
                                    when {
                                        isSelected -> GreenieTheme.colors.surface.accent
                                        isToday -> GreenieTheme.colors.surface.cardSubtle
                                        else -> Color.Transparent
                                    }
                                )
                                .border(
                                    width = if (isToday && !isSelected) 1.5.dp else 0.dp,
                                    color = if (isToday && !isSelected) GreenieTheme.colors.border.accent else Color.Transparent,
                                    shape = dayShape
                                )
                                .clickable { onDateSelected(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${date.dayOfMonth}",
                                style = if (isSelected || isToday) GreenieTheme.typography.title13 else GreenieTheme.typography.body13Medium,
                                color = when {
                                    isSelected -> GreenieTheme.colors.text.onAccent
                                    else -> GreenieTheme.colors.text.primary
                                }
                            )
                        }
                    } else {
                        Box(modifier = Modifier.size(36.dp))
                    }
                }
            }
        }
    }
}
