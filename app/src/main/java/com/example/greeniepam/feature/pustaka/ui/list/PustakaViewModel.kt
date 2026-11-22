package com.example.greeniepam.feature.pustaka.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greeniepam.core.data.LibraryRepository
import com.example.greeniepam.core.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PustakaViewModel(private val libraryRepository: LibraryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PustakaUiState())
    val uiState: StateFlow<PustakaUiState> = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val plants = libraryRepository.getAll()
                _uiState.update { it.copy(plants = plants) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun selectTab(tab: PustakaTab) = _uiState.update { it.copy(selectedTab = tab) }

    fun onQueryChange(query: String) = _uiState.update { it.copy(query = query) }
}
