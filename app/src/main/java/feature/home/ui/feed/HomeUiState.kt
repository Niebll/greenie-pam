package com.example.greeniepam.feature.home.ui.feed

import com.example.greeniepam.feature.home.data.model.Post

/**
 * State UI untuk layar Home.
 */
data class HomeUiState(
    val userName: String = "Dinda",
    val selectedTab: HomeTab = HomeTab.Populer,
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
)

enum class HomeTab(val label: String) {
    Populer("Populer"),
    Terbaru("Terbaru"),
}
