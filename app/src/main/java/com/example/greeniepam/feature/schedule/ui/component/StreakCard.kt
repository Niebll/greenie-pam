package com.example.greeniepam.feature.schedule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.greeniepam.feature.schedule.data.model.StreakInfo

@Composable
fun StreakCard(
    streakInfo: StreakInfo,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(24.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(GreenieTheme.colors.surface.card)
            .border(1.dp, GreenieTheme.colors.border.subtle, cardShape)
            .padding(GreenieTheme.spacing.space20),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Flame Icon / Placeholder
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(GreenieTheme.colors.surface.accentSoft),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🔥",
                style = GreenieTheme.typography.heading20
            )
        }

        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space8))

        Text(
            text = "${streakInfo.streakDays}",
            style = GreenieTheme.typography.heading28,
            color = GreenieTheme.colors.text.accent
        )
        Text(
            text = "hari streak!",
            style = GreenieTheme.typography.heading18,
            color = GreenieTheme.colors.text.primary
        )

        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space4))

        Text(
            text = "Selesaikan ${streakInfo.tasksRemainingToday} tugas hari ini untuk lanjut",
            style = GreenieTheme.typography.body12,
            color = GreenieTheme.colors.text.secondary
        )

        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))

        // Weekly Status Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            streakInfo.weeklyStatus.forEach { dayStatus ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = dayStatus.dayName,
                        style = GreenieTheme.typography.label10,
                        color = GreenieTheme.colors.text.secondary
                    )

                    val itemShape = CircleShape
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(itemShape)
                            .background(
                                if (dayStatus.isCompleted) GreenieTheme.colors.surface.accent
                                else
                                    Color.Transparent
                            )
                            .border(
                                width = if (dayStatus.isToday) 2.dp else 1.dp,
                                color = if (dayStatus.isToday) GreenieTheme.colors.border.accent else GreenieTheme.colors.border.subtle,
                                shape = itemShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayStatus.isCompleted) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = GreenieTheme.colors.text.onAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
