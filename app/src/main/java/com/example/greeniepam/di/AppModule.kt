package com.example.greeniepam.di

import com.example.greeniepam.BuildConfig
import com.example.greeniepam.core.data.JournalRepository
import com.example.greeniepam.core.data.LibraryRepository
import com.example.greeniepam.core.data.PlantRepository
import com.example.greeniepam.core.data.UserRepository
import com.example.greeniepam.core.session.SessionManager
import com.example.greeniepam.core.session.SessionViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            defaultSerializer = KotlinXSerializer(Json { ignoreUnknownKeys = true })
            install(Postgrest)
            install(Auth)
            install(Realtime)
            install(Storage)
        }
    }

    // Data yang dipakai bersama oleh beberapa fitur
    single { SessionManager(get()) }
    single { UserRepository(get()) }
    single { PlantRepository(get()) }
    single { JournalRepository(get()) }
    single { LibraryRepository(get()) }

    viewModel { SessionViewModel(get()) }
}
