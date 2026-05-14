package com.gramaurja2.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gramaurja2.app.presentation.viewmodel.AiTipsViewModel
import com.gramaurja2.app.ui.components.FuturisticBackground
import com.gramaurja2.app.ui.components.GramaCard
import com.gramaurja2.app.ui.components.HeroHeader
import com.gramaurja2.app.ui.components.PrimaryAction
import com.gramaurja2.app.ui.components.StatusPill
import com.gramaurja2.app.ui.components.t
import com.gramaurja2.app.ui.theme.Alert
import com.gramaurja2.app.ui.theme.Forest
import com.gramaurja2.app.ui.theme.Solar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiSuggestionsScreen(viewModel: AiTipsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    FuturisticBackground {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            HeroHeader(t("Smart Farming", "ಸ್ಮಾರ್ಟ್ ಕೃಷಿ", state.language), t("Weather-aware irrigation & Market prices", "ಹವಾಮಾನ ಆಧಾರಿತ ನೀರಾವರಿ ಮತ್ತು ಮಾರುಕಟ್ಟೆ ದರಗಳು", state.language), "Smart")
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                
                // Weather Awareness Section
                if (state.rainPredicted) {
                    GramaCard(Modifier.background(Alert.copy(alpha = 0.1f))) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("🌧️", style = MaterialTheme.typography.headlineMedium)
                            Column {
                                Text(t("Rain Alert", "ಮಳೆಯ ಎಚ್ಚರಿಕೆ", state.language), fontWeight = FontWeight.Black, color = Alert)
                                Text(t("Heavy rain predicted. Consider skipping irrigation.", "ಭಾರಿ ಮಳೆಯ ಮುನ್ಸೂಚನೆ. ನೀರಾವರಿ ಬೇಡವೆಂದು ಸಲಹೆ ನೀಡಲಾಗಿದೆ.", state.language))
                            }
                        }
                    }
                }

                GramaCard {
                    Text(t("Current context", "ಪ್ರಸ್ತುತ ಮಾಹಿತಿ", state.language), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    Text(state.zone.displayName(state.language), fontWeight = FontWeight.Bold)
                    StatusPill(state.update.status.name, Solar)
                    Text(t("Updated", "ನವೀಕರಿಸಿದ ಸಮಯ", state.language) + ": ${state.update.timeText}")
                }

                GramaCard {
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                        OutlinedTextField(
                            value = state.selectedCrop.displayName(state.language),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(t("Crop", "ಬೆಳೆ", state.language)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            state.crops.forEach { crop ->
                                DropdownMenuItem(text = { Text("${crop.nameKn} / ${crop.nameEn}") }, onClick = {
                                    viewModel.selectCrop(crop)
                                    expanded = false
                                })
                            }
                        }
                    }
                    PrimaryAction(t("Generate recommendation", "ಸಲಹೆ ರಚಿಸಿ", state.language), Modifier.fillMaxWidth()) { viewModel.generateAdvice() }
                    if (state.loading) CircularProgressIndicator(color = Forest)
                    state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) }
                }

                if (state.recommendation.isNotBlank()) {
                    GramaCard {
                        Text(t("Irrigation recommendation", "ನೀರಾವರಿ ಶಿಫಾರಸು", state.language), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Text(state.recommendation, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                // Market Connectivity Section
                Text(t("Local Mandi Prices (Karnataka)", "ಸ್ಥಳೀಯ ಮಾರುಕಟ್ಟೆ ದರಗಳು", state.language), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                if (state.mandiPrices.isEmpty()) {
                    Text(t("Loading market prices...", "ಮಾರುಕಟ್ಟೆ ದರಗಳನ್ನು ಲೋಡ್ ಮಾಡಲಾಗುತ್ತಿದೆ...", state.language))
                }
                state.mandiPrices.take(10).forEach { record ->
                    GramaCard {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(record.commodity, fontWeight = FontWeight.Black)
                                Text("${record.market}, ${record.district}", style = MaterialTheme.typography.bodySmall)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("₹${record.modal_price}", color = Forest, fontWeight = FontWeight.Bold)
                                Text("/ Quintal", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
