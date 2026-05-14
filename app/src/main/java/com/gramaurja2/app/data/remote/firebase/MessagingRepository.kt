package com.gramaurja2.app.data.remote.firebase

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.gramaurja2.app.data.awaitResult
import com.gramaurja2.app.domain.model.Zone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagingRepository @Inject constructor(
    private val messaging: FirebaseMessaging,
    private val database: FirebaseDatabase
) {
    suspend fun syncTokenAndTopics(followedZones: List<Zone>, mutedZoneIds: Set<String>) {
        val token = runCatching { messaging.token.awaitResult() }.getOrNull() ?: return
        database.getReference("deviceTokens")
            .child(token.hashCode().toString())
            .setValue(mapOf("token" to token, "updatedAt" to System.currentTimeMillis()))
            .awaitResult()
        followedZones.forEach { zone ->
            if (zone.id in mutedZoneIds) messaging.unsubscribeFromTopic(zone.topic()).awaitResult()
            else messaging.subscribeToTopic(zone.topic()).awaitResult()
        }
    }
}
