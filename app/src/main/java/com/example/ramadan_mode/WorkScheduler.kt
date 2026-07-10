package com.example.ramadan_mode

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * এই অবজেক্টটা Iftar reminder notification-কে সঠিক সময়ে schedule করে।
 */
object WorkScheduler {

    private const val WORK_TAG = "iftar_reminder_work"

    /**
     * maghribTime হলো আজকের Iftar এর সময় (Date অবজেক্ট)।
     * আমরা Iftar এর ১৫ মিনিট আগে notification schedule করব।
     */
    fun scheduleIftarReminder(context: Context, maghribTime: Date) {
        val reminderTime = maghribTime.time - TimeUnit.MINUTES.toMillis(15)
        val currentTime = System.currentTimeMillis()
        val delay = reminderTime - currentTime

        // যদি সময়টা অতীতে চলে গিয়ে থাকে (যেমন আজকের Iftar পার হয়ে গেছে),
        // তাহলে আর schedule করার দরকার নেই
        if (delay <= 0) return

        val workRequest = OneTimeWorkRequestBuilder<IftarReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag(WORK_TAG)
            .build()

        // আগে থেকে কোনো reminder schedule করা থাকলে সেটা বাতিল করে নতুন করে বসানো হচ্ছে
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}