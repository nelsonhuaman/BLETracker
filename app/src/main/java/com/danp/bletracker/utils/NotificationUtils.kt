package com.danp.bletracker.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.danp.bletracker.MainActivity
import com.danp.bletracker.R

object NotificationUtils {
    private const val CHANNEL_ID = "contact_tracing_channel"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "BLE Contact Tracing",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Canal para el servicio de rastreo por BLE"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    fun getForegroundNotification(context: Context): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Rastreo de Cercanía Activo")
            .setContentText("La aplicación está detectando dispositivos cercanos...")
            .setSmallIcon(R.drawable.ic_ble)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
}