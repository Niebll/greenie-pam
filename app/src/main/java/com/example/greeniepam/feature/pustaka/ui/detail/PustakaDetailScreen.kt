package com.example.greeniepam.feature.pustaka.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.greeniepam.core.component.GreenieToastHost
import com.example.greeniepam.core.component.LibraryPlantImage
import com.example.greeniepam.core.component.brush
import com.example.greeniepam.core.component.leafTint
import com.example.greeniepam.core.model.LibraryColor
import com.example.greeniepam.core.model.LibraryPlant
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas
import org.koin.androidx.compose.koinViewModel

@Composable
fun PustakaDetailScreen(
    plantId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PustakaDetailViewModel = koinViewModel(key = "pustaka-detail-$plantId"),
) {
    LaunchedEffect(plantId) { viewModel.load(plantId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize().greenieCanvas()) {
        PustakaDetailContent(
            state = state,
            onBack = onBack,
            onAddToCollection = viewModel::addToCollection,
        )
        GreenieToastHost(
            toast = state.toast,
            onDismiss = viewModel::dismissToast,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = GreenieTheme.spacing.space16, vertical = GreenieTheme.spacing.space8),
        )
    }
}

@Composable
private fun PustakaDetailContent(
    state: PustakaDetailUiState,
    onBack: () -> Unit,
    onAddToCollection: () -> Unit,
) {
    val plant = state.plant
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // ── Foto tanaman + tombol kembali & favorit ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // tinggi area foto dari Figma
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)) // radius 36 dari Figma (belum ada token)
                .background((plant?.color ?: LibraryColor.GREEN).brush()),
        ) {
            if (plant != null) {
                LibraryPlantImage(plant = plant, leafSize = 72.dp, modifier = Modifier.matchParentSize())
            } else {
                Icon(
                    imageVector = Icons.Outlined.Eco,
                    contentDescription = null,
                    tint = leafTint(),
                    modifier = Modifier
                        .size(72.dp)
                        .align(Alignment.Center),
                )
            }
            // Tombol kembali: kuning bulat supaya tetap terlihat di atas foto
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = GreenieTheme.spacing.screenHorizontal, vertical = GreenieTheme.spacing.space12)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(GreenieTheme.colors.surface.accent)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Kembali",
                    tint = GreenieTheme.colors.icon.onAccent,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        if (plant == null) {
            Text(
                text = if (state.isLoading) "Memuat..." else state.error ?: "Tanaman tidak ditemukan.",
                style = GreenieTheme.typography.body13,
                color = if (state.error != null) GreenieTheme.colors.text.danger else GreenieTheme.colors.text.secondary,
                modifier = Modifier.padding(GreenieTheme.spacing.screenHorizontal),
            )
            return@Column
        }

        Column(
            modifier = Modifier
                .padding(horizontal = GreenieTheme.spacing.screenHorizontal)
                .padding(top = GreenieTheme.spacing.space18, bottom = GreenieTheme.spacing.space24)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space14),
        ) {
            // ── Judul ──
            Column(verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space2)) {
                Text(plant.name, style = GreenieTheme.typography.heading28, color = GreenieTheme.colors.text.primary)
                Text(
                    text = listOf(plant.latinName, plant.category).filter { it.isNotBlank() }.joinToString(" · "),
                    style = GreenieTheme.typography.body13Italic,
                    color = GreenieTheme.colors.text.secondary,
                )
            }

            if (plant.description.isNotBlank()) {
                Text(plant.description, style = GreenieTheme.typography.body13Paragraph, color = GreenieTheme.colors.text.primary)
            }

            // ── Info perawatan (2×2) ──
            Column(verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space10)) {
                Row(horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space10)) {
                    InfoCard("Kebutuhan cahaya", plant.light, Modifier.weight(1f))
                    InfoCard("Frekuensi siram", plant.waterText, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space10)) {
                    InfoCard("Tingkat kesulitan", plant.difficulty, Modifier.weight(1f))
                    InfoCard("Kelembapan", plant.humidity, Modifier.weight(1f))
                }
            }

            // ── Panduan ──
            if (plant.careGuide.isNotBlank()) {
                Column(verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space6)) {
                    Text("Panduan perawatan", style = GreenieTheme.typography.heading15, color = GreenieTheme.colors.text.primary)
                    Text(plant.careGuide, style = GreenieTheme.typography.body13Paragraph, color = GreenieTheme.colors.text.secondary)
                }
            }

            // ── Tombol utama (emas) ──
            val enabled = !state.isInCollection && !state.isAdding
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp) // tinggi tombol dari Figma
                    .clip(RoundedCornerShape(GreenieTheme.radius.radius26))
                    .background(if (state.isInCollection) GreenieTheme.colors.surface.cardMuted else GreenieTheme.colors.surface.accent)
                    .clickable(enabled = enabled, onClick = onAddToCollection),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = when {
                        state.isInCollection -> "Sudah ada di koleksimu"
                        state.isAdding -> "Menambahkan..."
                        else -> "Tambah ke koleksi saya"
                    },
                    style = GreenieTheme.typography.heading15,
                    color = if (state.isInCollection) GreenieTheme.colors.text.secondary else GreenieTheme.colors.text.onAccent,
                )
            }
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(GreenieTheme.radius.radius20)
    Column(
        modifier = modifier
            .clip(shape)
            .background(GreenieTheme.colors.surface.card)
            .border(1.dp, GreenieTheme.colors.border.default, shape)
            .padding(horizontal = GreenieTheme.spacing.space14, vertical = GreenieTheme.spacing.space12),
        verticalArrangement = Arrangement.spacedBy(3.dp), // jarak label–nilai dari Figma
    ) {
        Text(label, style = GreenieTheme.typography.body11, color = GreenieTheme.colors.text.secondary)
        Text(value, style = GreenieTheme.typography.title14, color = GreenieTheme.colors.text.primary)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12, heightDp = 900)
@Composable
private fun PustakaDetailContentPreview() {
    GreenieTheme {
        Box(Modifier.greenieCanvas()) {
            PustakaDetailContent(
                state = PustakaDetailUiState(
                    isLoading = false,
                    plant = LibraryPlant(
                        id = 1, name = "Monstera", latinName = "Monstera deliciosa", category = "Hias daun",
                        difficulty = "Mudah", light = "Terang, tidak langsung", lightTag = "Terang", waterDays = 7,
                        waterText = "Tiap 5–7 hari", humidity = "Sedang",
                        description = "Monstera deliciosa dikenal juga sebagai janda bolong karena daunnya yang besar dengan celah alami.",
                        careGuide = "Siram saat 3–5 cm media tanam bagian atas terasa kering.",
                    ),
                ),
                onBack = {},
                onAddToCollection = {},
            )
        }
    }
}
