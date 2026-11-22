package com.example.greeniepam.feature.profile.ui.profile

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.greeniepam.core.model.JournalPost
import com.example.greeniepam.core.model.PlantGradientType
import com.example.greeniepam.core.model.PlantItem
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.feature.profile.ui.profile.component.AddPlantDialog
import com.example.greeniepam.feature.profile.ui.profile.component.JournalPostRow
import com.example.greeniepam.feature.profile.ui.profile.component.PlantGridCard
import com.example.greeniepam.feature.profile.ui.profile.component.ProfileBioCard
import com.example.greeniepam.feature.profile.ui.profile.component.ProfileHeader
import com.example.greeniepam.feature.profile.ui.profile.component.ProfileTabRow
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onPostClick: (Long) -> Unit = {},
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Muat ulang setiap kali tab Koleksi dibuka (mis. setelah edit profil / buat jurnal)
    LaunchedEffect(Unit) { viewModel.refresh() }

    ProfileContent(
        state = state,
        onTabSelected = viewModel::selectTab,
        onEditProfileClick = onEditProfileClick,
        onFavoritesClick = onFavoritesClick,
        onAddPlantClick = { viewModel.showAddPlantDialog(true) },
        onPostClick = onPostClick,
        modifier = modifier,
    )

    if (state.showAddPlantDialog) {
        AddPlantDialog(
            onDismiss = { viewModel.showAddPlantDialog(false) },
            onAddPlant = viewModel::addPlant,
        )
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onTabSelected: (ProfileTab) -> Unit,
    onEditProfileClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onAddPlantClick: () -> Unit = {},
    onPostClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    // Kartu "Tambah" selalu ada di akhir grid tanaman
    val plantRows = (state.plants.map<PlantItem, PlantItem?> { it } + null).chunked(3)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space12),
    ) {
        item {
            Spacer(
                Modifier
                    .statusBarsPadding()
                    .height(GreenieTheme.spacing.space8),
            )
        }

        item {
            ProfileHeader(
                userName = state.userName,
                usernameHandle = state.usernameHandle,
                onEditProfileClick = onEditProfileClick,
                onFavoritesClick = onFavoritesClick,
            )
        }

        item {
            ProfileBioCard(
                plantCount = state.plantCount,
                sharedCount = state.sharedCount,
                likeCount = state.likeCount,
                bio = state.bio,
            )
        }

        state.error?.let { error ->
            item {
                Text(
                    text = error,
                    style = GreenieTheme.typography.body12Medium,
                    color = GreenieTheme.colors.text.danger,
                    modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal),
                )
            }
        }

        item {
            ProfileTabRow(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected,
            )
        }

        if (state.selectedTab == ProfileTab.Tanaman) {
            items(plantRows) { rowPlants ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = GreenieTheme.spacing.screenHorizontal),
                    horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space12),
                ) {
                    rowPlants.forEach { plant ->
                        if (plant != null) {
                            PlantGridCard(plant = plant, modifier = Modifier.weight(1f))
                        } else {
                            AddPlantCard(onClick = onAddPlantClick, modifier = Modifier.weight(1f))
                        }
                    }

                    repeat(3 - rowPlants.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        } else if (state.sharedPosts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Belum ada postingan yang dibagikan.",
                        style = GreenieTheme.typography.body12,
                        color = GreenieTheme.colors.text.secondary,
                    )
                }
            }
        } else {
            items(state.sharedPosts, key = { it.id }) { post ->
                JournalPostRow(
                    post = post,
                    onClick = { onPostClick(post.id) },
                    modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal),
                )
            }
        }
    }
}

@Composable
private fun AddPlantCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(GreenieTheme.radius.radius24)
    Column(
        modifier = modifier
            .height(150.dp) // sama dengan tinggi PlantGridCard
            .clip(shape)
            .border(1.dp, GreenieTheme.colors.border.accentSoft, shape)
            .background(GreenieTheme.colors.surface.cardFaint)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = null,
            tint = GreenieTheme.colors.icon.accent,
            modifier = Modifier.size(28.dp),
        )
        Spacer(Modifier.height(GreenieTheme.spacing.space6))
        Text(
            text = "Tambah",
            style = GreenieTheme.typography.label12,
            color = GreenieTheme.colors.text.accent,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun ProfileContentPreview() {
    GreenieTheme {
        ProfileContent(
            state = ProfileUiState(
                userName = "Dinda",
                usernameHandle = "@dinda.greenie",
                plantCount = 4,
                sharedCount = 6,
                likeCount = 240,
                bio = "Mahasiswa, pecinta tanaman hias.\nKoleksi kecil di kamar kost.",
                selectedTab = ProfileTab.Tanaman,
                plants = listOf(
                    PlantItem(1, "Monstera", PlantGradientType.GREEN),
                    PlantItem(2, "Kaktus mini", PlantGradientType.GREEN),
                    PlantItem(3, "Pothos", PlantGradientType.GOLD),
                    PlantItem(4, "Calathea", PlantGradientType.TEAL),
                ),
            ),
            onTabSelected = {},
        )
    }
}
