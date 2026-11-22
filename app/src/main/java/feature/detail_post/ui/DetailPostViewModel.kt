package com.example.greeniepam.feature.detail_post.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greeniepam.core.data.JournalRepository
import com.example.greeniepam.core.model.JournalComment
import com.example.greeniepam.core.model.JournalPost
import com.example.greeniepam.core.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailPostUiState(
    val post: JournalPost? = null,
    val comments: List<JournalComment> = emptyList(),
    val commentText: String = "",
    val isSending: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
)

class DetailPostViewModel(private val journalRepository: JournalRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailPostUiState())
    val uiState: StateFlow<DetailPostUiState> = _uiState.asStateFlow()

    private var journalId: Long? = null

    fun load(id: Long) {
        journalId = id
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val post = journalRepository.getJournal(id)
                val comments = if (post != null) journalRepository.getComments(id) else emptyList()
                _uiState.update {
                    it.copy(
                        post = post,
                        comments = comments,
                        error = if (post == null) "Postingan tidak ditemukan." else null,
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onCommentTextChange(value: String) = _uiState.update { it.copy(commentText = value) }

    fun toggleLike() {
        val post = _uiState.value.post ?: return
        val liked = !post.isLiked
        setLikeLocally(liked)
        viewModelScope.launch {
            try {
                journalRepository.setLiked(post.id, liked)
            } catch (e: Exception) {
                setLikeLocally(!liked)
                _uiState.update { it.copy(error = e.toUserMessage()) }
            }
        }
    }

    fun sendComment() {
        val id = journalId ?: return
        val text = _uiState.value.commentText.trim()
        if (text.isEmpty() || _uiState.value.isSending) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true) }
            try {
                journalRepository.addComment(id, text)
                val comments = journalRepository.getComments(id)
                _uiState.update {
                    it.copy(
                        commentText = "",
                        comments = comments,
                        post = it.post?.copy(commentCount = comments.size),
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isSending = false) }
            }
        }
    }

    private fun setLikeLocally(liked: Boolean) {
        _uiState.update { state ->
            val post = state.post ?: return@update state
            state.copy(
                post = post.copy(
                    isLiked = liked,
                    likeCount = (post.likeCount + if (liked) 1 else -1).coerceAtLeast(0),
                )
            )
        }
    }
}
