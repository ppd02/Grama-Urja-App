package com.gramaurja2.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gramaurja2.app.presentation.viewmodel.PumpTimerViewModel
import com.gramaurja2.app.ui.components.FuturisticBackground
import com.gramaurja2.app.ui.components.GramaCard
import com.gramaurja2.app.ui.components.HeroHeader
import com.gramaurja2.app.ui.components.PrimaryAction
import com.gramaurja2.app.ui.components.SecondaryAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PumpTimerScreen(viewModel: PumpTimerViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var cropExpanded by remember { mutableStateOf(false) }

    if (state.showCompletionAlert) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissAlert() },
            title = { Text("Timer Finished") },
            text = { Text("The irrigation for ${state.selectedCrop.nameEn} is complete. Please turn off the pump.") },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissAlert() }) {
                    Text("OK")
                }
            }
        )
    }

    FuturisticBackground {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            HeroHeader("Pump Timer", "Irrigation duration calculator for Karnataka crops", "Calc")
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                GramaCard {
                    Text("Crop preset", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    ExposedDropdownMenuBox(expanded = cropExpanded, onExpandedChange = { cropExpanded = !cropExpanded }) {
                        OutlinedTextField(
                            value = state.selectedCrop.nameEn,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Crop type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(cropExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = cropExpanded, onDismissRequest = { cropExpanded = false }) {
                            state.crops.forEach { crop ->
                                DropdownMenuItem(text = { Text("${crop.nameEn} / ${crop.nameKn}") }, onClick = {
                                    viewModel.selectCrop(crop)
                                    cropExpanded = false
                                })
                            }
                        }
                    }
                    OutlinedTextField(
                        value = state.fieldSize,
                        onValueChange = viewModel::updateFieldSize,
                        label = { Text("Field size in acres") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Text("Water intensity: ${"%.1f".format(state.intensity)}x", fontWeight = FontWeight.Bold)
                    Slider(value = state.intensity, onValueChange = viewModel::updateIntensity, valueRange = 0.5f..1.8f, steps = 12)
                }
                GramaCard {
                    Text("Estimated duration", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    Text(state.estimateText, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    Text("Countdown", fontWeight = FontWeight.Bold)
                    Text(state.countdownText, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.secondary)
                    Text(state.selectedCrop.waterNoteEn)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        PrimaryAction(if (state.running) "Pause" else "Start", Modifier.weight(1f)) { viewModel.startPause() }
                        SecondaryAction("Reset", Modifier.weight(1f)) { viewModel.reset() }
                    }
                    state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}
