package com.example.greeniepam.feature.profile.data.model

enum class PlantGradientType {
    GREEN,
    GOLD,
    TEAL,
}

data class PlantItem(
    val id: Long,
    val name: String,
    val gradientType: PlantGradientType = PlantGradientType.GREEN,
)
