package com.example.greeniepam.ui

import androidx.compose.runtime.Composable
import com.example.greeniepam.feature.home.ui.feed.HomeScreen as HomeFeedScreen

@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit
) {
    HomeFeedScreen(
        onPostClick = { postId ->
            onNavigateToDetail(postId.toString())
        }
    )
}
