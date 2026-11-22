package com.example.greeniepam.feature.profile.ui.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greeniepam.core.data.UserRepository
import com.example.greeniepam.core.session.SessionManager
import com.example.greeniepam.core.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val name: String = "",
    val username: String = "",
    val bio: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
)

class EditProfileViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val profile = userRepository.getMyProfile()
                _uiState.update {
                    it.copy(name = profile.name, username = profile.username.orEmpty(), bio = profile.bio)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, error = null) }
    fun onBioChange(value: String) = _uiState.update { it.copy(bio = value, error = null) }

    fun onUsernameChange(value: String) = _uiState.update {
        // Username: huruf kecil, angka, titik, garis bawah
        it.copy(username = value.lowercase().filter { c -> c.isLetterOrDigit() || c == '.' || c == '_' }, error = null)
    }

    fun save(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.isSaving || state.isLoading) return
        if (state.name.isBlank()) {
            _uiState.update { it.copy(error = "Nama wajib diisi.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                userRepository.updateMyProfile(
                    name = state.name.trim(),
                    username = state.username.trim().ifBlank { null },
                    bio = state.bio.trim(),
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    /** Setelah keluar, AppNavGraph otomatis kembali ke layar Masuk. */
    fun signOut() {
        viewModelScope.launch {
            runCatching { sessionManager.signOut() }
                .onFailure { e -> _uiState.update { it.copy(error = e.toUserMessage()) } }
        }
    }
}
