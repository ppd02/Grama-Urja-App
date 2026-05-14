package com.gramaurja2.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gramaurja2.app.domain.model.PowerStatus
import com.gramaurja2.app.presentation.viewmodel.HomeViewModel
import com.gramaurja2.app.ui.components.FuturisticBackground
import com.gramaurja2.app.ui.components.GramaCard
import com.gramaurja2.app.ui.components.HeroHeader
import com.gramaurja2.app.ui.components.LargePowerButton
import com.gramaurja2.app.ui.components.PrimaryAction
import com.gramaurja2.app.ui.components.SecondaryAction
import com.gramaurja2.app.ui.components.StatusIcon
import com.gramaurja2.app.ui.components.StatusPill
import com.gramaurja2.app.ui.components.ZoneChips
import com.gramaurja2.app.ui.components.t
import com.gramaurja2.app.ui.theme.Alert
import com.gramaurja2.app.ui.theme.Forest
import com.gramaurja2.app.ui.theme.Solar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
    onOpenReport: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val statusColor = when (state.statusUpdate.status) {
        PowerStatus.ON -> Forest
        PowerStatus.OFF -> Alert
        PowerStatus.UNKNOWN -> Solar
    }
    FuturisticBackground {
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                item {
                    HeroHeader(
                        title = t("Grama-Urja", "ಗ್ರಾಮ-ಊರ್ಜಾ", state.language),
                        subtitle = t("Karnataka rural power monitoring", "ಕರ್ನಾಟಕ ಗ್ರಾಮೀಣ ವಿದ್ಯುತ್ ನಿಗಾವಳಿ", state.language),
                        live = t("Live", "ಲೈವ್", state.language)
                    )
                }
                item {
                    Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(t("Followed zones", "ಅನುಸರಿಸುವ ವಲಯಗಳು", state.language), fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SecondaryAction("Refresh") { viewModel.refresh() }
                                SecondaryAction("Settings") { onOpenSettings() }
                            }
                        }
                        ZoneChips(state.followedZones, state.zone, state.language, viewModel::selectZone)
                        GramaCard {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(t("Current status", "ಪ್ರಸ್ತುತ ಸ್ಥಿತಿ", state.language), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f))
                                    Text(state.statusUpdate.status.name, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = statusColor)
                                    Text(state.zone.displayName(state.language), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                }
                                StatusIcon(state.statusUpdate.status, Modifier.padding(6.dp))
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                StatusPill(state.zone.district, MaterialTheme.colorScheme.secondary)
                                StatusPill(state.zone.transformer, Solar)
                                StatusPill(state.zone.feeder, Forest)
                            }
                            Text(t("Reporter", "ವರದಿ ಮಾಡಿದವರು", state.language) + ": ${state.statusUpdate.updatedBy}")
                            Text(t("Updated", "ನವೀಕರಿಸಿದ ಸಮಯ", state.language) + ": ${state.statusUpdate.timeText}")
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            LargePowerButton(t("POWER ON", "ವಿದ್ಯುತ್ ಇದೆ", state.language), PowerStatus.ON, Modifier.weight(1f)) { viewModel.report(PowerStatus.ON) }
                            LargePowerButton(t("POWER OFF", "ವಿದ್ಯುತ್ ಇಲ್ಲ", state.language), PowerStatus.OFF, Modifier.weight(1f)) { viewModel.report(PowerStatus.OFF) }
                        }
                        PrimaryAction(t("Open detailed report", "ವಿವರವಾದ ವರದಿ ತೆರೆಯಿರಿ", state.language), Modifier.fillMaxWidth(), onOpenReport)
                        
                        state.isRainy?.let { rainy ->
                            GramaCard {
                                Text(t("Weather Update", "ಹವಾಮಾನ ನವೀಕರಣ", state.language), fontWeight = FontWeight.Black, color = Solar)
                                Text(
                                    if (rainy) t("Rain forecast detected. Please take necessary precautions.", "ಮಳೆಯ ಮುನ್ಸೂಚನೆ ಇದೆ, ದಯವಿಟ್ಟು ಜಾಗರೂಕರಾಗಿರಿ.", state.language)
                                    else t("Weather appears to be clear.", "ಹವಾಮಾನವು ಸ್ಪಷ್ಟವಾಗಿ ಕಂಡುಬರುತ್ತಿದೆ.", state.language)
                                )
                            }
                        }

                        // Electricity Bill Calculator Section
                        GramaCard {
                            Text(t("Electricity Bill Calculator", "ವಿದ್ಯುತ್ ಬಿಲ್ ಕ್ಯಾಲ್ಕುಲೇಟರ್", state.language), fontWeight = FontWeight.Black)
                            Text(t("Estimate your monthly bill based on usage.", "ಬಳಕೆಯ ಆಧಾರದ ಮೇಲೆ ನಿಮ್ಮ ಮಾಸಿಕ ಬಿಲ್ ಅನ್ನು ಅಂದಾಜು ಮಾಡಿ.", state.language), style = MaterialTheme.typography.bodySmall)
                            
                            val escoms = listOf("BESCOM", "HESCOM", "GESCOM")
                            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                                escoms.forEachIndexed { index, escom ->
                                    SegmentedButton(
                                        selected = state.billCalculator.selectedEscom == escom,
                                        onClick = { viewModel.updateBillCalculation(escom = escom) },
                                        shape = SegmentedButtonDefaults.itemShape(index = index, count = escoms.size)
                                    ) { Text(escom, style = MaterialTheme.typography.labelSmall) }
                                }
                            }

                            OutlinedTextField(
                                value = state.billCalculator.units,
                                onValueChange = { viewModel.updateBillCalculation(units = it) },
                                label = { Text(t("Enter Meter Units", "ಮೀಟರ್ ಯೂನಿಟ್‌ಗಳನ್ನು ನಮೂದಿಸಿ", state.language)) },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )

                            Text(t("OR estimate from pump usage:", "ಅಥವಾ ಪಂಪ್ ಬಳಕೆಯಿಂದ ಅಂದಾಜು ಮಾಡಿ:", state.language), style = MaterialTheme.typography.labelSmall)
                            
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = state.billCalculator.hours,
                                    onValueChange = { viewModel.updateBillCalculation(hours = it) },
                                    label = { Text(t("Usage Hours", "ಬಳಕೆಯ ಗಂಟೆಗಳು", state.language)) },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = state.billCalculator.hp,
                                    onValueChange = { viewModel.updateBillCalculation(hp = it) },
                                    label = { Text(t("Pump HP", "ಪಂಪ್ HP", state.language)) },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                            }

                            if (state.billCalculator.estimatedBill > 0) {
                                Row(
                                    Modifier.fillMaxWidth().padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(t("Estimated Monthly Bill:", "ಅಂದಾಜು ಮಾಸಿಕ ಬಿಲ್:", state.language), fontWeight = FontWeight.Bold)
                                    Text("₹${String.format(Locale.getDefault(), "%.2f", state.billCalculator.estimatedBill)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                                }
                                Text(t("*Rates: ₹4.25-4.75/unit + ₹50 fixed charge", "*ದರಗಳು: ₹4.25-4.75/ಯೂನಿಟ್ + ₹50 ಸ್ಥಿರ ಶುಲ್ಕ", state.language), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                        }

                        GramaCard {
                            Text(t("Smart feeder summary", "ಸ್ಮಾರ್ಟ್ ಫೀಡರ್ ಸಾರಾಂಶ", state.language), fontWeight = FontWeight.Black)
                            Text(t("Your report appears immediately on this phone and syncs to the community when Firebase is available.", "ನಿಮ್ಮ ವರದಿ ಈ ಫೋನಿನಲ್ಲಿ ತಕ್ಷಣ ಕಾಣುತ್ತದೆ ಮತ್ತು Firebase ಲಭ್ಯವಿದ್ದಾಗ ಸಮುದಾಯಕ್ಕೆ sync ಆಗುತ್ತದೆ.", state.language))
                        }
                        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) }
                    }
                }
        }
    }
}
