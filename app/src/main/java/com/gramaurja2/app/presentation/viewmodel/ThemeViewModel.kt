package com.gramaurja2.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja2.app.data.local.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository
) : ViewModel() {
    val darkMode = preferencesRepository.darkMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
}
