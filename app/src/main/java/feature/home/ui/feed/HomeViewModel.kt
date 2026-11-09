package com.example.greeniepam.feature.home.ui.feed

import androidx.lifecycle.ViewModel
import com.example.greeniepam.feature.home.data.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMockData()
    }

    fun selectTab(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    private fun loadMockData() {
        _uiState.update {
            it.copy(
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
            )
        }
    }
}
