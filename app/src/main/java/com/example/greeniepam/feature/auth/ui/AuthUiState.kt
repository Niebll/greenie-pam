package com.example.greeniepam.feature.auth.ui

data class AuthUiState(
    val isRegister: Boolean = false,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    /** Pesan info, mis. "cek email untuk konfirmasi". */
    val info: String? = null,
)
