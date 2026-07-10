package com.example.ramadan_mode

import android.content.Context

/**
 * এই ক্লাসটা আজকের Fajr ও Maghrib সময় (millisecond আকারে) সেভ করে রাখে,
 * যাতে background এ চলা HydrationReminderWorker এটা পড়ে বুঝতে পারে
 * এখন রোজার সময় (fasting window) চলছে কিনা।
 */
object PrayerTimeStorage {

    private const val PREFS_NAME = "ramadan_mode_prefs"
    private const val KEY_FAJR_MILLIS = "fajr_millis"
    private const val KEY_MAGHRIB_MILLIS = "maghrib_millis"

    fun saveTodayTimes(context: Context, fajrMillis: Long, maghribMillis: Long) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putLong(KEY_FAJR_MILLIS, fajrMillis)
            .putLong(KEY_MAGHRIB_MILLIS, maghribMillis)
            .apply()
    }

    fun isFastingTimeNow(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val fajrMillis = prefs.getLong(KEY_FAJR_MILLIS, -1)
        val maghribMillis = prefs.getLong(KEY_MAGHRIB_MILLIS, -1)

        // যদি এখনো কোনো সময় সেভ করা না থাকে, ধরে নিচ্ছি fasting time না
        if (fajrMillis == -1L || maghribMillis == -1L) return false

        val now = System.currentTimeMillis()
        return now in fajrMillis..maghribMillis
    }
}
