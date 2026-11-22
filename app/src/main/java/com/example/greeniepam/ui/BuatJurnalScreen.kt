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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Spa
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

    var showTulisCatatan by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .greenieCanvas()
    ) {
        if (showTulisCatatan) {
            TulisCatatanScreen(
                initialNotes = notesText,
                plantName = selectedPlant,
                date = tanggalText,
                height = tinggiText,
                onDismiss = { showTulisCatatan = false },
                onSave = { updatedNotes ->
                    notesText = updatedNotes
                    showTulisCatatan = false
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
                                FormFieldBox(
                                    label = "Tanggal",
                                    value = tanggalText,
                                    onValueChange = { tanggalText = it },
                                    modifier = Modifier.weight(1f)
                                )
                                FormFieldBox(
                                    label = "Tinggi",
                                    value = tinggiText,
                                    onValueChange = { tinggiText = it },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(GreenieTheme.spacing.space12))

                            // Catatan Multiline Input (Clickable to open TulisCatatanScreen modal)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(GreenieTheme.radius.radius16))
                                    .background(GreenieTheme.colors.surface.cardSubtle)
                                    .clickable { showTulisCatatan = true }
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = notesText.ifBlank { "Tulis catatan hari ini..." },
                                    style = if (notesText.isBlank()) GreenieTheme.typography.body14 else GreenieTheme.typography.body14Medium,
                                    color = if (notesText.isBlank()) GreenieTheme.colors.text.placeholder else GreenieTheme.colors.text.primary,
                                    minLines = 3
                                )
                            }

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
    }
}

@Composable
private fun TulisCatatanScreen(
    initialNotes: String,
    plantName: String,
    date: String,
    height: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialNotes) }
    val quickAddOptions = listOf("Daun baru muncul", "Sudah disiram", "Sudah dipupuk")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .greenieCanvas()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = GreenieTheme.spacing.screenHorizontal, vertical = 12.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close button (×)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Tutup",
                    tint = GreenieTheme.colors.icon.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "Tulis catatan",
                style = GreenieTheme.typography.heading18,
                color = GreenieTheme.colors.text.primary
            )

            // Selesai Button
            Button(
                onClick = { onSave(text) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenieTheme.colors.surface.accent,
                    contentColor = GreenieTheme.colors.text.onAccent
                ),
                shape = RoundedCornerShape(GreenieTheme.radius.full),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                modifier = Modifier.height(38.dp)
            ) {
                Text(
                    text = "Selesai",
                    style = GreenieTheme.typography.title13
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Plant Info Chip
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(GreenieTheme.radius.full))
                .background(GreenieTheme.colors.surface.card)
                .border(1.dp, GreenieTheme.colors.border.subtle, RoundedCornerShape(GreenieTheme.radius.full))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Spa,
                    contentDescription = null,
                    tint = GreenieTheme.colors.icon.accent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$plantName · $date · $height",
                    style = GreenieTheme.typography.label11,
                    color = GreenieTheme.colors.text.accent
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Text Editing Area
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { if (it.length <= 500) text = it },
                placeholder = {
                    Text(
                        text = "Tulis catatan hari ini...",
                        style = GreenieTheme.typography.body14,
                        color = GreenieTheme.colors.text.placeholder
                    )
                },
                textStyle = GreenieTheme.typography.body14Medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = GreenieTheme.colors.text.primary,
                    unfocusedTextColor = GreenieTheme.colors.text.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Character Counter
            Text(
                text = "${text.length}/500",
                style = GreenieTheme.typography.body12,
                color = GreenieTheme.colors.text.secondary,
                modifier = Modifier.align(Alignment.End)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Insertion Chips Bar
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(quickAddOptions) { option ->
                val chipShape = RoundedCornerShape(GreenieTheme.radius.full)
                Box(
                    modifier = Modifier
                        .clip(chipShape)
                        .background(GreenieTheme.colors.surface.card)
                        .border(1.dp, GreenieTheme.colors.border.subtle, chipShape)
                        .clickable {
                            text = if (text.isBlank()) option else "$text. $option"
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "+ ",
                            style = GreenieTheme.typography.title13,
                            color = GreenieTheme.colors.text.accent
                        )
                        Text(
                            text = option,
                            style = GreenieTheme.typography.body13Medium,
                            color = GreenieTheme.colors.text.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
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
