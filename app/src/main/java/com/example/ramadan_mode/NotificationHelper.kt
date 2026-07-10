package com.example.ramadan_mode

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * এই ক্লাসটা notification channel তৈরি করে এবং notification দেখানোর কাজ করে।
 */
object NotificationHelper {

    const val CHANNEL_ID = "ramadan_mode_channel"
    const val IFTAR_NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Ramadan Mode Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Sehri and Iftar time reminders"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun showIftarReminder(context: Context) {
        if (!hasNotificationPermission(context)) return

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Iftar Time Approaching")
            .setContentText("Iftar is coming soon. Get ready to break your fast!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // try-catch দিয়ে ঘিরে দেওয়া হলো, যাতে permission হঠাৎ revoke হলেও অ্যাপ crash না করে
        try {
            NotificationManagerCompat.from(context).notify(IFTAR_NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}