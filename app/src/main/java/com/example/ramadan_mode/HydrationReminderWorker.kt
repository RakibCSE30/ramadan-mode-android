package com.example.ramadan_mode

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * এই Worker টা প্রতি কয়েক ঘণ্টা পরপর চলে (WorkScheduler এ periodic ভাবে schedule করা হবে)।
 * এটা প্রথমে চেক করে এখন রোজার সময় (fasting) কিনা।
 * - রোজার সময় হলে → notification suppress (দেখাবে না)
 * - রোজার সময় না হলে → hydration reminder notification দেখাবে
 */
class HydrationReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val isFasting = PrayerTimeStorage.isFastingTimeNow(applicationContext)

        if (!isFasting) {
            NotificationHelper.createNotificationChannel(applicationContext)
            NotificationHelper.showHydrationReminder(applicationContext)
        }
        // isFasting == true হলে কিছুই করা হচ্ছে না — এটাই suppression logic

        return Result.success()
    }
}
