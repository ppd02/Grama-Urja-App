package com.gramaurja2.app.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PowerStatusUpdate(
    val status: PowerStatus = PowerStatus.UNKNOWN,
    val zoneId: String = ZoneCatalog.defaultZone.id,
    val zoneName: String = ZoneCatalog.defaultZone.nameEn,
    val updatedBy: String = "System",
    val timestamp: Long = System.currentTimeMillis()
) {
    val timeText: String
        get() = SimpleDateFormat("dd MMM, h:mm a", Locale.getDefault()).format(Date(timestamp))
}
