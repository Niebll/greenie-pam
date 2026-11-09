package com.example.greeniepam.feature.profile.ui.profile.component

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.feature.profile.data.model.PlantGradientType

@Composable
fun AddPlantDialog(
    onDismiss: () -> Unit,
    onAddPlant: (String, PlantGradientType) -> Unit,
) {
    var plantName by remember { mutableStateOf("") }
    var selectedGradient by remember { mutableStateOf(PlantGradientType.GREEN) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF142C20))
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp))
                .padding(20.dp),
        ) {
            Text(
                text = "Tambah Tanaman Baru",
                style = GreenieTheme.typography.heading18,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Spacer(Modifier.height(16.dp))

            // Text field untuk nama tanaman
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x1AFFFFFF))
                    .border(1.dp, Color(0x24FFFFFF), RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                BasicTextField(
                    value = plantName,
                    onValueChange = { plantName = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                    ),
                    cursorBrush = SolidColor(Color(0xFFE5C16C)),
                    decorationBox = { innerTextField ->
                        if (plantName.isEmpty()) {
                            Text(
                                text = "Nama tanaman (mis. Monstera)",
                                fontSize = 14.sp,
                                color = Color(0xFFA5B8AC),
                            )
                        }
                        innerTextField()
                    },
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Pilih Warna Kartu",
                style = GreenieTheme.typography.body12,
                color = Color(0xFFA5B8AC),
            )

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GradientOptionChip(
                    label = "Hijau",
                    color = Color(0xFF388E59),
                    isSelected = selectedGradient == PlantGradientType.GREEN,
                    onClick = { selectedGradient = PlantGradientType.GREEN },
                )
                GradientOptionChip(
                    label = "Emas",
                    color = Color(0xFFB58734),
                    isSelected = selectedGradient == PlantGradientType.GOLD,
                    onClick = { selectedGradient = PlantGradientType.GOLD },
                )
                GradientOptionChip(
                    label = "Teal",
                    color = Color(0xFF357F85),
                    isSelected = selectedGradient == PlantGradientType.TEAL,
                    onClick = { selectedGradient = PlantGradientType.TEAL },
                )
            }

            Spacer(Modifier.height(24.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onDismiss)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = "Batal",
                        style = GreenieTheme.typography.label12,
                        color = Color(0xFFA5B8AC),
                    )
                }

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFE5C16C))
                        .clickable {
                            if (plantName.isNotBlank()) {
                                onAddPlant(plantName.trim(), selectedGradient)
                            }
                        }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = "Tambah",
                        style = GreenieTheme.typography.label12,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F2418),
                    )
                }
            }
        }
    }
}

@Composable
private fun GradientOptionChip(
    label: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) color else Color(0x1AFFFFFF))
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) Color.White else Color(0x33FFFFFF),
                shape = CircleShape,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White,
        )
    }
}
