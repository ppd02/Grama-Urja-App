package com.gramaurja2.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja2.app.data.local.PreferencesRepository
import com.gramaurja2.app.data.remote.firebase.MessagingRepository
import com.gramaurja2.app.domain.model.Language
import com.gramaurja2.app.domain.model.Zone
import com.gramaurja2.app.domain.model.ZoneCatalog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val darkMode: Boolean = false,
    val language: Language = Language.English,
    val followedZones: List<Zone> = listOf(ZoneCatalog.defaultZone),
    val mutedZoneIds: Set<String> = emptySet(),
    val saving: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val messagingRepository: MessagingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        viewModelScope.launch { preferencesRepository.darkMode.collect { value -> _uiState.update { it.copy(darkMode = value) } } }
        viewModelScope.launch { preferencesRepository.language.collect { value -> _uiState.update { it.copy(language = value) } } }
        viewModelScope.launch { preferencesRepository.followedZoneIds.collect { ids -> _uiState.update { it.copy(followedZones = ZoneCatalog.byIds(ids)) } } }
        viewModelScope.launch { preferencesRepository.mutedZoneIds.collect { ids -> _uiState.update { it.copy(mutedZoneIds = ids) } } }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { preferencesRepository.setDarkMode(enabled) }
    }

    fun setLanguage(language: Language) {
        viewModelScope.launch { preferencesRepository.setLanguage(language) }
    }

    fun toggleMute(zone: Zone) {
        viewModelScope.launch {
            val state = _uiState.value
            val next = if (zone.id in state.mutedZoneIds) state.mutedZoneIds - zone.id else state.mutedZoneIds + zone.id
            _uiState.update { it.copy(saving = true) }
            preferencesRepository.setMutedZones(next)
            messagingRepository.syncTokenAndTopics(state.followedZones, next)
            _uiState.update { it.copy(saving = false) }
        }
    }

}
