package com.example.greeniepam.feature.auth.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository(private val supabase: SupabaseClient) {

    suspend fun signIn(email: String, password: String) {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    /**
     * Daftar akun baru. Nama disimpan di metadata, lalu trigger di database
     * otomatis membuat baris `profiles`.
     *
     * @return true kalau user langsung login; false kalau harus konfirmasi email dulu.
     */
    suspend fun signUp(name: String, email: String, password: String): Boolean {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
            data = buildJsonObject { put("name", name) }
        }
        return supabase.auth.currentSessionOrNull() != null
    }
}
