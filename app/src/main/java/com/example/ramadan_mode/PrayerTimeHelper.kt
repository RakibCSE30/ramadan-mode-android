package com.example.ramadan_mode

import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.math.*

/**
 * NOTE (best-guess extension):
 * The original PrayerTimeHelper.kt used elsewhere in the project (getTodayPrayerTimes)
 * was not shared, so this file re-implements the calculation from scratch using the
 * standard sun-angle method (Fajr angle 18°, Maghrib = sunset). If your real
 * PrayerTimeHelper produces slightly different times than this file, replace the
 * body of getPrayerTimesForDate() below with your real formula/library call — the
 * rest of the app (RamadanCalendarScreen) only depends on this function's signature,
 * not its internals.
 */

data class PrayerTimesResult(
    val fajrDate: Date,
    val maghribDate: Date
)

class PrayerTimeHelper {

    private val fajrAngle = 18.0 // Muslim World League convention

    /** Existing entry point used by MainActivity — keeps calculating for "today". */
    fun getTodayPrayerTimes(lat: Double, lon: Double): PrayerTimesResult {
        return getPrayerTimesForDate(lat, lon, Calendar.getInstance())
    }

    /**
     * New: calculate Sehri(Fajr)/Iftar(Maghrib) for ANY given date — needed to build
     * a full-month Ramadan calendar table.
     */
    fun getPrayerTimesForDate(lat: Double, lon: Double, date: Calendar): PrayerTimesResult {
        val cal = date.clone() as Calendar
        val timeZoneOffsetHours = cal.get(Calendar.ZONE_OFFSET).toDouble() / (1000 * 60 * 60) +
                cal.get(Calendar.DST_OFFSET).toDouble() / (1000 * 60 * 60)

        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)

        val fajrHour = sunAngleTime(dayOfYear, lat, lon, timeZoneOffsetHours, fajrAngle, isMorning = true)
        val maghribHour = sunsetTime(dayOfYear, lat, lon, timeZoneOffsetHours)

        val fajrDate = hourToDate(cal, fajrHour)
        val maghribDate = hourToDate(cal, maghribHour)

        return PrayerTimesResult(fajrDate, maghribDate)
    }

    // ---- Standard astronomical sun-position helpers (PrayTimes.org style algorithm) ----

    private fun sunDeclination(dayOfYear: Int): Double {
        return 23.45 * sin(Math.toRadians((360.0 / 365.0) * (dayOfYear - 81)))
    }

    private fun equationOfTime(dayOfYear: Int): Double {
        val b = Math.toRadians((360.0 / 365.0) * (dayOfYear - 81))
        return 9.87 * sin(2 * b) - 7.53 * cos(b) - 1.5 * sin(b)
    }

    /** Hour (0-24, local clock time) at which the sun is `angle` degrees below the horizon, before sunrise. */
    private fun sunAngleTime(
        dayOfYear: Int,
        lat: Double,
        lon: Double,
        tzOffset: Double,
        angle: Double,
        isMorning: Boolean
    ): Double {
        val decl = Math.toRadians(sunDeclination(dayOfYear))
        val latRad = Math.toRadians(lat)
        val eqt = equationOfTime(dayOfYear)

        val cosHourAngle = (-sin(Math.toRadians(angle)) - sin(latRad) * sin(decl)) / (cos(latRad) * cos(decl))
        val clamped = cosHourAngle.coerceIn(-1.0, 1.0)
        val hourAngle = Math.toDegrees(acos(clamped)) / 15.0

        val solarNoon = 12.0 - (lon / 15.0 - tzOffset) - eqt / 60.0
        return if (isMorning) solarNoon - hourAngle else solarNoon + hourAngle
    }

    private fun sunsetTime(dayOfYear: Int, lat: Double, lon: Double, tzOffset: Double): Double {
        // Sunset ~ sun at 0.833° below horizon (accounts for refraction + solar radius)
        val decl = Math.toRadians(sunDeclination(dayOfYear))
        val latRad = Math.toRadians(lat)
        val eqt = equationOfTime(dayOfYear)

        val angle = 0.833
        val cosHourAngle = (-sin(Math.toRadians(angle)) - sin(latRad) * sin(decl)) / (cos(latRad) * cos(decl))
        val clamped = cosHourAngle.coerceIn(-1.0, 1.0)
        val hourAngle = Math.toDegrees(acos(clamped)) / 15.0

        val solarNoon = 12.0 - (lon / 15.0 - tzOffset) - eqt / 60.0
        return solarNoon + hourAngle
    }

    private fun hourToDate(baseCal: Calendar, hourDecimal: Double): Date {
        val cal = baseCal.clone() as Calendar
        var h = hourDecimal
        if (h < 0) h += 24
        if (h >= 24) h -= 24
        val hours = h.toInt()
        val minutesDecimal = (h - hours) * 60
        val minutes = minutesDecimal.toInt()
        val seconds = ((minutesDecimal - minutes) * 60).toInt()
        cal.set(Calendar.HOUR_OF_DAY, hours)
        cal.set(Calendar.MINUTE, minutes)
        cal.set(Calendar.SECOND, seconds)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }
}