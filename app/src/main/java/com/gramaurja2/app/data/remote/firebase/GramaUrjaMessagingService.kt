package com.gramaurja2.app.data.remote.firebase

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class GramaUrjaMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        FirebaseDatabase.getInstance().getReference("deviceTokens")
            .child(token.hashCode().toString())
            .setValue(mapOf("token" to token, "updatedAt" to System.currentTimeMillis()))
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val type = message.data["type"] ?: "power"
        val title = message.notification?.title ?: message.data["title"] ?: "Grama-Urja"
        val body = message.notification?.body ?: message.data["body"] ?: ""

        when (type) {
            "scheme" -> {
                NotificationHelper.showSchemeNotification(this, title, body)
            }
            else -> {
                val zone = message.data["zoneName"] ?: title
                val reporter = message.data["reporterName"] ?: "Community report"
                val displayBody = if (body.isBlank()) "Power status updated by $reporter" else body
                NotificationHelper.showPowerNotification(this, zone, displayBody)
            }
        }
    }
}
