package com.example.greeniepam.feature.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greeniepam.core.util.toUserMessage
import com.example.greeniepam.feature.auth.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Setelah login berhasil, navigasi pindah otomatis karena
 * status sesi berubah (lihat AppNavGraph), jadi ViewModel ini tidak bernavigasi.
 */
class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, error = null) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, error = null) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value, error = null) }

    fun toggleMode() = _uiState.update {
        it.copy(isRegister = !it.isRegister, error = null, info = null)
    }

    fun submit() {
        val state = _uiState.value
        val email = state.email.trim()
        val name = state.name.trim()

        val validationError = when {
            state.isRegister && name.isBlank() -> "Nama wajib diisi."
            !email.contains("@") -> "Format email tidak valid."
            state.password.length < 6 -> "Password minimal 6 karakter."
            else -> null
        }
        if (validationError != null) {
            _uiState.update { it.copy(error = validationError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, info = null) }
            try {
                if (state.isRegister) {
                    val loggedIn = authRepository.signUp(name, email, state.password)
                    if (!loggedIn) {
                        _uiState.update {
                            it.copy(
                                isRegister = false,
                                password = "",
                                info = "Akun dibuat. Cek email kamu untuk konfirmasi, lalu masuk.",
                            )
                        }
                    }
                } else {
                    authRepository.signIn(email, state.password)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
