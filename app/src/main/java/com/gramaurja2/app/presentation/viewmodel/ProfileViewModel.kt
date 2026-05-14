package com.gramaurja2.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja2.app.data.local.PreferencesRepository
import com.gramaurja2.app.domain.model.Language
import com.gramaurja2.app.domain.model.Zone
import com.gramaurja2.app.domain.model.ZoneCatalog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val uid: String = "guest-local-farmer",
    val name: String = "Local Farmer",
    val followedZones: List<Zone> = listOf(ZoneCatalog.defaultZone),
    val mutedZoneIds: Set<String> = emptySet(),
    val language: Language = Language.English,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState
    private var nameSaveJob: Job? = null

    init {
        viewModelScope.launch { preferencesRepository.language.collect { language -> _uiState.update { it.copy(language = language) } } }
        viewModelScope.launch { preferencesRepository.displayName.collect { name -> _uiState.update { it.copy(name = name) } } }
        viewModelScope.launch { preferencesRepository.followedZoneIds.collect { ids -> _uiState.update { it.copy(followedZones = ZoneCatalog.byIds(ids)) } } }
        viewModelScope.launch { preferencesRepository.mutedZoneIds.collect { ids -> _uiState.update { it.copy(mutedZoneIds = ids) } } }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name, error = null) }
        nameSaveJob?.cancel()
        nameSaveJob = viewModelScope.launch {
            delay(250)
            preferencesRepository.setDisplayName(name)
        }
    }
}
