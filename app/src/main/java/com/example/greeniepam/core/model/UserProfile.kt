package com.example.greeniepam.core.model

import kotlinx.serialization.Serializable

/** Profil user (tabel `profiles`). */
@Serializable
data class UserProfile(
    val id: String,
    val name: String = "",
    val username: String? = null,
    val bio: String = "",
)
