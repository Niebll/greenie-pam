package com.example.greeniepam.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.greeniepam.core.model.LibraryColor
import com.example.greeniepam.core.model.LibraryPlant
import com.example.greeniepam.core.theme.GreenieGradients
import com.example.greeniepam.core.theme.GreenieTheme

// Gradien kartu hijau muda & teal: nilai mentah dari Figma (frame Pustaka), belum ada token
private val LimeGradient = Brush.verticalGradient(listOf(Color(0xFF5E9A62), Color(0xFF1F4A2E)))
private val TealGradient = Brush.verticalGradient(listOf(Color(0xFF3B7F86), Color(0xFF173F45)))

/** Gradien kartu tanaman katalog sesuai kolom `plant_library.color`. */
fun LibraryColor.brush(): Brush = when (this) {
    LibraryColor.GREEN -> GreenieGradients.cardGreen
    LibraryColor.GOLD -> GreenieGradients.cardGold
    LibraryColor.LIME -> LimeGradient
    LibraryColor.TEAL -> TealGradient
}

/** Warna ikon daun di atas gradien (putih transparan seperti di Figma). */
@Composable
fun leafTint(): Color = GreenieTheme.colors.text.primary.copy(alpha = 0.4f)

/**
 * Foto tanaman katalog. Di bawahnya selalu ada gradien + ikon daun,
 * jadi saat foto masih dimuat / gagal (offline) kartu tetap terlihat rapi.
 * Atur ukuran & sudut lewat [modifier] (pakai clip di pemanggil).
 */
@Composable
fun LibraryPlantImage(
    plant: LibraryPlant,
    leafSize: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(plant.color.brush()),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Eco,
            contentDescription = null,
            tint = leafTint(),
            modifier = Modifier.size(leafSize),
        )
        if (plant.imageUrl != null) {
            AsyncImage(
                model = plant.imageUrl,
                contentDescription = "Foto ${plant.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        }
    }
}

/**
 * Kartu tanaman katalog versi baris (Figma › Pustaka › "Rekomendasi untukmu").
 * Dipakai di Pustaka (rekomendasi & hasil cari).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LibraryPlantRowCard(
    plant: LibraryPlant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(GreenieTheme.radius.radius24)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(GreenieTheme.colors.surface.card)
            .border(1.dp, GreenieTheme.colors.border.default, shape)
            .clickable(onClick = onClick)
            .padding(GreenieTheme.spacing.space10),
        horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space12),
    ) {
        LibraryPlantImage(
            plant = plant,
            leafSize = 28.dp,
            modifier = Modifier
                .size(92.dp) // ukuran thumbnail dari Figma
                .clip(RoundedCornerShape(GreenieTheme.radius.radius18)),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = GreenieTheme.spacing.space2),
            verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space2),
        ) {
            Text(text = plant.name, style = GreenieTheme.typography.title15, color = GreenieTheme.colors.text.primary)
            Text(text = plant.latinName, style = GreenieTheme.typography.body12Italic, color = GreenieTheme.colors.text.secondary)
            Spacer(Modifier.height(GreenieTheme.spacing.space6))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space4),
                verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space4),
            ) {
                PlantTag(plant.lightTag)
                PlantTag("Siram ${plant.waterDays} hari")
                PlantTag(plant.difficulty)
            }
        }
    }
}

@Composable
private fun PlantTag(text: String) {
    val shape = RoundedCornerShape(GreenieTheme.radius.radius14)
    Text(
        text = text,
        style = GreenieTheme.typography.label11,
        color = GreenieTheme.colors.text.primary,
        modifier = Modifier
            .clip(shape)
            .background(GreenieTheme.colors.surface.card)
            .border(1.dp, GreenieTheme.colors.border.default, shape)
            .padding(horizontal = 11.dp, vertical = 5.dp), // padding tag dari Figma (belum ada token)
    )
}

internal val previewLibraryPlant = LibraryPlant(
    id = 1, name = "Lidah mertua", latinName = "Sansevieria trifasciata", category = "Hias daun",
    difficulty = "Mudah", light = "Teduh hingga terang", lightTag = "Teduh–terang", waterDays = 14,
    waterText = "Tiap 2–3 minggu", humidity = "Rendah", color = LibraryColor.TEAL,
)

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun LibraryPlantRowCardPreview() {
    GreenieTheme {
        LibraryPlantRowCard(
            plant = previewLibraryPlant,
            onClick = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
