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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.greeniepam.core.theme.GreenieGradients
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas

data class CollectionPlant(
    val id: String,
    val name: String,
    val gradient: Brush
)

data class SharedPost(
    val id: String,
    val likes: Int,
    val gradient: Brush
)

private val sampleCollectionPlants = listOf(
    CollectionPlant("monstera", "Monstera", GreenieGradients.cardGreen),
    CollectionPlant("kaktus", "Kaktus mini", GreenieGradients.cardGreen),
    CollectionPlant("pothos", "Pothos", GreenieGradients.cardGold),
    CollectionPlant("calathea", "Calathea", Brush.verticalGradient(listOf(Color(0xFF2A7B88), Color(0xFF143E46)))),
    CollectionPlant("lidah-mertua", "Lidah mertua", GreenieGradients.cardGreen),
    CollectionPlant("aglaonema", "Aglaonema", GreenieGradients.cardGreen),
    CollectionPlant("sirih-gading", "Sirih Gading", GreenieGradients.cardGreen),
    CollectionPlant("aloevera", "Aloevera", GreenieGradients.cardGold)
)

private val initialSharedPosts = listOf(
    SharedPost("post-1", 125, GreenieGradients.cardGreen),
    SharedPost("post-2", 88, GreenieGradients.cardGreen),
    SharedPost("post-3", 64, GreenieGradients.cardGold),
    SharedPost("post-4", 40, Brush.verticalGradient(listOf(Color(0xFF2A7B88), Color(0xFF143E46)))),
    SharedPost("post-5", 31, GreenieGradients.cardGreen),
    SharedPost("post-6", 19, GreenieGradients.cardGreen)
)

private val koleksiTabs = listOf("Tanaman", "Dibagikan", "Favorit")

@Composable
fun KoleksiScreen(
    journalList: List<JournalEntry>,
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToDetailPost: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var selectedTab by remember(journalList) {
        mutableStateOf(if (journalList.isNotEmpty()) "Dibagikan" else "Tanaman")
    }

    val displaySharedPosts = remember(journalList) {
        val createdPosts = journalList.map { entry ->
            SharedPost(
                id = entry.id,
                likes = 0,
                gradient = GreenieGradients.cardGreen
            )
        }
        createdPosts + initialSharedPosts
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .greenieCanvas(),
        contentPadding = PaddingValues(
            top = GreenieTheme.spacing.screenTop,
            bottom = 100.dp
        )
    ) {
        // User Profile Header & Bio Card
        item {
            Column(
                modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal)
            ) {
                // User Profile Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar Box
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(GreenieGradients.cardGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Spa,
                            contentDescription = "Avatar",
                            tint = GreenieTheme.colors.icon.primary.copy(alpha = 0.8f),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(GreenieTheme.spacing.space16))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Dinda",
                            style = GreenieTheme.typography.heading28,
                            color = GreenieTheme.colors.text.primary
                        )
                        Text(
                            text = "@dinda.greenie",
                            style = GreenieTheme.typography.body13,
                            color = GreenieTheme.colors.text.secondary
                        )

                        Spacer(modifier = Modifier.height(GreenieTheme.spacing.space8))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space8)
                        ) {
                            // Edit Profil Button
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(GreenieTheme.radius.full))
                                    .border(1.dp, GreenieTheme.colors.border.emphasis, RoundedCornerShape(GreenieTheme.radius.full))
                                    .clickable {
                                        Toast.makeText(context, "Edit profil", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Edit profil",
                                    style = GreenieTheme.typography.title13,
                                    color = GreenieTheme.colors.text.primary
                                )
                            }

                            // Bagikan Button
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(GreenieTheme.radius.full))
                                    .border(1.dp, GreenieTheme.colors.border.accent, RoundedCornerShape(GreenieTheme.radius.full))
                                    .clickable {
                                        Toast.makeText(context, "Bagikan profil", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Bagikan",
                                    style = GreenieTheme.typography.title13,
                                    color = GreenieTheme.colors.text.accent
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space20))

                // Stats & Bio Card
                val cardShape = RoundedCornerShape(GreenieTheme.radius.radius24)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(cardShape)
                        .background(GreenieTheme.colors.surface.card)
                        .border(1.dp, GreenieTheme.colors.border.subtle, cardShape)
                        .padding(GreenieTheme.spacing.space16)
                ) {
                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(count = "8", label = "Tanaman")
                        StatItem(count = "${displaySharedPosts.size}", label = "Dibagikan")
                        StatItem(count = "240", label = "Suka")
                    }

                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space12))
                    HorizontalDivider(color = GreenieTheme.colors.border.subtle, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space12))

                    // Bio Teks
                    Text(
                        text = "Mahasiswa, pecinta tanaman hias.",
                        style = GreenieTheme.typography.body13Medium,
                        color = GreenieTheme.colors.text.primary
                    )
                    Spacer(modifier = Modifier.height(GreenieTheme.spacing.space2))
                    Text(
                        text = "Koleksi kecil di kamar kost.",
                        style = GreenieTheme.typography.body13,
                        color = GreenieTheme.colors.text.secondary
                    )
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space20))

                // Tabs (Tanaman, Dibagikan, Favorit)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space24),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    koleksiTabs.forEach { tab ->
                        val isSelected = tab == selectedTab
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { selectedTab = tab }
                                .padding(vertical = GreenieTheme.spacing.space4)
                        ) {
                            Text(
                                text = tab,
                                style = if (isSelected) GreenieTheme.typography.title15 else GreenieTheme.typography.body13Medium,
                                color = if (isSelected) GreenieTheme.colors.text.accent else GreenieTheme.colors.text.secondary
                            )
                            Spacer(modifier = Modifier.height(GreenieTheme.spacing.space4))
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(GreenieTheme.radius.full))
                                        .background(GreenieTheme.colors.surface.accent)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(GreenieTheme.spacing.space16))
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            "Tanaman" -> {
                item {
                    PlantGridSection(
                        plants = sampleCollectionPlants,
                        onPlantClick = { onNavigateToDetail(it) }
                    )
                }
            }
            "Dibagikan" -> {
                item {
                    SharedPostsGridSection(
                        posts = displaySharedPosts,
                        onPostClick = { postId ->
                            onNavigateToDetailPost(postId)
                        }
                    )
                }
            }
            "Favorit" -> {
                item {
                    PlantGridSection(
                        plants = sampleCollectionPlants.take(3),
                        onPlantClick = { onNavigateToDetail(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    count: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            style = GreenieTheme.typography.heading20,
            color = GreenieTheme.colors.text.primary
        )
        Text(
            text = label,
            style = GreenieTheme.typography.body12,
            color = GreenieTheme.colors.text.secondary
        )
    }
}

@Composable
private fun PlantGridSection(
    plants: List<CollectionPlant>,
    onPlantClick: (String) -> Unit
) {
    val rows = plants.chunked(3)
    Column(
        modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        rows.forEach { rowPlants ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowPlants.forEach { plant ->
                    Box(modifier = Modifier.weight(1f)) {
                        CollectionPlantCard(
                            plant = plant,
                            onClick = { onPlantClick(plant.id) }
                        )
                    }
                }
                repeat(3 - rowPlants.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CollectionPlantCard(
    plant: CollectionPlant,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(20.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(cardShape)
            .background(plant.gradient)
            .border(1.dp, GreenieTheme.colors.border.subtle, cardShape)
            .clickable(onClick = onClick)
            .padding(GreenieTheme.spacing.space12),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Spa,
                contentDescription = null,
                tint = GreenieTheme.colors.icon.primary.copy(alpha = 0.8f),
                modifier = Modifier.size(36.dp)
            )
        }

        Text(
            text = plant.name,
            style = GreenieTheme.typography.heading15,
            color = GreenieTheme.colors.text.primary,
            maxLines = 1
        )
    }
}

@Composable
private fun SharedPostsGridSection(
    posts: List<SharedPost>,
    onPostClick: (String) -> Unit
) {
    val rows = posts.chunked(3)
    Column(
        modifier = Modifier.padding(horizontal = GreenieTheme.spacing.screenHorizontal),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        rows.forEach { rowPosts ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowPosts.forEach { post ->
                    Box(modifier = Modifier.weight(1f)) {
                        SharedPostCard(
                            post = post,
                            onClick = { onPostClick(post.id) }
                        )
                    }
                }
                repeat(3 - rowPosts.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SharedPostCard(
    post: SharedPost,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(20.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(cardShape)
            .background(post.gradient)
            .border(1.dp, GreenieTheme.colors.border.subtle, cardShape)
            .clickable(onClick = onClick)
            .padding(GreenieTheme.spacing.space12),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Spa,
                contentDescription = null,
                tint = GreenieTheme.colors.icon.primary.copy(alpha = 0.8f),
                modifier = Modifier.size(36.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Suka",
                tint = GreenieTheme.colors.icon.primary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${post.likes}",
                style = GreenieTheme.typography.title12,
                color = GreenieTheme.colors.text.primary
            )
        }
    }
}
