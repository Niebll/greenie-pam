package com.example.greeniepam.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Warna kartu tanaman; disimpan sebagai teks di kolom `plants.gradient_type`. */
@Serializable
enum class PlantGradientType {
    GREEN,
    GOLD,
    TEAL,
}

/** Satu tanaman di koleksi user (tabel `plants`). */
@Serializable
data class PlantItem(
    val id: Long,
    val name: String,
    @SerialName("gradient_type") val gradientType: PlantGradientType = PlantGradientType.GREEN,
)
