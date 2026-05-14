package com.gramaurja2.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja2.app.data.local.PreferencesRepository
import com.gramaurja2.app.data.remote.firebase.PowerRepository
import com.gramaurja2.app.domain.model.Language
import com.gramaurja2.app.domain.model.NotificationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<NotificationItem> = emptyList(),
    val language: Language = Language.English,
    val loading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val powerRepository: PowerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState
    private var notificationJob: Job? = null

    init {
        viewModelScope.launch { preferencesRepository.language.collect { language -> _uiState.update { it.copy(language = language) } } }
        viewModelScope.launch {
            preferencesRepository.followedZoneIds.collect { ids ->
                notificationJob?.cancel()
                notificationJob = viewModelScope.launch {
                    powerRepository.observeNotifications(ids).collect { items ->
                        _uiState.update { it.copy(notifications = items, loading = false, error = null) }
                    }
                }
            }
        }
    }
}
