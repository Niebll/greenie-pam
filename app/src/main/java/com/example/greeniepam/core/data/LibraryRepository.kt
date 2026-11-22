package com.example.greeniepam.core.data

import com.example.greeniepam.core.model.LibraryPlant
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

/** Akses katalog Pustaka (`plant_library`). Isi katalog sama untuk semua user. */
class LibraryRepository(private val supabase: SupabaseClient) {

    /** Seluruh katalog, urut dari yang paling populer. */
    suspend fun getAll(): List<LibraryPlant> =
        supabase.from(LIBRARY)
            .select { order("popularity", Order.DESCENDING) }
            .decodeList<LibraryPlant>()

    suspend fun getById(id: Long): LibraryPlant? =
        supabase.from(LIBRARY)
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<LibraryPlant>()

    private companion object {
        const val LIBRARY = "plant_library"
    }
}
