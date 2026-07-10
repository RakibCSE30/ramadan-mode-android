package com.example.ramadan_mode

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * এই Worker টা background এ চলে এবং নির্দিষ্ট সময়ে (Iftar এর কাছাকাছি)
 * notification দেখায়। WorkManager এটাকে schedule করে চালায়, এমনকি
 * অ্যাপ বন্ধ থাকলেও।
 */
class IftarReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        NotificationHelper.createNotificationChannel(applicationContext)
        NotificationHelper.showIftarReminder(applicationContext)
        return Result.success()
    }
}
