package com.example.greeniepam.feature.profile.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greeniepam.core.data.JournalRepository
import com.example.greeniepam.core.model.JournalPost
import com.example.greeniepam.core.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val posts: List<JournalPost> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

class FavoritesViewModel(private val journalRepository: JournalRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val posts = journalRepository.getFavorites()
                _uiState.update { it.copy(posts = posts) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /** Hapus dari favorit langsung dari daftar; dikembalikan kalau gagal. */
    fun removeFavorite(post: JournalPost) {
        val before = _uiState.value.posts
        _uiState.update { it.copy(posts = before.filterNot { p -> p.id == post.id }) }
        viewModelScope.launch {
            try {
                journalRepository.setFavorited(post.id, favorited = false)
            } catch (e: Exception) {
                _uiState.update { it.copy(posts = before, error = e.toUserMessage()) }
            }
        }
    }
}
