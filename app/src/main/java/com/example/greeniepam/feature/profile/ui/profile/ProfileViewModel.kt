package com.example.greeniepam.feature.profile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greeniepam.core.data.JournalRepository
import com.example.greeniepam.core.data.PlantRepository
import com.example.greeniepam.core.data.UserRepository
import com.example.greeniepam.core.model.PlantGradientType
import com.example.greeniepam.core.util.toUserMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val plantRepository: PlantRepository,
    private val journalRepository: JournalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun selectTab(tab: ProfileTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // coroutineScope supaya error dari query paralel tertangkap catch di bawah
                // (tanpa ini, error dari async membuat aplikasi crash)
                val (profile, plants, journals) = coroutineScope {
                    val profileJob = async { userRepository.getMyProfile() }
                    val plantsJob = async { plantRepository.getMyPlants() }
                    val journalsJob = async { journalRepository.getMyJournals() }
                    Triple(profileJob.await(), plantsJob.await(), journalsJob.await())
                }
                val shared = journals.filter { it.isShared }
                _uiState.update {
                    it.copy(
                        userName = profile.name,
                        usernameHandle = profile.username?.let { u -> "@$u" }.orEmpty(),
                        bio = profile.bio,
                        plants = plants,
                        plantCount = plants.size,
                        sharedPosts = shared,
                        sharedCount = shared.size,
                        likeCount = shared.sumOf { j -> j.likeCount },
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage()) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun showAddPlantDialog(show: Boolean) {
        _uiState.update { it.copy(showAddPlantDialog = show) }
    }

    fun addPlant(name: String, gradientType: PlantGradientType) {
        viewModelScope.launch {
            try {
                val plant = plantRepository.addPlant(name, gradientType)
                _uiState.update {
                    val plants = it.plants + plant
                    it.copy(plants = plants, plantCount = plants.size, showAddPlantDialog = false)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toUserMessage(), showAddPlantDialog = false) }
            }
        }
    }
}
