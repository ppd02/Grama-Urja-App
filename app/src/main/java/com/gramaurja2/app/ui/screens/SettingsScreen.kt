package com.gramaurja2.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gramaurja2.app.domain.model.Language
import com.gramaurja2.app.presentation.viewmodel.SettingsViewModel
import com.gramaurja2.app.ui.components.FuturisticBackground
import com.gramaurja2.app.ui.components.GramaCard
import com.gramaurja2.app.ui.components.HeroHeader
import com.gramaurja2.app.ui.components.SecondaryAction
import com.gramaurja2.app.ui.components.t

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onChangeZones: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    FuturisticBackground {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            HeroHeader(t("Settings", "ಸೆಟ್ಟಿಂಗ್", state.language), t("Language, alerts, zones, and account", "ಭಾಷೆ, ಎಚ್ಚರಿಕೆ, ವಲಯ, ಖಾತೆ", state.language), "Control")
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SecondaryAction("Back", onClick = onBack)
                    Text(t("App settings", "ಅ್ಯಪ್ ಸೆಟ್ಟಿಂಗ್", state.language), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }
                GramaCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(t("Dark mode", "ಡಾರ್ಕ್ ಮೋಡ್", state.language), fontWeight = FontWeight.Bold)
                            Text(t("Futuristic night dashboard", "ರಾತ್ರಿ dashboard", state.language), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f))
                        }
                        Switch(checked = state.darkMode, onCheckedChange = viewModel::setDarkMode)
                    }
                    Text(t("Language", "ಭಾಷೆ", state.language), fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(selected = state.language == Language.English, onClick = { viewModel.setLanguage(Language.English) }, label = { Text("English") })
                        FilterChip(selected = state.language == Language.Kannada, onClick = { viewModel.setLanguage(Language.Kannada) }, label = { Text("ಕನ್ನಡ + English") })
                    }
                }
                GramaCard {
                    Text(t("Mute notifications per zone", "ಪ್ರತಿ ವಲಯದ ಸೂಚನೆ ಮ್ಯೂಟ್", state.language), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    state.followedZones.forEach { zone ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(zone.displayName(state.language), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Switch(checked = zone.id !in state.mutedZoneIds, onCheckedChange = { viewModel.toggleMute(zone) })
                        }
                    }
                }
                SecondaryAction(t("Change followed zones", "ವಲಯ ಬದಲಿಸಿ", state.language), Modifier.fillMaxWidth(), onChangeZones)
            }
        }
    }
}
