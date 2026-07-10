package com.example.ramadan_mode

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * এই অবজেক্টটা Iftar reminder ও Hydration reminder — দুইটাই schedule করে।
 */
object WorkScheduler {

    private const val IFTAR_WORK_TAG = "iftar_reminder_work"
    private const val HYDRATION_WORK_NAME = "hydration_reminder_work"

    /**
     * Iftar এর ১৫ মিনিট আগে একবারের জন্য (one-time) notification schedule করে।
     */
    fun scheduleIftarReminder(context: Context, maghribTime: Date) {
        val reminderTime = maghribTime.time - TimeUnit.MINUTES.toMillis(15)
        val currentTime = System.currentTimeMillis()
        val delay = reminderTime - currentTime

        if (delay <= 0) return

        val workRequest = OneTimeWorkRequestBuilder<IftarReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag(IFTAR_WORK_TAG)
            .build()

        WorkManager.getInstance(context).cancelAllWorkByTag(IFTAR_WORK_TAG)
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    /**
     * প্রতি ২ ঘণ্টা পরপর চেক করে hydration reminder দেখাবে
     * (রোজার সময় হলে worker নিজে থেকেই suppress করে দেবে — এই লজিক HydrationReminderWorker এ আছে)
     */
    fun scheduleHydrationReminder(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<HydrationReminderWorker>(
            2, TimeUnit.HOURS
        ).build()

        // এটা নিশ্চিত করে যে বারবার নতুন করে schedule না হয়ে একটাই periodic worker চলে
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            HYDRATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}