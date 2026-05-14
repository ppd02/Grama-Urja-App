package com.gramaurja2.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja2.app.data.local.IrrigationTipsRepository
import com.gramaurja2.app.data.local.PreferencesRepository
import com.gramaurja2.app.data.remote.api.MandiRecord
import com.gramaurja2.app.data.remote.api.MandiRepository
import com.gramaurja2.app.data.remote.api.WeatherRepository
import com.gramaurja2.app.data.remote.firebase.PowerRepository
import com.gramaurja2.app.domain.model.CropCatalog
import com.gramaurja2.app.domain.model.CropType
import com.gramaurja2.app.domain.model.Language
import com.gramaurja2.app.domain.model.PowerStatusUpdate
import com.gramaurja2.app.domain.model.Zone
import com.gramaurja2.app.domain.model.ZoneCatalog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AiTipsUiState(
    val crops: List<CropType> = CropCatalog.crops,
    val selectedCrop: CropType = CropCatalog.defaultCrop,
    val zone: Zone = ZoneCatalog.defaultZone,
    val update: PowerStatusUpdate = PowerStatusUpdate(),
    val language: Language = Language.English,
    val recommendation: String = "",
    val mandiPrices: List<MandiRecord> = emptyList(),
    val rainPredicted: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AiTipsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val powerRepository: PowerRepository,
    private val irrigationTipsRepository: IrrigationTipsRepository,
    private val weatherRepository: WeatherRepository,
    private val mandiRepository: MandiRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AiTipsUiState())
    val uiState: StateFlow<AiTipsUiState> = _uiState
    private var statusJob: Job? = null

    init {
        viewModelScope.launch { 
            preferencesRepository.language.collect { language -> 
                _uiState.update { it.copy(language = language) } 
            } 
        }
        viewModelScope.launch {
            preferencesRepository.selectedZoneId.collect { zoneId ->
                val zone = ZoneCatalog.byId(zoneId)
                _uiState.update { it.copy(zone = zone) }
                statusJob?.cancel()
                statusJob = viewModelScope.launch {
                    powerRepository.observeZoneStatus(zone).collect { update -> 
                        _uiState.update { it.copy(update = update) } 
                    }
                }
                fetchWeatherData(zone.district)
            }
        }
        fetchMandiPrices()
    }

    private fun fetchWeatherData(district: String) {
        viewModelScope.launch {
            val isRaining = weatherRepository.checkRainProbability(district)
            _uiState.update { it.copy(rainPredicted = isRaining) }
        }
    }

    private fun fetchMandiPrices() {
        viewModelScope.launch {
            val prices = mandiRepository.getPricesForCrops(listOf("Paddy", "Sugarcane", "Ragi", "Maize"))
            _uiState.update { it.copy(mandiPrices = prices) }
        }
    }

    fun selectCrop(crop: CropType) = _uiState.update { it.copy(selectedCrop = crop) }

    fun generateAdvice() {
        viewModelScope.launch {
            val state = _uiState.value
            _uiState.update { it.copy(loading = true, error = null) }
            val text = irrigationTipsRepository.advice(
                state.zone, 
                state.selectedCrop, 
                state.update, 
                state.language,
                state.rainPredicted
            )
            _uiState.update { it.copy(loading = false, recommendation = text) }
        }
    }
}
