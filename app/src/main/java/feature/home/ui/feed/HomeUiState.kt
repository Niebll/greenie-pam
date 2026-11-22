package com.example.greeniepam.feature.home.ui.feed

import com.example.greeniepam.feature.home.data.model.Post

/**
 * State UI untuk layar Home.
 */
data class HomeUiState(
    val userName: String = "",
    val selectedTab: HomeTab = HomeTab.Populer,
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

enum class HomeTab(val label: String) {
    Populer("Populer"),
    Terbaru("Terbaru"),
}
