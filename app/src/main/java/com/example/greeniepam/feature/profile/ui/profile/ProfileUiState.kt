package com.example.greeniepam.feature.profile.ui.profile

import com.example.greeniepam.feature.profile.data.model.PlantItem

enum class ProfileTab(val label: String) {
    Tanaman("Tanaman"),
    Dibagikan("Dibagikan"),
}

data class ProfileUiState(
    val userName: String = "Dinda",
    val usernameHandle: String = "@dinda.greenie",
    val plantCount: Int = 9,
    val sharedCount: Int = 6,
    val likeCount: Int = 240,
    val bio: String = "Mahasiswa, pecinta tanaman hias.\nKoleksi kecil di kamar kost.",
    val selectedTab: ProfileTab = ProfileTab.Tanaman,
    val plants: List<PlantItem> = emptyList(),
    val isLoading: Boolean = false,
)
