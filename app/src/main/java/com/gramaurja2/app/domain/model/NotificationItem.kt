package com.gramaurja2.app.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NotificationItem(
    val id: String = "",
    val zoneId: String = "",
    val zoneName: String = "",
    val reporterName: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    val timeText: String
        get() = SimpleDateFormat("dd MMM, h:mm a", Locale.getDefault()).format(Date(timestamp))
}
