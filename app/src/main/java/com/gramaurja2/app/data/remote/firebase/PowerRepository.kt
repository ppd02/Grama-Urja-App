package com.gramaurja2.app.data.remote.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gramaurja2.app.data.awaitResult
import com.gramaurja2.app.data.local.LocalPowerStore
import com.gramaurja2.app.domain.model.NotificationItem
import com.gramaurja2.app.domain.model.PowerStatus
import com.gramaurja2.app.domain.model.PowerStatusUpdate
import com.gramaurja2.app.domain.model.Zone
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PowerRepository @Inject constructor(
    private val database: FirebaseDatabase,
    private val localPowerStore: LocalPowerStore
) {
    fun observeZoneStatus(zone: Zone): Flow<PowerStatusUpdate> = callbackFlow {
        val localJob = launch {
            localPowerStore.observeStatus(zone).collect { update -> trySend(update) }
        }
        val ref = database.getReference("zones").child(zone.id).child("currentStatus")
        ref.keepSynced(true)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val update = snapshot.toPowerStatusUpdate(zone)
                    if (update.status == PowerStatus.UNKNOWN) {
                        trySend(localPowerStore.currentStatus(zone))
                    } else {
                        localPowerStore.applyRemoteStatus(update)
                        trySend(update)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(localPowerStore.currentStatus(zone))
            }
        }
        ref.addValueEventListener(listener)
        awaitClose {
            localJob.cancel()
            ref.removeEventListener(listener)
        }
    }

    suspend fun refreshZone(zone: Zone): Result<PowerStatusUpdate> {
        val update = runCatching {
            val snapshot = database.getReference("zones").child(zone.id).child("currentStatus").get().awaitResult()
            val remoteUpdate = if (snapshot.exists()) snapshot.toPowerStatusUpdate(zone) else null
            if (remoteUpdate != null && remoteUpdate.status != PowerStatus.UNKNOWN) {
                remoteUpdate
            } else {
                localPowerStore.currentStatus(zone)
            }
        }.getOrElse {
            localPowerStore.currentStatus(zone)
        }
        localPowerStore.applyRemoteStatus(update)
        return Result.success(update)
    }

    suspend fun reportStatus(zone: Zone, status: PowerStatus, reporterName: String): Result<Unit> {
        val localUpdate = localPowerStore.reportStatus(zone, status, reporterName)
        val statusRef = database.getReference("zones").child(zone.id).child("currentStatus")
        runCatching {
            val payload = mapOf(
                "status" to status.name,
                "updatedBy" to reporterName,
                "timestamp" to localUpdate.timestamp,
                "zoneId" to zone.id,
                "zoneName" to zone.nameEn,
                "zoneNameKannada" to zone.nameKn,
                "district" to zone.district,
                "transformer" to zone.transformer,
                "feeder" to zone.feeder
            )
            statusRef.setValue(payload).awaitResult()
            createNotification(zone, reporterName, localUpdate.timestamp, status)
        }
        return Result.success(Unit)
    }

    fun observeNotifications(zoneIds: Set<String>): Flow<List<NotificationItem>> = callbackFlow {
        var localItems = emptyList<NotificationItem>()
        var remoteItems = emptyList<NotificationItem>()
        fun emitMerged() {
            val merged = (localItems + remoteItems)
                .filter { zoneIds.isEmpty() || it.zoneId in zoneIds }
                .distinctBy { it.id.ifBlank { "${it.zoneId}-${it.timestamp}-${it.message}" } }
                .sortedByDescending { it.timestamp }
            trySend(merged)
        }
        val localJob = launch {
            localPowerStore.observeNotifications(zoneIds).collect { items ->
                localItems = items
                emitMerged()
            }
        }
        val ref = database.getReference("notifications")
        ref.keepSynced(true)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                remoteItems = snapshot.children.flatMap { zoneNode ->
                    if (zoneIds.isNotEmpty() && zoneNode.key !in zoneIds) emptyList() else zoneNode.children.map { it.toNotificationItem() }
                }.sortedByDescending { it.timestamp }
                localPowerStore.mergeRemoteNotifications(remoteItems)
                emitMerged()
            }

            override fun onCancelled(error: DatabaseError) {
                emitMerged()
            }
        }
        ref.addValueEventListener(listener)
        awaitClose {
            localJob.cancel()
            ref.removeEventListener(listener)
        }
    }

    private suspend fun createNotification(zone: Zone, reporterName: String, timestamp: Long, status: PowerStatus) {
        val ref = database.getReference("notifications").child(zone.id).push()
        val notification = mapOf(
            "id" to (ref.key ?: timestamp.toString()),
            "zoneId" to zone.id,
            "zoneName" to zone.nameEn,
            "reporterName" to reporterName,
            "message" to when (status) {
                PowerStatus.ON -> "Power reported ON in ${zone.nameEn}"
                PowerStatus.OFF -> "Power reported OFF in ${zone.nameEn}"
                PowerStatus.UNKNOWN -> "Power status marked unknown in ${zone.nameEn}"
            },
            "timestamp" to timestamp
        )
        ref.setValue(notification).awaitResult()
    }

    private fun DataSnapshot.toPowerStatusUpdate(zone: Zone): PowerStatusUpdate {
        val status = runCatching { PowerStatus.valueOf(child("status").getValue(String::class.java) ?: PowerStatus.UNKNOWN.name) }
            .getOrDefault(PowerStatus.UNKNOWN)
        return PowerStatusUpdate(
            status = status,
            zoneId = child("zoneId").getValue(String::class.java) ?: zone.id,
            zoneName = child("zoneName").getValue(String::class.java) ?: zone.nameEn,
            updatedBy = child("updatedBy").getValue(String::class.java) ?: "System",
            timestamp = child("timestamp").getValue(Long::class.java) ?: System.currentTimeMillis()
        )
    }

    private fun DataSnapshot.toNotificationItem(): NotificationItem = NotificationItem(
        id = child("id").getValue(String::class.java) ?: key.orEmpty(),
        zoneId = child("zoneId").getValue(String::class.java).orEmpty(),
        zoneName = child("zoneName").getValue(String::class.java).orEmpty(),
        reporterName = child("reporterName").getValue(String::class.java).orEmpty(),
        message = child("message").getValue(String::class.java).orEmpty(),
        timestamp = child("timestamp").getValue(Long::class.java) ?: System.currentTimeMillis()
    )
}
