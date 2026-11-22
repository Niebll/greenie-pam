package com.example.greeniepam.core.data

import com.example.greeniepam.core.model.UserProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

/** Akses tabel `profiles` untuk user yang sedang login. */
class UserRepository(private val supabase: SupabaseClient) {

    private fun requireUserId(): String =
        supabase.auth.currentUserOrNull()?.id ?: error("User belum login")

    suspend fun getMyProfile(): UserProfile =
        supabase.from("profiles")
            .select { filter { eq("id", requireUserId()) } }
            .decodeSingle<UserProfile>()

    suspend fun updateMyProfile(name: String, username: String?, bio: String) {
        supabase.from("profiles").update({
            set("name", name)
            set("username", username)
            set("bio", bio)
        }) {
            filter { eq("id", requireUserId()) }
        }
    }
}
