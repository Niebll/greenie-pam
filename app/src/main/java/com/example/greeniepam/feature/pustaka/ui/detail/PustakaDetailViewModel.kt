package com.example.greeniepam.feature.pustaka.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greeniepam.core.component.GreenieToastData
import com.example.greeniepam.core.data.LibraryRepository
import com.example.greeniepam.core.data.PlantRepository
import com.example.greeniepam.core.model.LibraryPlant
import com.example.greeniepam.core.util.toUserMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PustakaDetailUiState(
    val plant: LibraryPlant? = null,
    /** Sudah ada di koleksi user (dari Pustaka atau nama yang sama). */
    val isInCollection: Boolean = false,
    val isAdding: Boolean = false,
    val toast: GreenieToastData? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
)

class PustakaDetailViewModel(
    private val libraryRepository: LibraryRepository,
    private val plantRepository: PlantRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PustakaDetailUiState())
    val uiState: StateFlow<PustakaDetailUiState> = _uiState.asStateFlow()

    fun load(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val (plant, myPlants) = coroutineScope {
                    val plantJob = async { libraryRepository.getById(id) }
                    val mineJob = async { plantRepository.getMyPlants() }
                    plantJob.await() to mineJob.await()
                }
                _uiState.update {
                    it.copy(
                        plant = plant,
                        isInCollection = plant != null && myPlants.any { p ->
                            p.libraryId == id || p.name.equals(plant.name, ignoreCase = true)
                        },
                        error = if (plant == null) "Tanaman tidak ditemukan." else null,
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun addToCollection() {
        val state = _uiState.value
        val plant = state.plant ?: return
        if (state.isAdding || state.isInCollection) return

        viewModelScope.launch {
            _uiState.update { it.copy(isAdding = true) }
            try {
                plantRepository.addPlant(plant.name, plant.collectionGradient, libraryId = plant.id)
                _uiState.update {
                    it.copy(
                        isInCollection = true,
                        toast = GreenieToastData("Ditambahkan ke koleksi", "${plant.name} sekarang ada di tab Koleksi."),
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(toast = GreenieToastData("Gagal menambahkan", e.toUserMessage(), isSuccess = false))
                }
            } finally {
                _uiState.update { it.copy(isAdding = false) }
            }
        }
    }

    fun dismissToast() = _uiState.update { it.copy(toast = null) }
}
