package com.gramaurja2.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.gramaurja2.app.presentation.viewmodel.NotificationsViewModel
import com.gramaurja2.app.ui.components.FuturisticBackground
import com.gramaurja2.app.ui.components.GramaCard
import com.gramaurja2.app.ui.components.GramaLogo
import com.gramaurja2.app.ui.components.HeroHeader
import com.gramaurja2.app.ui.components.t

@Composable
fun NotificationsScreen(viewModel: NotificationsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    FuturisticBackground {
        Column(Modifier.fillMaxSize()) {
            HeroHeader(t("Power Alerts", "ವಿದ್ಯುತ್ ಎಚ್ಚರಿಕೆ", state.language), t("Recent reports from followed zones", "ಅನುಸರಿಸುವ ವಲಯಗಳ ಇತ್ತೀಚಿನ ವರದಿಗಳು", state.language), "Live")
            if (state.notifications.isEmpty()) {
                Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    GramaLogo(Modifier.size(84.dp))
                    Text(t("No alerts yet", "ಇನ್ನೂ ಎಚ್ಚರಿಕೆಗಳಿಲ್ಲ", state.language), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    Text(t("When someone reports ON or OFF, alerts appear here.", "ಯಾರಾದರೂ ವಿದ್ಯುತ್ ಇದೆ ಅಥವಾ ಇಲ್ಲ ಎಂದು ವರದಿ ಮಾಡಿದಾಗ ಇಲ್ಲಿ ಕಾಣುತ್ತದೆ.", state.language))
                }
            } else {
                LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.notifications, key = { it.id }) { item ->
                        GramaCard {
                            Text(item.message, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                            Text("${item.zoneName} • ${item.reporterName}")
                            Text(item.timeText, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f))
                        }
                    }
                }
            }
        }
    }
}
