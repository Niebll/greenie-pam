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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme

@Composable
fun ScheduleToastView(
    message: String,
    isSuccess: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)
    val bgColor = if (isSuccess) GreenieTheme.colors.surface.sheet else GreenieTheme.colors.surface.sheet
    val borderColor = if (isSuccess) GreenieTheme.colors.border.success else GreenieTheme.colors.border.dangerSoft
    val iconTint = if (isSuccess) GreenieTheme.colors.icon.success else GreenieTheme.colors.icon.danger

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(bgColor)
            .border(1.dp, borderColor, cardShape)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isSuccess) GreenieTheme.colors.surface.successSoft else GreenieTheme.colors.surface.dangerSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSuccess) Icons.Outlined.Check else Icons.Outlined.Close,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = if (isSuccess) "Notifikasi Berhasil" else "Status gagal disimpan",
                    style = GreenieTheme.typography.title13,
                    color = GreenieTheme.colors.text.primary
                )
                Text(
                    text = message,
                    style = GreenieTheme.typography.body12,
                    color = GreenieTheme.colors.text.secondary
                )
            }
        }

        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = "Tutup",
            tint = GreenieTheme.colors.icon.default,
            modifier = Modifier
                .size(20.dp)
                .clickable(onClick = onDismiss)
        )
    }
}
