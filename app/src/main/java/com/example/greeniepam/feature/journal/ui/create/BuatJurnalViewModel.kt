package com.example.greeniepam.feature.journal.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greeniepam.core.data.JournalRepository
import com.example.greeniepam.core.data.PlantRepository
import com.example.greeniepam.core.model.NewJournal
import com.example.greeniepam.core.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BuatJurnalUiState(
    /** Nama tanaman milik user, untuk pilihan chip. */
    val plantNames: List<String> = emptyList(),
    val isLoadingPlants: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
)

class BuatJurnalViewModel(
    private val journalRepository: JournalRepository,
    private val plantRepository: PlantRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BuatJurnalUiState())
    val uiState: StateFlow<BuatJurnalUiState> = _uiState.asStateFlow()

    fun loadPlants() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPlants = true) }
            try {
                val names = plantRepository.getMyPlants().map { it.name }
                _uiState.update { it.copy(plantNames = names) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isLoadingPlants = false) }
            }
        }
    }

    fun save(journal: NewJournal, onSuccess: () -> Unit) {
        if (_uiState.value.isSaving) return
        if (journal.plantName.isBlank()) {
            _uiState.update { it.copy(error = "Pilih tanaman dulu.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                journalRepository.createJournal(journal)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }
}
