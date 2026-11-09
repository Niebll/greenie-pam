package com.example.greeniepam.feature.home.ui.feed

import androidx.lifecycle.ViewModel
import com.example.greeniepam.feature.home.data.model.Post
import com.example.greeniepam.ui.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.abs

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun selectTab(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun refreshPosts() {
        loadPosts()
    }

    fun toggleLike(postId: Long) {
        val entry = JournalRepository.journals.find { abs(it.id.hashCode().toLong()) == postId }
        if (entry != null) {
            JournalRepository.toggleLike(entry.id)
            loadPosts()
        }
    }

    private fun loadPosts() {
        val journalPosts = JournalRepository.journals
            .filter { it.isSharedToFeed }
            .map { entry ->
                Post(
                    id = abs(entry.id.hashCode().toLong()),
                    authorName = "Dinda",
                    timeAgo = entry.date,
                    plantHeight = "Tinggi ${entry.height}",
                    leafCondition = entry.condition,
                    caption = entry.notes.ifBlank { "Perkembangan tanaman ${entry.plantName}." },
                    likeCount = entry.likesCount,
                    commentCount = entry.comments.size,
                    isGold = false,
                )
            }

        _uiState.update {
            it.copy(
                posts = journalPosts
            )
        }
    }
}
