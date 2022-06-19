package com.example.wiuh.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wiuh.R

const val CHANNEL_ID = "WIFI_CHANNEL_ID"
const val CHANNEL_NAME = "WIFI_CHANNEL_NAME"
const val CHANNEL_DESC = "WIFI_CHANNEL_DESC"

class NotificationUtil(private val context: Context) {

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
                .apply {
                    description = CHANNEL_DESC
                }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(title: String, message: String) {
        // 1
        createNotificationChannel()

        // 5
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        // 6
        NotificationManagerCompat.from(context).notify(0, notification)
    }
}