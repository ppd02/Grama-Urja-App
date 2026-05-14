package com.gramaurja2.app.data.remote.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gramaurja2.app.MainActivity
import com.gramaurja2.app.R

object NotificationHelper {
    private const val POWER_CHANNEL_ID = "power_status_updates"
    private const val TIMER_CHANNEL_ID = "pump_timer_alerts"
    private const val SCHEME_CHANNEL_ID = "gov_scheme_alerts"
    private const val WEATHER_CHANNEL_ID = "weather_alerts"

    fun showPowerNotification(context: Context, title: String, message: String) {
        show(context, POWER_CHANNEL_ID, "Power status updates", title, message, 1001)
    }

    fun showTimerNotification(context: Context, title: String, message: String) {
        show(context, TIMER_CHANNEL_ID, "Pump Timer Alerts", title, message, 2002)
    }

    fun showSchemeNotification(context: Context, title: String, message: String) {
        show(context, SCHEME_CHANNEL_ID, "Government Schemes", title, message, 3003)
    }

    fun showWeatherNotification(context: Context, title: String, message: String) {
        show(context, WEATHER_CHANNEL_ID, "Weather Updates", title, message, 4004)
    }

    private fun show(context: Context, channelId: String, channelName: String, title: String, message: String, reqCode: Int) {
        createChannel(context, channelId, channelName)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            reqCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        runCatching { 
            NotificationManagerCompat.from(context).notify(reqCode + System.currentTimeMillis().toInt(), notification) 
        }
    }

    private fun createChannel(context: Context, channelId: String, name: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            channelId,
            name,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alerts for $name"
        }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}
