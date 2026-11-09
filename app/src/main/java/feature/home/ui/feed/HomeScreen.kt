package com.example.greeniepam.feature.home.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.greeniepam.core.theme.GreenieTheme
import com.example.greeniepam.feature.home.data.model.Post
import com.example.greeniepam.feature.home.ui.feed.component.HomeHeader
import com.example.greeniepam.feature.home.ui.feed.component.HomeTabRow
import com.example.greeniepam.feature.home.ui.feed.component.PostCard
import org.koin.androidx.compose.koinViewModel

/**
 * Layar Home — entry point yang mengambil ViewModel dari Koin.
 */
@Composable
fun HomeScreen(
    onPostClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        onTabSelected = viewModel::selectTab,
        onPostClick = onPostClick,
        modifier = modifier,
    )
}

/**
 * Konten stateless Home — bisa dipakai untuk @Preview.
 */
@Composable
private fun HomeContent(
    state: HomeUiState,
    onTabSelected: (HomeTab) -> Unit,
    onPostClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp), // ruang untuk navbar melayang
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // ── Status bar padding ──
        item {
            Spacer(
                Modifier
                    .statusBarsPadding()
                    .height(4.dp),
            )
        }

        // ── Header: Halo, Nama ──
        item {
            HomeHeader(userName = state.userName)
        }

        // ── Tab: Populer / Terbaru ──
        item {
            HomeTabRow(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected,
            )
        }

        // ── Feed postingan ──
        items(
            items = state.posts,
            key = { it.id },
        ) { post ->
            PostCard(
                post = post,
                onClick = { onPostClick(post.id) },
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                ),
            )
        }
    }
}

/* ────────────────────────────────────────────────────────────────────────── */
/*  Preview                                                                  */
/* ────────────────────────────────────────────────────────────────────────── */

@Preview(showBackground = true, backgroundColor = 0xFF0C1A12)
@Composable
private fun HomeContentPreview() {
    GreenieTheme {
        HomeContent(
            state = HomeUiState(
                userName = "Dinda",
                selectedTab = HomeTab.Populer,
                posts = listOf(
                    Post(
                        id = 1,
                        authorName = "Dinda",
                        timeAgo = "45 menit lalu",
                        plantHeight = "Tinggi 32 cm",
                        leafCondition = "Daun segar",
                        caption = "Daun baru akhirnya membuka. Pelan tapi pasti!",
                        likeCount = 125,
                        commentCount = 24,
                        isGold = false,
                    ),
                    Post(
                        id = 2,
                        authorName = "Raka",
                        timeAgo = "1 jam lalu",
                        caption = "Tanaman monstera makin lebat.",
                        likeCount = 89,
                        commentCount = 12,
                        isGold = true,
                    ),
                ),
            ),
            onTabSelected = {},
        )
    }
}
