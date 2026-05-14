package com.gramaurja2.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gramaurja2.app.domain.model.Language
import com.gramaurja2.app.domain.model.ZoneCatalog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.gramaUrjaDataStore by preferencesDataStore("grama_urja_user_settings")

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val onboarded = booleanPreferencesKey("onboarded")
        val selectedZone = stringPreferencesKey("selected_zone")
        val followedZones = stringSetPreferencesKey("followed_zones")
        val mutedZones = stringSetPreferencesKey("muted_zones")
        val darkMode = booleanPreferencesKey("dark_mode")
        val language = stringPreferencesKey("language")
        val displayName = stringPreferencesKey("display_name")
    }

    val onboarded: Flow<Boolean> = context.gramaUrjaDataStore.data.map { it[Keys.onboarded] ?: false }
    val selectedZoneId: Flow<String> = context.gramaUrjaDataStore.data.map { it[Keys.selectedZone] ?: ZoneCatalog.defaultZone.id }
    val followedZoneIds: Flow<Set<String>> = context.gramaUrjaDataStore.data.map { it[Keys.followedZones] ?: setOf(ZoneCatalog.defaultZone.id) }
    val mutedZoneIds: Flow<Set<String>> = context.gramaUrjaDataStore.data.map { it[Keys.mutedZones] ?: emptySet() }
    val darkMode: Flow<Boolean> = context.gramaUrjaDataStore.data.map { it[Keys.darkMode] ?: false }
    val displayName: Flow<String> = context.gramaUrjaDataStore.data.map { it[Keys.displayName] ?: "Local Farmer" }
    val language: Flow<Language> = context.gramaUrjaDataStore.data.map { prefs ->
        runCatching { Language.valueOf(prefs[Keys.language] ?: Language.English.name) }.getOrDefault(Language.English)
    }

    suspend fun completeOnboarding(selectedZoneId: String, followedZoneIds: Set<String>) {
        context.gramaUrjaDataStore.edit { prefs ->
            val selected = selectedZoneId.ifBlank { ZoneCatalog.defaultZone.id }
            val followed = followedZoneIds.ifEmpty { setOf(selected) }
            prefs[Keys.selectedZone] = selected
            prefs[Keys.followedZones] = followed
            prefs[Keys.onboarded] = true
        }
    }

    suspend fun setSelectedZone(zoneId: String) {
        context.gramaUrjaDataStore.edit { it[Keys.selectedZone] = zoneId }
    }

    suspend fun setFollowedZones(zoneIds: Set<String>) {
        context.gramaUrjaDataStore.edit { it[Keys.followedZones] = zoneIds.ifEmpty { setOf(ZoneCatalog.defaultZone.id) } }
    }

    suspend fun setMutedZones(zoneIds: Set<String>) {
        context.gramaUrjaDataStore.edit { it[Keys.mutedZones] = zoneIds }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.gramaUrjaDataStore.edit { it[Keys.darkMode] = enabled }
    }

    suspend fun setLanguage(language: Language) {
        context.gramaUrjaDataStore.edit { it[Keys.language] = language.name }
    }

    suspend fun setDisplayName(name: String) {
        context.gramaUrjaDataStore.edit { it[Keys.displayName] = name.ifBlank { "Local Farmer" } }
    }
}
