package com.example.greeniepam.feature.home.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greeniepam.core.data.JournalRepository
import com.example.greeniepam.core.data.UserRepository
import com.example.greeniepam.core.model.JournalPost
import com.example.greeniepam.core.util.timeAgo
import com.example.greeniepam.core.util.toUserMessage
import com.example.greeniepam.feature.home.data.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val journalRepository: JournalRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /** Data mentah dari Supabase; diurutkan ulang sesuai tab. */
    private var journals: List<JournalPost> = emptyList()

    fun selectTab(tab: HomeTab) {
        _uiState.update { it.copy(selectedTab = tab, posts = toPosts(journals, tab)) }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                journals = journalRepository.getFeed()
                val name = runCatching { userRepository.getMyProfile().name }.getOrNull()
                _uiState.update {
                    it.copy(
                        userName = name ?: it.userName,
                        posts = toPosts(journals, it.selectedTab),
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun toggleLike(postId: Long) {
        val target = journals.find { it.id == postId } ?: return
        val liked = !target.isLiked

        // Update tampilan dulu supaya terasa cepat, lalu kirim ke Supabase
        applyLike(postId, liked)
        viewModelScope.launch {
            try {
                journalRepository.setLiked(postId, liked)
            } catch (e: Exception) {
                applyLike(postId, !liked)
                _uiState.update { it.copy(error = e.toUserMessage()) }
            }
        }
    }

    private fun applyLike(postId: Long, liked: Boolean) {
        journals = journals.map {
            if (it.id != postId) it
            else it.copy(isLiked = liked, likeCount = (it.likeCount + if (liked) 1 else -1).coerceAtLeast(0))
        }
        _uiState.update { it.copy(posts = toPosts(journals, it.selectedTab)) }
    }

    private fun toPosts(source: List<JournalPost>, tab: HomeTab): List<Post> {
        val sorted = when (tab) {
            HomeTab.Populer -> source.sortedByDescending { it.likeCount }
            HomeTab.Terbaru -> source.sortedByDescending { it.createdAt }
        }
        return sorted.map { journal ->
            Post(
                id = journal.id,
                authorName = journal.authorName,
                timeAgo = timeAgo(journal.createdAt),
                plantHeight = journal.height.takeIf { it.isNotBlank() }?.let { "Tinggi $it" },
                leafCondition = journal.condition.takeIf { it.isNotBlank() },
                caption = journal.notes.ifBlank { "Perkembangan tanaman ${journal.plantName}." },
                likeCount = journal.likeCount,
                commentCount = journal.commentCount,
                isLiked = journal.isLiked,
                isGold = false,
            )
        }
    }
}
