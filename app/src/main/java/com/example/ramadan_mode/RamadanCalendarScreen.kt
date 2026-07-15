package com.example.ramadan_mode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import java.util.Date

// ---------- Palette (matches the reference "মাহে রমজান" mockup) ----------
private val RamadanGreen = Color(0xFF1E8449)
private val RamadanGreenLight = Color(0xFFEAF6EE)
private val RowStripe = Color(0xFFF3F3F3)
private val TextDark = Color(0xFF1A1A1A)
private val TextMuted = Color(0xFF6B6B6B)

data class RamadanDayEntry(
    val dayNumber: Int,
    val date: Calendar,
    val sehriTime: Date,
    val iftarTime: Date,
    val totalFastMinutes: Long
)

/**
 * Builds the full-month Ramadan Sehri/Iftar table for a given location, starting from
 * [startDate], for [days] consecutive days (default 30 — a full Ramadan month).
 */
fun generateRamadanCalendar(
    prayerTimeHelper: PrayerTimeHelper,
    lat: Double,
    lon: Double,
    startDate: Calendar,
    days: Int = 30
): List<RamadanDayEntry> {
    val result = mutableListOf<RamadanDayEntry>()
    val cursor = startDate.clone() as Calendar
    for (i in 0 until days) {
        val times = prayerTimeHelper.getPrayerTimesForDate(lat, lon, cursor)
        val totalMinutes = (times.maghribDate.time - times.fajrDate.time) / (1000 * 60)
        result.add(
            RamadanDayEntry(
                dayNumber = i + 1,
                date = cursor.clone() as Calendar,
                sehriTime = times.fajrDate,
                iftarTime = times.maghribDate,
                totalFastMinutes = totalMinutes
            )
        )
        cursor.add(Calendar.DAY_OF_YEAR, 1)
    }
    return result
}

private val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

fun String.toBanglaDigits(): String = this.map { ch ->
    if (ch.isDigit()) banglaDigits[ch - '0'] else ch
}.joinToString("")

private fun formatClock(date: Date): String {
    val fmt = java.text.SimpleDateFormat("hh:mm", java.util.Locale.getDefault())
    return fmt.format(date).toBanglaDigits()
}

private fun formatDateShort(cal: Calendar): String {
    val fmt = java.text.SimpleDateFormat("dd-MM-yy", java.util.Locale.getDefault())
    return fmt.format(cal.time).toBanglaDigits()
}

private val weekdayShortBn = arrayOf("রবি", "সোম", "মঙ্গল", "বুধ", "বৃহ", "শুক্র", "শনি")

private fun weekdayShort(cal: Calendar): String {
    // Calendar.DAY_OF_WEEK: 1=Sunday ... 7=Saturday
    return weekdayShortBn[cal.get(Calendar.DAY_OF_WEEK) - 1]
}

@Composable
fun RamadanCalendarScreen(
    entries: List<RamadanDayEntry>,
    districtName: String,
    onDistrictClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().background(Color(0xFFFBF3E3))) {

        // ---------- Top green app bar ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(RamadanGreen)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("\uD83C\uDF19", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "মাহে রমজান ২০২৬",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Text("\u2630", color = Color.White, fontSize = 20.sp)
        }

        val firstEntry = entries.firstOrNull()

        // ---------- Today summary card ----------
        if (firstEntry != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RamadanGreenLight)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "${firstEntry.dayNumber.toString().toBanglaDigits()}ম রমজান",
                            color = RamadanGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "সেহেরী : ${formatClock(firstEntry.sehriTime)} মিনিট",
                            color = TextDark,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "ইফতার : ${formatClock(firstEntry.iftarTime)} মিনিট",
                            color = TextDark,
                            fontSize = 13.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "আজ ${formatDateShort(firstEntry.date)}",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val hh = (firstEntry.totalFastMinutes / 60).toString().padStart(2, '0').toBanglaDigits()
                        val mm = (firstEntry.totalFastMinutes % 60).toString().padStart(2, '0').toBanglaDigits()
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            DigitBox(hh)
                            DigitBox(mm)
                            DigitBox("০০")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ---------- District picker pill ----------
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(RamadanGreen, RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .clickable(onClick = onDistrictClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("\uD83D\uDCCD", fontSize = 13.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(districtName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "জেলার সেহেরী ও ইফতারের সময়সূচি",
                color = TextDark,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ---------- Table header ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(RamadanGreenLight)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            TableHeaderCell("তারিখ", 1.3f)
            TableHeaderCell("সেহেরী", 1f)
            TableHeaderCell("ইফতার", 1f)
            TableHeaderCell("মোট সময়", 1.4f)
        }

        // ---------- Table rows (scrollable) ----------
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(entries) { entry ->
                val isEven = entry.dayNumber % 2 == 0
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isEven) RowStripe else Color.White)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    TableCell(
                        "${entry.dayNumber.toString().padStart(2, '0').toBanglaDigits()} ${formatDateShort(entry.date)} ${weekdayShort(entry.date)}",
                        1.3f
                    )
                    TableCell(formatClock(entry.sehriTime), 1f)
                    TableCell(formatClock(entry.iftarTime), 1f)
                    val h = (entry.totalFastMinutes / 60).toString().toBanglaDigits()
                    val m = (entry.totalFastMinutes % 60).toString().toBanglaDigits()
                    TableCell("$h ঘন্টা $m মিনিট", 1.4f)
                }
            }
        }

        // ---------- Bottom nav ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(RamadanGreenLight)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem("রোযা")
            BottomNavItem("দোয়া - নিয়ত")
            BottomNavItem("ফজিলত")
            BottomNavItem("ছোট সুরা")
        }
    }
}

@Composable
private fun DigitBox(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFF1A1A1A), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun RowScope.TableHeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        color = RamadanGreen,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
    )
}

@Composable
private fun RowScope.TableCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        color = TextDark,
        fontSize = 12.sp
    )
}

@Composable
private fun RowScope.BottomNavItem(text: String) {
    Text(
        text = text,
        modifier = Modifier.weight(1f),
        color = RamadanGreen,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}