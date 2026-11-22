package com.example.greeniepam.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Warna kartu katalog (kolom `plant_library.color`). */
@Serializable
enum class LibraryColor { GREEN, LIME, GOLD, TEAL }

/** Satu jenis tanaman di Pustaka (tabel `plant_library`). */
@Serializable
data class LibraryPlant(
    val id: Long,
    val name: String,
    @SerialName("latin_name") val latinName: String = "",
    val category: String,
    val difficulty: String,
    val light: String,
    @SerialName("light_tag") val lightTag: String,
    @SerialName("water_days") val waterDays: Int,
    @SerialName("water_text") val waterText: String,
    val humidity: String,
    val description: String = "",
    @SerialName("care_guide") val careGuide: String = "",
    val color: LibraryColor = LibraryColor.GREEN,
    val popularity: Int = 0,
    @SerialName("is_recommended") val isRecommended: Boolean = false,
    /** Foto tanaman (Wikimedia Commons); null = pakai gradien + ikon daun. */
    @SerialName("image_url") val imageUrl: String? = null,
) {
    /** Warna kartu saat tanaman ini masuk ke koleksi (tabel `plants`). */
    val collectionGradient: PlantGradientType
        get() = when (color) {
            LibraryColor.GOLD -> PlantGradientType.GOLD
            LibraryColor.TEAL -> PlantGradientType.TEAL
            LibraryColor.GREEN, LibraryColor.LIME -> PlantGradientType.GREEN
        }
}
