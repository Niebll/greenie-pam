package com.example.greeniepam.feature.pustaka.ui.list

import com.example.greeniepam.core.model.LibraryPlant

/** Tab di bawah kolom cari. `category` null = Populer (semua, urut popularitas). */
enum class PustakaTab(val label: String, val category: String?) {
    Populer("Populer", null),
    HiasDaun("Hias daun", "Hias daun"),
    Sukulen("Sukulen", "Sukulen"),
    Berbunga("Berbunga", "Berbunga"),
}

data class PustakaUiState(
    val plants: List<LibraryPlant> = emptyList(),
    val selectedTab: PustakaTab = PustakaTab.Populer,
    val query: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
) {
    /** Kartu besar yang bisa digeser, sesuai tab terpilih. */
    val tabPlants: List<LibraryPlant>
        get() = selectedTab.category
            ?.let { cat -> plants.filter { it.category == cat } }
            ?: plants.take(POPULAR_LIMIT)

    val recommended: List<LibraryPlant>
        get() = plants.filter { it.isRecommended }

    /** Hasil cari berdasarkan nama atau nama latin. */
    val searchResults: List<LibraryPlant>
        get() {
            val q = query.trim()
            return plants.filter {
                it.name.contains(q, ignoreCase = true) || it.latinName.contains(q, ignoreCase = true)
            }
        }

    val isSearching: Boolean get() = query.isNotBlank()

    private companion object {
        const val POPULAR_LIMIT = 8
    }
}
