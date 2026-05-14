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

data class OnboardingUiState(
    val zones: List<Zone> = ZoneCatalog.zones,
    val selectedZoneId: String = ZoneCatalog.defaultZone.id,
    val followedZoneIds: Set<String> = setOf(ZoneCatalog.defaultZone.id),
    val language: Language = Language.English,
    val saving: Boolean = false,
    val complete: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val messagingRepository: MessagingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    fun selectPrimaryZone(zoneId: String) {
        _uiState.update { state ->
            state.copy(selectedZoneId = zoneId, followedZoneIds = state.followedZoneIds + zoneId)
        }
    }

    fun toggleFollow(zoneId: String) {
        _uiState.update { state ->
            val next = if (zoneId in state.followedZoneIds) state.followedZoneIds - zoneId else state.followedZoneIds + zoneId
            state.copy(followedZoneIds = next.ifEmpty { setOf(state.selectedZoneId) })
        }
    }

    fun setLanguage(language: Language) = _uiState.update { it.copy(language = language) }

    fun finish() {
        if (_uiState.value.saving || _uiState.value.complete) return
        viewModelScope.launch {
            _uiState.update { it.copy(saving = true) }
            val state = _uiState.value
            
            // Save preferences locally first - this is the critical part for navigation
            preferencesRepository.setLanguage(state.language)
            preferencesRepository.completeOnboarding(state.selectedZoneId, state.followedZoneIds)
            
            // Sync with Firebase - wrap in runCatching so network/Firebase errors don't block navigation
            runCatching {
                messagingRepository.syncTokenAndTopics(ZoneCatalog.byIds(state.followedZoneIds), emptySet())
            }

            _uiState.update { it.copy(saving = false, complete = true) }
        }
    }
}
