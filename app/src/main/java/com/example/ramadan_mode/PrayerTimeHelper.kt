

package com.example.ramadan_mode

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * এই ক্লাসটা lat/long ব্যবহার করে আজকের Sehri (Fajr) ও Iftar (Maghrib)
 * সময় ক্যালকুলেট করে।
 */
class PrayerTimeHelper {

    fun getTodayPrayerTimes(latitude: Double, longitude: Double): TodayPrayerTimes {
        // ইউজারের লোকেশন দিয়ে Coordinates অবজেক্ট বানানো
        val coordinates = Coordinates(latitude, longitude)

        // আজকের তারিখ বের করা
        val today = DateComponents.from(Date())

        // ক্যালকুলেশন method সেট করা — বাংলাদেশ/দক্ষিণ এশিয়ার জন্য Karachi method
        val params = CalculationMethod.KARACHI.parameters

        // Prayer times ক্যালকুলেট করা
        val prayerTimes = PrayerTimes(coordinates, today, params)

        // সময়গুলোকে readable format এ (যেমন "04:32 AM") রূপান্তর করা
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

        return TodayPrayerTimes(
            sehriEndTime = formatter.format(prayerTimes.fajr),
            iftarTime = formatter.format(prayerTimes.maghrib),
            fajrDate = prayerTimes.fajr,
            maghribDate = prayerTimes.maghrib
        )
    }
}

/**
 * আজকের Sehri ও Iftar এর সময় ধরে রাখার জন্য একটা ডেটা ক্লাস
 */
data class TodayPrayerTimes(
    val sehriEndTime: String,   // দেখানোর জন্য readable format, যেমন "04:32 AM"
    val iftarTime: String,      // যেমন "06:15 PM"
    val fajrDate: Date,         // আসল Date অবজেক্ট, পরে notification schedule করতে কাজে লাগবে
    val maghribDate: Date
)