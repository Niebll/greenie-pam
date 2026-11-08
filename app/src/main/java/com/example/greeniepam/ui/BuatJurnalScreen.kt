package com.example.greeniepam.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas

private val samplePlantList = listOf("Monstera", "Pothos", "Kaktus mini", "Calathea")
private val quickNotesOptions = listOf("Daun baru muncul", "Daun menguning", "Sudah disiram")
private val leafConditionOptions = listOf("Segar", "Menguning", "Layu")

@Composable
fun BuatJurnalScreen(
    onSaveSuccess: (JournalEntry) -> Unit = {}
) {
    val context = LocalContext.current

    var selectedPlant by remember { mutableStateOf("Monstera") }
    var tanggalText by remember { mutableStateOf("1 Nov 2026") }
    var tinggiText by remember { mutableStateOf("32 cm") }
    var notesText by remember { mutableStateOf("") }
    var selectedCondition by remember { mutableStateOf("Segar") }
    var shareToFeed by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .greenieCanvas(),
        contentPadding = PaddingValues(
            top = GreenieTheme.spacing.screenTop,
            bottom = 100.dp
        )
    ) {
        // Header
        item {
            Column(
                modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal)
            ) {
                Text(
                    text = "Buat Jurnal",
                    style = GreenieTheme.typography.heading28,
                    color = GreenieTheme.colors.text.primary
                )
                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space4))
                Text(
                    text = "Catat dan bagikan perkembangan tanamanmu",
                    style = GreenieTheme.typography.body13,
                    color = GreenieTheme.colors.text.secondary
                )
                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))
            }
        }

        // Horizontal Plant Choice Chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = GreenieTheme.spacing.screenHorizontal),
                horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space8)
            ) {
                items(samplePlantList) { plantName ->
                    val isSelected = plantName == selectedPlant
                    val chipShape = RoundedCornerShape(GreenieTheme.radius.full)
                    Box(
                        modifier = Modifier
                            .clip(chipShape)
                            .background(if (isSelected) GreenieTheme.colors.surface.card else Color.White.copy(alpha = 0.05f))
                            .border(
                                width = 1.dp,
                                color = if (isSelected) GreenieTheme.colors.border.accent else GreenieTheme.colors.border.subtle,
                                shape = chipShape
                            )
                            .clickable { selectedPlant = plantName }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = plantName,
                            style = if (isSelected) GreenieTheme.typography.title14 else GreenieTheme.typography.body13Medium,
                            color = if (isSelected) GreenieTheme.colors.text.accent else GreenieTheme.colors.text.secondary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))
        }

        // Upload Photo Box
        item {
            Column(
                modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal)
            ) {
                val photoCardShape = RoundedCornerShape(GreenieTheme.radius.radius20)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(photoCardShape)
                        .background(GreenieTheme.colors.surface.card)
                        .border(1.dp, GreenieTheme.colors.border.subtle, photoCardShape)
                        .clickable {
                            Toast.makeText(context, "Buka kamera / galeri", Toast.LENGTH_SHORT).show()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Tambah Foto",
                        tint = GreenieTheme.colors.icon.accent,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space8))
                    Text(
                        text = "Tambah foto tanaman",
                        style = GreenieTheme.typography.title14,
                        color = GreenieTheme.colors.text.primary
                    )
                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space2))
                    Text(
                        text = "Dari kamera atau galeri",
                        style = GreenieTheme.typography.body12,
                        color = GreenieTheme.colors.text.secondary
                    )
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))
            }
        }

        // Form Card (Tanggal, Tinggi, Catatan, Tulis Cepat, Kondisi Daun)
        item {
            Column(
                modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal)
            ) {
                val formCardShape = RoundedCornerShape(GreenieTheme.radius.radius24)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(formCardShape)
                        .background(GreenieTheme.colors.surface.card)
                        .border(1.dp, GreenieTheme.colors.border.subtle, formCardShape)
                        .padding(GreenieTheme.spacing.space16)
                ) {
                    // Row 1: Tanggal & Tinggi
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space12),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Tanggal Field
                        FormFieldBox(
                            label = "Tanggal",
                            value = tanggalText,
                            onValueChange = { tanggalText = it },
                            modifier = Modifier.weight(1f)
                        )

                        // Tinggi Field
                        FormFieldBox(
                            label = "Tinggi",
                            value = tinggiText,
                            onValueChange = { tinggiText = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space12))

                    // Catatan Multiline Input
                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        placeholder = {
                            Text(
                                text = "Tulis catatan hari ini...",
                                style = GreenieTheme.typography.body14,
                                color = GreenieTheme.colors.text.placeholder
                            )
                        },
                        minLines = 3,
                        maxLines = 4,
                        shape = RoundedCornerShape(GreenieTheme.radius.radius16),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = GreenieTheme.colors.surface.cardSubtle,
                            unfocusedContainerColor = GreenieTheme.colors.surface.cardSubtle,
                            focusedBorderColor = GreenieTheme.colors.border.accent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = GreenieTheme.colors.text.primary,
                            unfocusedTextColor = GreenieTheme.colors.text.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))

                    // Section: Tulis Cepat
                    Text(
                        text = "Tulis cepat",
                        style = GreenieTheme.typography.title14,
                        color = GreenieTheme.colors.text.primary
                    )
                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space8))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space8)
                    ) {
                        items(quickNotesOptions) { option ->
                            val chipShape = RoundedCornerShape(GreenieTheme.radius.full)
                            Box(
                                modifier = Modifier
                                    .clip(chipShape)
                                    .background(Color.White.copy(alpha = 0.05f))
                                    .border(1.dp, GreenieTheme.colors.border.subtle, chipShape)
                                    .clickable {
                                        notesText = if (notesText.isBlank()) option else "$notesText. $option"
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = option,
                                    style = GreenieTheme.typography.body13Medium,
                                    color = GreenieTheme.colors.text.secondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))

                    // Section: Kondisi Daun
                    Text(
                        text = "Kondisi daun",
                        style = GreenieTheme.typography.title14,
                        color = GreenieTheme.colors.text.primary
                    )
                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space8))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space8)
                    ) {
                        leafConditionOptions.forEach { condition ->
                            val isSelected = condition == selectedCondition
                            val chipShape = RoundedCornerShape(GreenieTheme.radius.full)
                            Box(
                                modifier = Modifier
                                    .clip(chipShape)
                                    .background(
                                        if (isSelected) GreenieTheme.colors.surface.accent else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) GreenieTheme.colors.border.accent else GreenieTheme.colors.border.subtle,
                                        shape = chipShape
                                    )
                                    .clickable { selectedCondition = condition }
                                    .padding(horizontal = 18.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = condition,
                                    style = if (isSelected) GreenieTheme.typography.title14 else GreenieTheme.typography.body13Medium,
                                    color = if (isSelected) GreenieTheme.colors.text.onAccent else GreenieTheme.colors.text.secondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))
            }
        }

        // Share to Feed Switch Card
        item {
            Column(
                modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal)
            ) {
                val shareCardShape = RoundedCornerShape(GreenieTheme.radius.radius20)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shareCardShape)
                        .background(GreenieTheme.colors.surface.card)
                        .border(1.dp, GreenieTheme.colors.border.subtle, shareCardShape)
                        .padding(GreenieTheme.spacing.space16),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Bagikan ke Beranda",
                            style = GreenieTheme.typography.title14,
                            color = GreenieTheme.colors.text.primary
                        )
                        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space2))
                        Text(
                            text = "Tampil di Beranda dan tab Dibagikan di profilmu",
                            style = GreenieTheme.typography.body12,
                            color = GreenieTheme.colors.text.secondary
                        )
                    }

                    Spacer(modifier = Modifier.width(GreenieTheme.spacing.space12))

                    Switch(
                        checked = shareToFeed,
                        onCheckedChange = { shareToFeed = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = GreenieTheme.colors.surface.white,
                            checkedTrackColor = GreenieTheme.colors.surface.accent,
                            uncheckedThumbColor = GreenieTheme.colors.icon.default,
                            uncheckedTrackColor = GreenieTheme.colors.surface.cardSubtle
                        )
                    )
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space24))
            }
        }

        // Primary Submit Button (Simpan & bagikan)
        item {
            Column(
                modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal)
            ) {
                Button(
                    onClick = {
                        val newEntry = JournalEntry(
                            plantName = selectedPlant,
                            date = tanggalText.ifBlank { "Hari ini" },
                            height = tinggiText,
                            notes = notesText,
                            condition = selectedCondition,
                            isSharedToFeed = shareToFeed
                        )
                        Toast.makeText(context, "Jurnal $selectedPlant berhasil disimpan ke Koleksi!", Toast.LENGTH_SHORT).show()
                        onSaveSuccess(newEntry)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenieTheme.colors.surface.accent,
                        contentColor = GreenieTheme.colors.text.onAccent
                    ),
                    shape = RoundedCornerShape(GreenieTheme.radius.full),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = "Simpan & bagikan",
                        style = GreenieTheme.typography.title15
                    )
                }
            }
        }
    }
}

@Composable
private fun FormFieldBox(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val boxShape = RoundedCornerShape(GreenieTheme.radius.radius14)
    Column(
        modifier = modifier
            .clip(boxShape)
            .background(GreenieTheme.colors.surface.cardSubtle)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = GreenieTheme.typography.body12,
            color = GreenieTheme.colors.text.secondary
        )
        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space2))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = GreenieTheme.typography.title14,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = GreenieTheme.colors.text.primary,
                unfocusedTextColor = GreenieTheme.colors.text.primary
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
