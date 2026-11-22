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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.greeniepam.core.theme.GreenieTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val actionOptions = listOf("Siram", "Pupuk")

@Composable
fun AddTaskDialog(
    plantNames: List<String>,
    date: LocalDate,
    onDismiss: () -> Unit,
    onAddTask: (plantName: String, actionName: String, time: String) -> Unit,
) {
    var selectedPlant by remember { mutableStateOf(plantNames.firstOrNull().orEmpty()) }
    var selectedAction by remember { mutableStateOf(actionOptions.first()) }
    var time by remember { mutableStateOf("08.00") }
    val dateLabel = date.format(DateTimeFormatter.ofPattern("EEEE, d MMM", Locale("id", "ID")))

    Dialog(onDismissRequest = onDismiss) {
        val shape = RoundedCornerShape(GreenieTheme.radius.radius24)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(GreenieTheme.colors.surface.sheet)
                .border(1.dp, GreenieTheme.colors.border.subtle, shape)
                .padding(GreenieTheme.spacing.space20),
        ) {
            Text(text = "Tambah Jadwal", style = GreenieTheme.typography.heading18, color = GreenieTheme.colors.text.primary)
            Text(text = dateLabel, style = GreenieTheme.typography.body12, color = GreenieTheme.colors.text.secondary)

            Spacer(Modifier.height(GreenieTheme.spacing.space16))
            Label("Tanaman")
            if (plantNames.isEmpty()) {
                Text(
                    text = "Belum ada tanaman. Tambahkan dulu lewat Pustaka.",
                    style = GreenieTheme.typography.body12Medium,
                    color = GreenieTheme.colors.text.accent,
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space8)) {
                    items(plantNames) { name ->
                        Chip(text = name, selected = name == selectedPlant, onClick = { selectedPlant = name })
                    }
                }
            }

            Spacer(Modifier.height(GreenieTheme.spacing.space16))
            Label("Perawatan")
            Row(horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space8)) {
                actionOptions.forEach { action ->
                    Chip(text = action, selected = action == selectedAction, onClick = { selectedAction = action })
                }
            }

            Spacer(Modifier.height(GreenieTheme.spacing.space16))
            Label("Jam")
            val fieldShape = RoundedCornerShape(GreenieTheme.radius.radius14)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(fieldShape)
                    .background(GreenieTheme.colors.surface.card)
                    .border(1.dp, GreenieTheme.colors.border.subtle, fieldShape)
                    .padding(horizontal = GreenieTheme.spacing.space16, vertical = GreenieTheme.spacing.space12),
            ) {
                BasicTextField(
                    value = time,
                    onValueChange = { time = it.take(5) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = GreenieTheme.typography.body14Medium.copy(color = GreenieTheme.colors.text.primary),
                    cursorBrush = SolidColor(GreenieTheme.colors.text.accent),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(GreenieTheme.spacing.space24))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Batal",
                    style = GreenieTheme.typography.label12,
                    color = GreenieTheme.colors.text.secondary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(GreenieTheme.radius.full))
                        .clickable(onClick = onDismiss)
                        .padding(horizontal = GreenieTheme.spacing.space16, vertical = GreenieTheme.spacing.space12),
                )
                Spacer(Modifier.width(GreenieTheme.spacing.space8))
                val canSave = selectedPlant.isNotBlank()
                Text(
                    text = "Tambah",
                    style = GreenieTheme.typography.title12,
                    color = GreenieTheme.colors.text.onAccent,
                    modifier = Modifier
                        .clip(RoundedCornerShape(GreenieTheme.radius.full))
                        .background(if (canSave) GreenieTheme.colors.surface.accent else GreenieTheme.colors.surface.cardMuted)
                        .clickable(enabled = canSave) {
                            onAddTask(selectedPlant, "$selectedAction $selectedPlant", time.trim())
                        }
                        .padding(horizontal = GreenieTheme.spacing.space20, vertical = GreenieTheme.spacing.space12),
                )
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(text = text, style = GreenieTheme.typography.label12, color = GreenieTheme.colors.text.secondary)
    Spacer(Modifier.height(GreenieTheme.spacing.space8))
}

@Composable
private fun Chip(text: String, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(GreenieTheme.radius.full)
    Box(
        modifier = Modifier
            .clip(shape)
            .background(if (selected) GreenieTheme.colors.surface.accent else GreenieTheme.colors.surface.cardSubtle)
            .border(1.dp, if (selected) GreenieTheme.colors.border.accent else GreenieTheme.colors.border.subtle, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = GreenieTheme.spacing.space14, vertical = GreenieTheme.spacing.space10),
    ) {
        Text(
            text = text,
            style = GreenieTheme.typography.label13,
            color = if (selected) GreenieTheme.colors.text.onAccent else GreenieTheme.colors.text.secondary,
        )
    }
}

@Preview
@Composable
private fun AddTaskDialogPreview() {
    GreenieTheme {
        AddTaskDialog(
            plantNames = listOf("Monstera", "Pothos"),
            date = LocalDate.now(),
            onDismiss = {},
            onAddTask = { _, _, _ -> },
        )
    }
}
