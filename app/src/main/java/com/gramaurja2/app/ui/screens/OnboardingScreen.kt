package com.gramaurja2.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gramaurja2.app.domain.model.Language
import com.gramaurja2.app.presentation.viewmodel.OnboardingViewModel
import com.gramaurja2.app.ui.components.FuturisticBackground
import com.gramaurja2.app.ui.components.GramaCard
import com.gramaurja2.app.ui.components.HeroHeader
import com.gramaurja2.app.ui.components.PrimaryAction

@Composable
fun OnboardingScreen(onComplete: () -> Unit, viewModel: OnboardingViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.complete) { if (state.complete) onComplete() }
    FuturisticBackground {
        Column(Modifier.fillMaxSize()) {
            HeroHeader("Setup / ಸೆಟಪ್", "Choose Karnataka power zones to follow", "Live")
            LazyColumn(Modifier.weight(1f).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    GramaCard {
                        Text("Language / ಭಾಷೆ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            FilterChip(selected = state.language == Language.English, onClick = { viewModel.setLanguage(Language.English) }, label = { Text("English") })
                            FilterChip(selected = state.language == Language.Kannada, onClick = { viewModel.setLanguage(Language.Kannada) }, label = { Text("ಕನ್ನಡ + English") })
                        }
                    }
                }
                item { Text("Select primary village / transformer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                items(state.zones) { zone ->
                    GramaCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = state.selectedZoneId == zone.id, onClick = { viewModel.selectPrimaryZone(zone.id) })
                            Column(Modifier.weight(1f)) {
                                Text(zone.displayName(state.language), fontWeight = FontWeight.Black)
                                Text("${zone.district} • ${zone.transformer} • ${zone.feeder}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f))
                            }
                            Checkbox(checked = zone.id in state.followedZoneIds, onCheckedChange = { viewModel.toggleFollow(zone.id) })
                        }
                    }
                }
                item {
                    PrimaryAction(if (state.saving) "Saving..." else "Start Monitoring", Modifier.fillMaxWidth()) { viewModel.finish() }
                    if (state.saving) CircularProgressIndicator(Modifier.padding(16.dp))
                }
            }
        }
    }
}
