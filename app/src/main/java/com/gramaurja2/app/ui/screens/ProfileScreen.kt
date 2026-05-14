package com.gramaurja2.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gramaurja2.app.presentation.viewmodel.ProfileViewModel
import com.gramaurja2.app.ui.components.FuturisticBackground
import com.gramaurja2.app.ui.components.GramaCard
import com.gramaurja2.app.ui.components.HeroHeader
import com.gramaurja2.app.ui.components.SecondaryAction
import com.gramaurja2.app.ui.components.StatusPill
import com.gramaurja2.app.ui.components.t

@Composable
fun ProfileScreen(onOpenSettings: () -> Unit, viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    FuturisticBackground {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            HeroHeader(t("Profile", "ಪ್ರೊಫೈಲ್", state.language), t("Farmer identity and followed zones", "ರೈತ ಗುರುತು ಮತ್ತು ವಲಯಗಳು", state.language), "User")
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                GramaCard {
                    Text(t("Your details", "ನಿಮ್ಮ ವಿವರಗಳು", state.language), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    OutlinedTextField(value = state.name, onValueChange = viewModel::updateName, label = { Text(t("Name", "ಹೆಸರು", state.language)) }, modifier = Modifier.fillMaxWidth())
                    Text(t("This name is used on power reports. Change it anytime.", "ಈ ಹೆಸರನ್ನು ವಿದ್ಯುತ್ ವರದಿಗಳಲ್ಲಿ ಬಳಸಲಾಗುತ್ತದೆ. ಯಾವಾಗ ಬೇಕಾದರೂ ಬದಲಾಯಿಸಬಹುದು.", state.language))
                    Text(t("No account details are required.", "ಖಾತೆಯ ವಿವರಗಳು ಅಗತ್ಯವಿಲ್ಲ.", state.language))
                    state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) }
                }
                GramaCard {
                    Text(t("Followed zones", "ಅನುಸರಿಸುವ ವಲಯಗಳು", state.language), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    state.followedZones.forEach { zone -> StatusPill(zone.displayName(state.language), MaterialTheme.colorScheme.secondary) }
                }
                SecondaryAction(t("Open settings", "ಸೆಟ್ಟಿಂಗ್ ತೆರೆಯಿರಿ", state.language), Modifier.fillMaxWidth(), onOpenSettings)
            }
        }
    }
}
