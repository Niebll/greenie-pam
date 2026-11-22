package com.example.greeniepam.feature.profile.ui.profile

import com.example.greeniepam.core.model.JournalPost
import com.example.greeniepam.core.model.PlantItem

enum class ProfileTab(val label: String) {
    Tanaman("Tanaman"),
    Dibagikan("Dibagikan"),
}

data class ProfileUiState(
    val userName: String = "",
    val usernameHandle: String = "",
    val plantCount: Int = 0,
    val sharedCount: Int = 0,
    val likeCount: Int = 0,
    val bio: String = "",
    val selectedTab: ProfileTab = ProfileTab.Tanaman,
    val plants: List<PlantItem> = emptyList(),
    val sharedPosts: List<JournalPost> = emptyList(),
    val showAddPlantDialog: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
