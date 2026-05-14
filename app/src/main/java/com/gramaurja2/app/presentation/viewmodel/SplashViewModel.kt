package com.gramaurja2.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja2.app.data.local.PreferencesRepository
import com.gramaurja2.app.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashUiState(val route: String? = null)

@HiltViewModel
class SplashViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState

    init {
        viewModelScope.launch {
            delay(900)
            preferencesRepository.onboarded.collect { onboarded ->
                _uiState.value = SplashUiState(if (onboarded) Route.Home.path else Route.Onboarding.path)
            }
        }
    }
}
