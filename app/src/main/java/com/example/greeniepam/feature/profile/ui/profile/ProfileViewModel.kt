package com.example.greeniepam.feature.profile.ui.profile

import androidx.lifecycle.ViewModel
import com.example.greeniepam.feature.profile.data.model.PlantGradientType
import com.example.greeniepam.feature.profile.data.model.PlantItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadMockProfile()
    }

    fun selectTab(tab: ProfileTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    private fun loadMockProfile() {
        _uiState.update {
            it.copy(
                plants = listOf(
                    PlantItem(1, "Monstera", PlantGradientType.GREEN),
                    PlantItem(2, "Kaktus mini", PlantGradientType.GREEN),
                    PlantItem(3, "Pothos", PlantGradientType.GOLD),
                    PlantItem(4, "Calathea", PlantGradientType.TEAL),
                    PlantItem(5, "Lidah mertua", PlantGradientType.GREEN),
                    PlantItem(6, "Aglaonema", PlantGradientType.GREEN),
                    PlantItem(7, "Sirih gading", PlantGradientType.GREEN),
                    PlantItem(8, "Philodendron", PlantGradientType.GOLD),
                    PlantItem(9, "Sukulen", PlantGradientType.GREEN),
                )
            )
        }
    }
}
