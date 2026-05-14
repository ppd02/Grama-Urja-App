package com.gramaurja2.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gramaurja2.app.domain.model.PowerStatus
import com.gramaurja2.app.presentation.viewmodel.HomeViewModel
import com.gramaurja2.app.ui.components.FuturisticBackground
import com.gramaurja2.app.ui.components.GramaCard
import com.gramaurja2.app.ui.components.HeroHeader
import com.gramaurja2.app.ui.components.LargePowerButton
import com.gramaurja2.app.ui.components.SecondaryAction
import com.gramaurja2.app.ui.components.StatusIcon
import com.gramaurja2.app.ui.components.t

@Composable
fun PowerReportScreen(onBack: () -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    FuturisticBackground {
        Column(Modifier.fillMaxSize()) {
            HeroHeader(t("Report Status", "ಸ್ಥಿತಿ ವರದಿ", state.language), state.zone.displayName(state.language), t("Safe", "ಸುರಕ್ಷಿತ", state.language))
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SecondaryAction("Back", onClick = onBack)
                    Text(t("Community power report", "ಸಮುದಾಯ ವಿದ್ಯುತ್ ವರದಿ", state.language), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }
                GramaCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(state.zone.displayName(state.language), fontWeight = FontWeight.Black)
                            Text("${state.zone.district} • ${state.zone.transformer} • ${state.zone.feeder}")
                        }
                        StatusIcon(state.statusUpdate.status)
                    }
                    Text(t("Tap only after confirming supply at your pump starter or nearby transformer.", "ನಿಮ್ಮ ಪಂಪ್ ಸ್ಟಾರ್ಟರ್ ಅಥವಾ ಹತ್ತಿರದ ಟ್ರಾನ್ಸ್‌ಫಾರ್ಮರ್‌ನಲ್ಲಿ ಪರಿಶೀಲಿಸಿದ ನಂತರ ಮಾತ್ರ ಒತ್ತಿರಿ.", state.language))
                }
                LargePowerButton(t("Report POWER ON", "ವಿದ್ಯುತ್ ಇದೆ ಎಂದು ವರದಿ", state.language), PowerStatus.ON, Modifier.fillMaxWidth()) { viewModel.report(PowerStatus.ON) }
                LargePowerButton(t("Report POWER OFF", "ವಿದ್ಯುತ್ ಇಲ್ಲ ಎಂದು ವರದಿ", state.language), PowerStatus.OFF, Modifier.fillMaxWidth()) { viewModel.report(PowerStatus.OFF) }
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) }
            }
        }
    }
}
