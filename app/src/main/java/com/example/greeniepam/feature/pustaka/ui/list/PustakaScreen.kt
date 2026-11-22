package com.example.greeniepam.feature.pustaka.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.greeniepam.core.component.LibraryPlantImage
import com.example.greeniepam.core.component.LibraryPlantRowCard
import com.example.greeniepam.core.model.LibraryColor
import com.example.greeniepam.core.model.LibraryPlant
import com.example.greeniepam.core.theme.GreenieGradients
import com.example.greeniepam.core.theme.GreenieTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PustakaScreen(
    onPlantClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PustakaViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.refresh() }

    PustakaContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onTabSelected = viewModel::selectTab,
        onPlantClick = { onPlantClick(it.id) },
        modifier = modifier,
    )
}

@Composable
private fun PustakaContent(
    state: PustakaUiState,
    onQueryChange: (String) -> Unit,
    onTabSelected: (PustakaTab) -> Unit,
    onPlantClick: (LibraryPlant) -> Unit,
    modifier: Modifier = Modifier,
) {
    val horizontal = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = GreenieTheme.spacing.screenTop, bottom = 120.dp), // 120dp: ruang navbar
        verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space16),
    ) {
        // ── Header ──
        item {
            Column(horizontal, verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space4)) {
                Text("Pustaka", style = GreenieTheme.typography.heading28, color = GreenieTheme.colors.text.primary)
                Text(
                    "Temukan tanaman favoritmu",
                    style = GreenieTheme.typography.body14,
                    color = GreenieTheme.colors.text.secondary,
                )
            }
        }

        // ── Cari ──
        item { SearchField(query = state.query, onQueryChange = onQueryChange, modifier = horizontal) }

        when {
            state.isLoading && state.plants.isEmpty() -> item { InfoText("Memuat pustaka...", horizontal) }
            state.error != null && state.plants.isEmpty() -> item { InfoText(state.error, horizontal, isError = true) }

            // ── Mode cari: daftar hasil ──
            state.isSearching -> {
                val results = state.searchResults
                if (results.isEmpty()) {
                    item { InfoText("Tidak ada tanaman yang cocok dengan \"${state.query.trim()}\".", horizontal) }
                } else {
                    item { SectionTitle("Hasil pencarian", horizontal) }
                    items(results, key = { "cari-${it.id}" }) { plant ->
                        LibraryPlantRowCard(plant = plant, onClick = { onPlantClick(plant) }, modifier = horizontal)
                    }
                }
            }

            else -> {
                // ── Tab kategori ──
                item { PustakaTabs(selected = state.selectedTab, onSelect = onTabSelected, modifier = horizontal) }

                // ── Kartu besar (bisa digeser) ──
                item {
                    val tabPlants = state.tabPlants
                    if (tabPlants.isEmpty()) {
                        InfoText("Belum ada tanaman di kategori ini.", horizontal)
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = GreenieTheme.spacing.screenHorizontal),
                            horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space12),
                        ) {
                            items(tabPlants, key = { it.id }) { plant ->
                                PopularPlantCard(plant = plant, onClick = { onPlantClick(plant) })
                            }
                        }
                    }
                }

                // ── Rekomendasi ──
                if (state.recommended.isNotEmpty()) {
                    item { SectionTitle("Rekomendasi untukmu", horizontal) }
                    items(state.recommended, key = { "rek-${it.id}" }) { plant ->
                        LibraryPlantRowCard(plant = plant, onClick = { onPlantClick(plant) }, modifier = horizontal)
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(GreenieTheme.radius.full)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp) // tinggi kolom cari dari Figma
            .clip(shape)
            .background(GreenieTheme.colors.surface.card)
            .border(1.dp, GreenieTheme.colors.border.default, shape)
            .padding(horizontal = GreenieTheme.spacing.space18),
        horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space10),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = GreenieTheme.colors.text.secondary,
            modifier = Modifier.size(20.dp),
        )
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = GreenieTheme.typography.body14.copy(color = GreenieTheme.colors.text.primary),
            cursorBrush = SolidColor(GreenieTheme.colors.text.accent),
            modifier = Modifier.weight(1f),
            decorationBox = { inner ->
                if (query.isEmpty()) {
                    Text("Cari tanaman apa saja", style = GreenieTheme.typography.body14, color = GreenieTheme.colors.text.secondary)
                }
                inner()
            },
        )
    }
}

@Composable
private fun PustakaTabs(selected: PustakaTab, onSelect: (PustakaTab) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space22)) {
        PustakaTab.entries.forEach { tab ->
            val isSelected = tab == selected
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .clickable { onSelect(tab) }
                    .padding(top = GreenieTheme.spacing.space12),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space8),
            ) {
                Text(
                    text = tab.label,
                    style = if (isSelected) GreenieTheme.typography.title14 else GreenieTheme.typography.body14Medium,
                    color = if (isSelected) GreenieTheme.colors.text.accent else GreenieTheme.colors.text.primary,
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(2.dp) // garis bawah tab aktif
                        .background(if (isSelected) GreenieTheme.colors.surface.accent else Color.Transparent),
                )
            }
        }
    }
}

/** Kartu besar 104×168 (Figma › Pustaka › "Kartu populer"). */
@Composable
private fun PopularPlantCard(plant: LibraryPlant, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(width = 104.dp, height = 168.dp) // ukuran kartu dari Figma
            .clip(RoundedCornerShape(GreenieTheme.radius.radius22))
            .clickable(onClick = onClick),
    ) {
        LibraryPlantImage(plant = plant, leafSize = 36.dp, modifier = Modifier.matchParentSize())
        Box(
            Modifier
                .matchParentSize()
                .background(GreenieGradients.photoShade),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(GreenieTheme.spacing.space12),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(plant.name, style = GreenieTheme.typography.title14, color = GreenieTheme.colors.text.primary, maxLines = 1)
            Text(plant.difficulty, style = GreenieTheme.typography.body11, color = GreenieTheme.colors.text.secondary)
        }
    }
}

@Composable
private fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(text, style = GreenieTheme.typography.heading17, color = GreenieTheme.colors.text.primary, modifier = modifier)
}

@Composable
private fun InfoText(text: String, modifier: Modifier = Modifier, isError: Boolean = false) {
    Text(
        text = text,
        style = GreenieTheme.typography.body13,
        color = if (isError) GreenieTheme.colors.text.danger else GreenieTheme.colors.text.secondary,
        modifier = modifier.padding(vertical = GreenieTheme.spacing.space12),
    )
}

/* ---------- preview ---------- */

private fun previewPlant(id: Long, name: String, latin: String, cat: String, diff: String, color: LibraryColor, rec: Boolean = false) =
    LibraryPlant(
        id = id, name = name, latinName = latin, category = cat, difficulty = diff,
        light = "Terang, tidak langsung", lightTag = "Teduh", waterDays = 5, waterText = "Tiap 5–7 hari",
        humidity = "Sedang", color = color, isRecommended = rec,
    )

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12, heightDp = 800)
@Composable
private fun PustakaContentPreview() {
    GreenieTheme {
        PustakaContent(
            state = PustakaUiState(
                isLoading = false,
                plants = listOf(
                    previewPlant(1, "Monstera", "Monstera deliciosa", "Hias daun", "Mudah", LibraryColor.GREEN),
                    previewPlant(2, "Kaktus", "Cactaceae", "Sukulen", "Mudah", LibraryColor.LIME),
                    previewPlant(3, "Calathea", "Calathea orbifolia", "Hias daun", "Sedang", LibraryColor.GOLD),
                    previewPlant(4, "Lidah mertua", "Sansevieria", "Hias daun", "Mudah", LibraryColor.TEAL, rec = true),
                    previewPlant(5, "Pothos", "Epipremnum aureum", "Hias daun", "Mudah", LibraryColor.LIME, rec = true),
                ),
            ),
            onQueryChange = {},
            onTabSelected = {},
            onPlantClick = {},
        )
    }
}
