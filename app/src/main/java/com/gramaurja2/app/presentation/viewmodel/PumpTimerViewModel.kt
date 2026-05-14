package com.gramaurja2.app.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramaurja2.app.data.remote.firebase.NotificationHelper
import com.gramaurja2.app.domain.model.CropCatalog
import com.gramaurja2.app.domain.model.CropType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

data class PumpTimerUiState(
    val crops: List<CropType> = CropCatalog.pumpPresets,
    val selectedCrop: CropType = CropCatalog.defaultCrop,
    val fieldSize: String = "1.0",
    val intensity: Float = 1.0f,
    val estimatedMinutes: Int = 210,
    val remainingSeconds: Int = 210 * 60,
    val running: Boolean = false,
    val showCompletionAlert: Boolean = false,
    val error: String? = null
) {
    val estimateText: String get() = formatMinutes(estimatedMinutes)
    val countdownText: String get() = formatSeconds(remainingSeconds)
}

@HiltViewModel
class PumpTimerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(PumpTimerUiState())
    val uiState: StateFlow<PumpTimerUiState> = _uiState
    private var timerJob: Job? = null

    init { recalculate() }

    fun selectCrop(crop: CropType) {
        _uiState.update { it.copy(selectedCrop = crop, showCompletionAlert = false) }
        recalculate()
    }

    fun updateFieldSize(value: String) {
        _uiState.update { it.copy(fieldSize = value.filter { char -> char.isDigit() || char == '.' }.take(5), error = null, showCompletionAlert = false) }
        recalculate()
    }

    fun updateIntensity(value: Float) {
        _uiState.update { it.copy(intensity = value, showCompletionAlert = false) }
        recalculate()
    }

    fun dismissAlert() {
        _uiState.update { it.copy(showCompletionAlert = false) }
    }

    fun startPause() {
        val current = _uiState.value
        if (current.remainingSeconds <= 0) reset()
        if (current.running) {
            timerJob?.cancel()
            _uiState.update { it.copy(running = false) }
            return
        }
        _uiState.update { it.copy(running = true, showCompletionAlert = false) }
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0 && _uiState.value.running) {
                delay(1000)
                _uiState.update { it.copy(remainingSeconds = (it.remainingSeconds - 1).coerceAtLeast(0)) }
            }
            if (_uiState.value.remainingSeconds == 0) {
                onTimerFinished()
            }
            _uiState.update { it.copy(running = false) }
        }
    }

    private fun onTimerFinished() {
        _uiState.update { it.copy(showCompletionAlert = true) }
        NotificationHelper.showTimerNotification(
            context,
            "Pump Timer Finished",
            "The irrigation for ${_uiState.value.selectedCrop.nameEn} is complete."
        )
    }

    fun reset() {
        timerJob?.cancel()
        _uiState.update { it.copy(remainingSeconds = it.estimatedMinutes * 60, running = false, showCompletionAlert = false) }
    }

    private fun recalculate() {
        val state = _uiState.value
        val acres = state.fieldSize.toDoubleOrNull()
        if (acres == null || acres <= 0.0) {
            _uiState.update { it.copy(estimatedMinutes = 0, remainingSeconds = 0, running = false, error = "Enter valid field size") }
            return
        }
        val minutes = (state.selectedCrop.baseMinutesPerAcre * acres * state.intensity).roundToInt().coerceAtLeast(1)
        _uiState.update { it.copy(estimatedMinutes = minutes, remainingSeconds = minutes * 60, running = false, error = null) }
        timerJob?.cancel()
    }
}

private fun formatMinutes(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return when {
        minutes <= 0 -> "0 min"
        h == 0 -> "$m min"
        m == 0 -> "$h hr"
        else -> "$h hr $m min"
    }
}

private fun formatSeconds(seconds: Int): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return "%02d:%02d:%02d".format(h, m, s)
}
