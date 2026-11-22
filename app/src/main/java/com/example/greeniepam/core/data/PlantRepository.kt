package com.example.greeniepam.core.data

import com.example.greeniepam.core.model.PlantGradientType
import com.example.greeniepam.core.model.PlantItem
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Akses tabel `plants` (koleksi tanaman user yang login). */
class PlantRepository(private val supabase: SupabaseClient) {

    private fun requireUserId(): String =
        supabase.auth.currentUserOrNull()?.id ?: error("User belum login")

    suspend fun getMyPlants(): List<PlantItem> =
        supabase.from(TABLE)
            .select(COLUMNS) {
                filter { eq("user_id", requireUserId()) }
                order("created_at", Order.ASCENDING)
            }
            .decodeList<PlantItem>()

    /** @param libraryId isi kalau tanaman ditambahkan dari Pustaka */
    suspend fun addPlant(name: String, gradientType: PlantGradientType, libraryId: Long? = null): PlantItem =
        supabase.from(TABLE)
            .insert(NewPlantRow(name = name, gradientType = gradientType, libraryId = libraryId)) { select(COLUMNS) }
            .decodeSingle<PlantItem>()

    suspend fun deletePlant(id: Long) {
        supabase.from(TABLE).delete { filter { eq("id", id) } }
    }

    private companion object {
        const val TABLE = "plants"
        val COLUMNS = Columns.list("id", "name", "gradient_type", "library_id")
    }
}

@Serializable
private data class NewPlantRow(
    val name: String,
    @SerialName("gradient_type") val gradientType: PlantGradientType,
    @SerialName("library_id") val libraryId: Long?,
)
