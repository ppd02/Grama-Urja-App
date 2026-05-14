package com.gramaurja2.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja2.app.data.local.PreferencesRepository
import com.gramaurja2.app.data.remote.firebase.PowerRepository
import com.gramaurja2.app.data.remote.api.WeatherRepository
import com.gramaurja2.app.domain.model.Language
import com.gramaurja2.app.domain.model.PowerStatus
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

data class BillCalculatorState(
    val units: String = "",
    val hours: String = "",
    val hp: String = "10",
    val estimatedBill: Double = 0.0,
    val selectedEscom: String = "BESCOM"
)

data class HomeUiState(
    val zone: Zone = ZoneCatalog.defaultZone,
    val followedZones: List<Zone> = listOf(ZoneCatalog.defaultZone),
    val statusUpdate: PowerStatusUpdate = PowerStatusUpdate(),
    val language: Language = Language.English,
    val reporterName: String = "Local Farmer",
    val isRainy: Boolean? = null,
    val billCalculator: BillCalculatorState = BillCalculatorState(),
    val loading: Boolean = true,
    val refreshing: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val powerRepository: PowerRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState
    private var statusJob: Job? = null

    init {
        viewModelScope.launch {
            preferencesRepository.language.collect { language -> _uiState.update { it.copy(language = language) } }
        }
        viewModelScope.launch {
            preferencesRepository.displayName.collect { name -> _uiState.update { it.copy(reporterName = name.ifBlank { "Local Farmer" }) } }
        }
        viewModelScope.launch {
            preferencesRepository.followedZoneIds.collect { ids -> _uiState.update { it.copy(followedZones = ZoneCatalog.byIds(ids)) } }
        }
        viewModelScope.launch {
            preferencesRepository.selectedZoneId.collect { zoneId ->
                val zone = ZoneCatalog.byId(zoneId)
                _uiState.update { it.copy(zone = zone, loading = true, error = null) }
                
                updateWeather(zone)

                statusJob?.cancel()
                statusJob = viewModelScope.launch {
                    powerRepository.observeZoneStatus(zone)
                        .collect { update -> _uiState.update { it.copy(statusUpdate = update, loading = false, refreshing = false) } }
                }
            }
        }
    }

    private fun updateWeather(zone: Zone) {
        viewModelScope.launch {
            val rainy = weatherRepository.checkRainProbability(zone.district)
            _uiState.update { it.copy(isRainy = rainy) }
        }
    }

    fun updateBillCalculation(units: String? = null, hours: String? = null, hp: String? = null, escom: String? = null) {
        _uiState.update { state ->
            var calc = state.billCalculator.copy(
                units = units ?: state.billCalculator.units,
                hours = hours ?: state.billCalculator.hours,
                hp = hp ?: state.billCalculator.hp,
                selectedEscom = escom ?: state.billCalculator.selectedEscom
            )
            
            // Logic to clear units if hours/hp are entered, or vice versa, to avoid confusion
            if (units != null) {
                if (units.isNotEmpty()) calc = calc.copy(hours = "", hp = "")
            } else if (hours != null || hp != null) {
                if ((hours?.isNotEmpty() == true) || (hp?.isNotEmpty() == true)) calc = calc.copy(units = "")
            }

            val finalUnits = if (calc.units.isNotBlank()) {
                calc.units.toDoubleOrNull() ?: 0.0
            } else {
                val h = calc.hours.toDoubleOrNull() ?: 0.0
                val p = calc.hp.toDoubleOrNull() ?: 0.0
                h * p * 0.746 * 30 // Monthly estimate: Hours per day * HP * 0.746 kWh/HP * 30 days
            }

            // Differentiated rates per ESCOM
            val rate = when (calc.selectedEscom) {
                "BESCOM" -> 4.75
                "HESCOM" -> 4.50
                "GESCOM" -> 4.25
                else -> 4.50
            }
            
            val fixedCharge = 50.0
            val estimate = if (finalUnits > 0) (finalUnits * rate) + fixedCharge else 0.0
            state.copy(billCalculator = calc.copy(estimatedBill = estimate))
        }
    }

    fun selectZone(zone: Zone) {
        viewModelScope.launch { preferencesRepository.setSelectedZone(zone.id) }
    }

    fun report(status: PowerStatus) {
        viewModelScope.launch {
            _uiState.update { it.copy(refreshing = true, error = null) }
            powerRepository.reportStatus(_uiState.value.zone, status, _uiState.value.reporterName)
                .onFailure { error -> _uiState.update { it.copy(refreshing = false, error = error.message ?: "Unable to report status") } }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(refreshing = true, error = null) }
            updateWeather(_uiState.value.zone)
            powerRepository.refreshZone(_uiState.value.zone)
                .onSuccess { update -> _uiState.update { it.copy(statusUpdate = update, refreshing = false, loading = false) } }
                .onFailure { error -> _uiState.update { it.copy(refreshing = false, error = error.message ?: "Refresh failed") } }
        }
    }
}
