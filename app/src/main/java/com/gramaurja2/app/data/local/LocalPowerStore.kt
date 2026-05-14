package com.gramaurja2.app.data.local

import com.gramaurja2.app.domain.model.NotificationItem
import com.gramaurja2.app.domain.model.PowerStatus
import com.gramaurja2.app.domain.model.PowerStatusUpdate
import com.gramaurja2.app.domain.model.Zone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalPowerStore @Inject constructor() {
    private val statusUpdates = MutableStateFlow<Map<String, PowerStatusUpdate>>(emptyMap())
    private val notifications = MutableStateFlow<List<NotificationItem>>(emptyList())

    fun observeStatus(zone: Zone): Flow<PowerStatusUpdate> = statusUpdates.map { updates ->
        updates[zone.id] ?: PowerStatusUpdate(zoneId = zone.id, zoneName = zone.nameEn)
    }

    fun currentStatus(zone: Zone): PowerStatusUpdate =
        statusUpdates.value[zone.id] ?: PowerStatusUpdate(zoneId = zone.id, zoneName = zone.nameEn)

    fun reportStatus(zone: Zone, status: PowerStatus, reporterName: String): PowerStatusUpdate {
        val now = System.currentTimeMillis()
        val update = PowerStatusUpdate(
            status = status,
            zoneId = zone.id,
            zoneName = zone.nameEn,
            updatedBy = reporterName,
            timestamp = now
        )
        statusUpdates.update { it + (zone.id to update) }
        addNotification(
            NotificationItem(
                id = "${zone.id}-$now",
                zoneId = zone.id,
                zoneName = zone.nameEn,
                reporterName = reporterName,
                message = when (status) {
                    PowerStatus.ON -> "Power reported ON in ${zone.nameEn}"
                    PowerStatus.OFF -> "Power reported OFF in ${zone.nameEn}"
                    PowerStatus.UNKNOWN -> "Power status marked unknown in ${zone.nameEn}"
                },
                timestamp = now
            )
        )
        return update
    }

    fun applyRemoteStatus(update: PowerStatusUpdate) {
        if (update.status != PowerStatus.UNKNOWN) {
            statusUpdates.update { it + (update.zoneId to update) }
        }
    }

    fun observeNotifications(zoneIds: Set<String>): Flow<List<NotificationItem>> = notifications.map { items ->
        items
            .filter { zoneIds.isEmpty() || it.zoneId in zoneIds }
            .sortedByDescending { it.timestamp }
    }

    fun mergeRemoteNotifications(items: List<NotificationItem>) {
        if (items.isEmpty()) return
        notifications.update { existing ->
            (items + existing)
                .distinctBy { it.id.ifBlank { "${it.zoneId}-${it.timestamp}-${it.message}" } }
                .sortedByDescending { it.timestamp }
                .take(100)
        }
    }

    private fun addNotification(item: NotificationItem) {
        notifications.update { existing ->
            (listOf(item) + existing)
                .distinctBy { it.id }
                .sortedByDescending { it.timestamp }
                .take(100)
        }
    }
}
