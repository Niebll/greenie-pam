package com.example.greeniepam.core.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SessionState { Loading, LoggedIn, LoggedOut }

/** Status login user, dipakai navigasi untuk memilih layar Masuk atau Beranda. */
class SessionManager(private val supabase: SupabaseClient) {

    val state: Flow<SessionState> = supabase.auth.sessionStatus.map { status ->
        when (status) {
            is SessionStatus.Initializing -> SessionState.Loading
            is SessionStatus.Authenticated -> SessionState.LoggedIn
            // Gagal refresh token (mis. offline) — sesi lama masih dipakai
            is SessionStatus.RefreshFailure -> SessionState.LoggedIn
            is SessionStatus.NotAuthenticated -> SessionState.LoggedOut
        }
    }

    suspend fun signOut() {
        supabase.auth.signOut()
    }
}

class SessionViewModel(private val sessionManager: SessionManager) : ViewModel() {

    val state: StateFlow<SessionState> = sessionManager.state
        .stateIn(viewModelScope, SharingStarted.Eagerly, SessionState.Loading)

    fun signOut() {
        viewModelScope.launch { runCatching { sessionManager.signOut() } }
    }
}
