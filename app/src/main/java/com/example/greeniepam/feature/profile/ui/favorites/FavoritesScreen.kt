package com.example.greeniepam.feature.profile.ui.favorites

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.greeniepam.core.model.JournalPost
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.core.theme.greenieCanvas
import com.example.greeniepam.feature.profile.ui.profile.component.JournalPostRow
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    onBack: () -> Unit,
    onPostClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // Dimuat ulang setiap kali halaman dibuka / kembali dari Detail Post
    LaunchedEffect(Unit) { viewModel.refresh() }

    FavoritesContent(
        state = state,
        onBack = onBack,
        onPostClick = onPostClick,
        onRemoveFavorite = viewModel::removeFavorite,
        modifier = modifier,
    )
}

@Composable
private fun FavoritesContent(
    state: FavoritesUiState,
    onBack: () -> Unit,
    onPostClick: (Long) -> Unit,
    onRemoveFavorite: (JournalPost) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .greenieCanvas()
            .statusBarsPadding(),
    ) {
        // ── Top bar ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GreenieTheme.spacing.screenHorizontal, vertical = GreenieTheme.spacing.space12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlassIconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Kembali",
                    tint = GreenieTheme.colors.icon.white,
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(
                text = "Favorit",
                style = GreenieTheme.typography.title17,
                color = GreenieTheme.colors.text.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            // Penyeimbang supaya judul tetap di tengah
            Spacer(Modifier.size(44.dp))
        }

        val message = when {
            state.isLoading && state.posts.isEmpty() -> "Memuat favorit..."
            state.error != null && state.posts.isEmpty() -> state.error
            state.posts.isEmpty() -> "Belum ada favorit.\nTekan ikon bookmark di postingan untuk menyimpannya di sini."
            else -> null
        }

        if (message != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(GreenieTheme.spacing.screenHorizontal),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = message,
                    style = GreenieTheme.typography.body13,
                    color = if (state.error != null) GreenieTheme.colors.text.danger else GreenieTheme.colors.text.secondary,
                    textAlign = TextAlign.Center,
                )
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = GreenieTheme.spacing.screenHorizontal,
                end = GreenieTheme.spacing.screenHorizontal,
                bottom = GreenieTheme.spacing.space24,
            ),
            verticalArrangement = Arrangement.spacedBy(GreenieTheme.spacing.space12),
        ) {
            items(state.posts, key = { it.id }) { post ->
                Box {
                    JournalPostRow(
                        post = post,
                        onClick = { onPostClick(post.id) },
                        showAuthor = true,
                    )
                    // Hapus dari favorit
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(GreenieTheme.spacing.space4)
                            .size(44.dp)
                            .clip(CircleShape)
                            .clickable { onRemoveFavorite(post) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Bookmark,
                            contentDescription = "Hapus dari favorit",
                            tint = GreenieTheme.colors.icon.accent,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassIconButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(GreenieTheme.colors.surface.glass)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) { content() }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun FavoritesContentPreview() {
    GreenieTheme {
        FavoritesContent(
            state = FavoritesUiState(
                isLoading = false,
                posts = listOf(
                    JournalPost(
                        id = 1, authorId = "a", authorName = "Raka", plantName = "Monstera",
                        journalDate = "1 Nov 2026", height = "32 cm", notes = "Daun baru akhirnya membuka.",
                        condition = "Segar", isShared = true, createdAt = "", likeCount = 12,
                        isLiked = false, commentCount = 3, isMine = false, isFavorited = true,
                    ),
                ),
            ),
            onBack = {},
            onPostClick = {},
            onRemoveFavorite = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun FavoritesEmptyPreview() {
    GreenieTheme {
        FavoritesContent(
            state = FavoritesUiState(isLoading = false),
            onBack = {},
            onPostClick = {},
            onRemoveFavorite = {},
        )
    }
}
