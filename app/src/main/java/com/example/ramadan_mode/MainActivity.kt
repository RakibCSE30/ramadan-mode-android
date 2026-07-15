//package com.example.ramadan_mode
//
//import android.Manifest
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.ramadan_mode.ui.theme.RamadanmodeTheme
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import java.util.Calendar
//import java.util.Date
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            RamadanmodeTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    LocationScreen(modifier = Modifier.padding(innerPadding))
//                }
//            }
//        }
//    }
//}
//
//// ---------- Color palette (matches the refreshed mockup) ----------
//private val NavyCard = Color(0xFF0F1B3D)
//private val CreamBackground = Color(0xFFFBF3E3)
//private val GoldAccent = Color(0xFFE8C468)
//private val RingTrack = Color(0xFF223066)
//private val MutedTextOnDark = Color(0xFFB8C0DE)
//private val CardWhite = Color.White
//private val SubtleGray = Color(0xFF6B6B6B)
//private val HydrationBlue = Color(0xFF3E7BFA)
//private val CalorieOrange = Color(0xFFE8935C)
//private val DarkText = Color(0xFF1A1A1A)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LocationScreen(modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val locationHelper = remember { LocationHelper(context) }
//    val prayerTimeHelper = remember { PrayerTimeHelper() }
//    val coroutineScope = rememberCoroutineScope()
//
//    var locationText by remember { mutableStateOf("Location not fetched yet") }
//    var sehriTime by remember { mutableStateOf<Date?>(null) }
//    var iftarTime by remember { mutableStateOf<Date?>(null) }
//
//    var expanded by remember { mutableStateOf(false) }
//    var selectedDistrict by remember { mutableStateOf<District?>(null) }
//
//    fun calculateAndShowPrayerTimes(lat: Double, lon: Double) {
//        val prayerTimes = prayerTimeHelper.getTodayPrayerTimes(lat, lon)
//        sehriTime = prayerTimes.fajrDate
//        iftarTime = prayerTimes.maghribDate
//
//        PrayerTimeStorage.saveTodayTimes(
//            context,
//            prayerTimes.fajrDate.time,
//            prayerTimes.maghribDate.time
//        )
//
//        WorkScheduler.scheduleIftarReminder(context, prayerTimes.maghribDate)
//        WorkScheduler.scheduleHydrationReminder(context)
//    }
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            locationText = "Fetching location..."
//            coroutineScope.launch {
//                val location = locationHelper.getCurrentLocation()
//                if (location != null) {
//                    locationText = "Location found"
//                    calculateAndShowPrayerTimes(location.first, location.second)
//                } else {
//                    locationText = "Couldn't get location (turn on GPS)"
//                }
//            }
//        } else {
//            locationText = "Permission not granted"
//        }
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(CreamBackground)
//            .verticalScroll(rememberScrollState())
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Nourish",
//            fontSize = 26.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF1A1A1A)
//        )
//        Spacer(modifier = Modifier.height(4.dp))
//        Text(
//            text = "\uD83D\uDCCD " + (selectedDistrict?.name ?: locationText),
//            fontSize = 14.sp,
//            color = SubtleGray
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // ---------- Ramadan Mode Countdown Card (circular ring) ----------
//        RamadanCountdownCard(sehriTime = sehriTime, iftarTime = iftarTime)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // ---------- Hydration + Calorie summary row ----------
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            InfoStatCard(
//                modifier = Modifier.weight(1f),
//                emoji = "\uD83D\uDCA7",
//                title = "Hydration",
//                statusLabel = "Paused",
//                accentColor = HydrationBlue,
//                description = "Reminders paused during the fast. Drink 2.5L after Iftar."
//            )
//            InfoStatCard(
//                modifier = Modifier.weight(1f),
//                emoji = "\uD83C\uDF7D\uFE0F",
//                title = "Calorie Tracking",
//                statusLabel = "Restricted",
//                accentColor = CalorieOrange,
//                description = "Goal: 1800kcal (Iftar to Sehri window)"
//            )
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // ---------- Get My Location Button ----------
//        Button(
//            onClick = {
//                if (locationHelper.hasLocationPermission()) {
//                    coroutineScope.launch {
//                        val location = locationHelper.getCurrentLocation()
//                        if (location != null) {
//                            locationText = "Location found"
//                            selectedDistrict = null
//                            calculateAndShowPrayerTimes(location.first, location.second)
//                        } else {
//                            locationText = "Couldn't get location"
//                        }
//                    }
//                } else {
//                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(24.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCBB9F5))
//        ) {
//            Text("Use My Location", color = Color(0xFF2A1B5C), fontWeight = FontWeight.Medium)
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//        Text("Or pick your district:", fontWeight = FontWeight.Medium, color = DarkText)
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // ---------- District Dropdown ----------
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = { expanded = !expanded }
//        ) {
//            TextField(
//                value = selectedDistrict?.name ?: "Select district",
//                onValueChange = {},
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                modifier = Modifier
//                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
//                    .fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                colors = TextFieldDefaults.colors(
//                    focusedTextColor = DarkText,
//                    unfocusedTextColor = DarkText,
//                    disabledTextColor = DarkText,
//                    focusedContainerColor = CardWhite,
//                    unfocusedContainerColor = CardWhite,
//                    disabledContainerColor = CardWhite
//                )
//            )
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false },
//                containerColor = CardWhite,
//                modifier = Modifier.background(CardWhite)
//            ) {
//                DistrictData.districts.forEach { district ->
//                    DropdownMenuItem(
//                        text = { Text(district.name, color = DarkText) },
//                        onClick = {
//                            selectedDistrict = district
//                            expanded = false
//                            calculateAndShowPrayerTimes(district.latitude, district.longitude)
//                        },
//                        modifier = Modifier.background(CardWhite)
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // ---------- Nutrition Suggestions Card ----------
//        SectionCard(title = "Iftar Nutrition Tips") {
//            NutritionData.iftarSuggestions.forEach { item ->
//                Text(
//                    text = "•  ${item.name} — ${item.benefit}",
//                    fontSize = 14.sp,
//                    color = DarkText,
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // ---------- Dua Card ----------
//        SectionCard(title = "Sehri / Fasting Intention (Niyyah)") {
//            Text(text = DuaData.sehriDua.title, fontWeight = FontWeight.Medium, color = DarkText)
//            Text(
//                text = DuaData.sehriDua.arabic,
//                fontSize = 15.sp,
//                color = DarkText,
//                modifier = Modifier.padding(vertical = 4.dp)
//            )
//            Text(text = DuaData.sehriDua.meaning, fontSize = 13.sp, color = SubtleGray)
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Text(text = DuaData.iftarDua.title, fontWeight = FontWeight.Medium, color = DarkText)
//            Text(
//                text = DuaData.iftarDua.arabic,
//                fontSize = 15.sp,
//                color = DarkText,
//                modifier = Modifier.padding(vertical = 4.dp)
//            )
//            Text(text = DuaData.iftarDua.meaning, fontSize = 13.sp, color = SubtleGray)
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // ---------- Things That Break Fast Card ----------
//        SectionCard(title = "Things That Break the Fast") {
//            DuaData.thingsThatBreakFast.forEach { reason ->
//                Text(
//                    text = "•  $reason",
//                    fontSize = 14.sp,
//                    color = DarkText,
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//    }
//}
//
///**
// * Top dark navy card — shows a circular progress ring around a live countdown
// * to Iftar (while fasting) or to the end of Sehri (while not fasting).
// */
//@Composable
//fun RamadanCountdownCard(sehriTime: Date?, iftarTime: Date?) {
//    var remainingText by remember { mutableStateOf("--:--:--") }
//    var labelText by remember { mutableStateOf("Calculating...") }
//    var elapsedText by remember { mutableStateOf("") }
//    var isFasting by remember { mutableStateOf(true) }
//    var progress by remember { mutableStateOf(0f) }
//
//    LaunchedEffect(sehriTime, iftarTime) {
//        while (true) {
//            if (sehriTime != null && iftarTime != null) {
//                val now = Calendar.getInstance().time
//
//                val target: Date
//                val phaseStart: Date
//                if (now.before(iftarTime) && now.after(sehriTime)) {
//                    // Fasting -> counting down to Iftar
//                    isFasting = true
//                    labelText = "Until Iftar"
//                    target = iftarTime
//                    phaseStart = sehriTime
//                } else {
//                    // Not fasting -> counting down to end of next Sehri
//                    isFasting = false
//                    labelText = "Until Sehri Ends"
//                    target = Calendar.getInstance().apply {
//                        time = sehriTime
//                        if (now.after(sehriTime)) add(Calendar.DAY_OF_YEAR, 1)
//                    }.time
//                    phaseStart = iftarTime
//                }
//
//                val diffMillis = target.time - now.time
//                if (diffMillis > 0) {
//                    val hours = diffMillis / (1000 * 60 * 60)
//                    val minutes = (diffMillis / (1000 * 60)) % 60
//                    val seconds = (diffMillis / 1000) % 60
//                    remainingText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
//                } else {
//                    remainingText = "00:00:00"
//                }
//
//                val totalPhaseMillis = target.time - phaseStart.time
//                val elapsedMillis = now.time - phaseStart.time
//                progress = if (totalPhaseMillis > 0) {
//                    (elapsedMillis.toFloat() / totalPhaseMillis.toFloat()).coerceIn(0f, 1f)
//                } else 0f
//
//                if (isFasting) {
//                    val eh = elapsedMillis / (1000 * 60 * 60)
//                    val em = (elapsedMillis / (1000 * 60)) % 60
//                    elapsedText = "Current Fast: ${eh}h ${em}m (Fajr was ${formatTime(sehriTime)})"
//                } else {
//                    elapsedText = ""
//                }
//            }
//            delay(1000)
//        }
//    }
//
//    Card(
//        shape = RoundedCornerShape(20.dp),
//        colors = CardDefaults.cardColors(containerColor = NavyCard),
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = if (isFasting) "\uD83C\uDF19 RAMADAN MODE ACTIVE" else "RAMADAN MODE",
//                color = GoldAccent,
//                fontWeight = FontWeight.Bold,
//                fontSize = 15.sp,
//                letterSpacing = 1.sp
//            )
//            Spacer(modifier = Modifier.height(20.dp))
//
//            Box(contentAlignment = Alignment.Center) {
//                Canvas(modifier = Modifier.size(200.dp)) {
//                    val strokeWidth = 14.dp.toPx()
//                    val diameter = size.minDimension - strokeWidth
//                    val topLeft = androidx.compose.ui.geometry.Offset(
//                        (size.width - diameter) / 2f,
//                        (size.height - diameter) / 2f
//                    )
//                    val arcSize = Size(diameter, diameter)
//
//                    // Track
//                    drawArc(
//                        color = RingTrack,
//                        startAngle = -90f,
//                        sweepAngle = 360f,
//                        useCenter = false,
//                        topLeft = topLeft,
//                        size = arcSize,
//                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
//                    )
//                    // Progress
//                    drawArc(
//                        color = GoldAccent,
//                        startAngle = -90f,
//                        sweepAngle = 360f * progress,
//                        useCenter = false,
//                        topLeft = topLeft,
//                        size = arcSize,
//                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
//                    )
//                }
//
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(
//                        text = labelText,
//                        color = MutedTextOnDark,
//                        fontSize = 13.sp
//                    )
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        text = remainingText,
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 32.sp,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            if (elapsedText.isNotEmpty()) {
//                Text(
//                    text = elapsedText,
//                    color = MutedTextOnDark,
//                    fontSize = 12.sp
//                )
//                Spacer(modifier = Modifier.height(6.dp))
//            }
//
//            if (sehriTime != null && iftarTime != null) {
//                Text(
//                    text = "Sehri ends: ${formatTime(sehriTime)}   •   Iftar: ${formatTime(iftarTime)}",
//                    color = MutedTextOnDark,
//                    fontSize = 12.sp
//                )
//            }
//        }
//    }
//}
//
///**
// * Small stat card used for the Hydration / Calorie Tracking row under the countdown.
// */
//@Composable
//fun InfoStatCard(
//    modifier: Modifier = Modifier,
//    emoji: String,
//    title: String,
//    statusLabel: String,
//    accentColor: Color,
//    description: String
//) {
//    Card(
//        modifier = modifier,
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = CardWhite),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Column(modifier = Modifier.padding(14.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(text = emoji, fontSize = 18.sp)
//                Spacer(modifier = Modifier.width(6.dp))
//                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkText)
//            }
//            Spacer(modifier = Modifier.height(6.dp))
//            Text(
//                text = statusLabel,
//                color = accentColor,
//                fontWeight = FontWeight.Medium,
//                fontSize = 12.sp
//            )
//            Spacer(modifier = Modifier.height(6.dp))
//            Text(
//                text = description,
//                color = SubtleGray,
//                fontSize = 11.sp,
//                lineHeight = 15.sp
//            )
//        }
//    }
//}
//
///**
// * Generic white section card, reused for Nutrition, Dua, etc.
// */
//@Composable
//fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
//    Card(
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = CardWhite),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(
//                text = title,
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp,
//                color = DarkText,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            content()
//        }
//    }
//}
//
//private fun formatTime(date: Date): String {
//    val fmt = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
//    return fmt.format(date)
//}


package com.example.ramadan_mode

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ramadan_mode.ui.theme.RamadanmodeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RamadanmodeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LocationScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// ---------- Color palette (matches the refreshed mockup) ----------
private val NavyCard = Color(0xFF0F1B3D)
private val CreamBackground = Color(0xFFFBF3E3)
private val GoldAccent = Color(0xFFE8C468)
private val RingTrack = Color(0xFF223066)
private val MutedTextOnDark = Color(0xFFB8C0DE)
private val CardWhite = Color.White
private val SubtleGray = Color(0xFF6B6B6B)
private val HydrationBlue = Color(0xFF3E7BFA)
private val CalorieOrange = Color(0xFFE8935C)
private val DarkText = Color(0xFF1A1A1A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    val prayerTimeHelper = remember { PrayerTimeHelper() }
    val coroutineScope = rememberCoroutineScope()

    var locationText by remember { mutableStateOf("Location not fetched yet") }
    var sehriTime by remember { mutableStateOf<Date?>(null) }
    var iftarTime by remember { mutableStateOf<Date?>(null) }

    var expanded by remember { mutableStateOf(false) }
    var selectedDistrict by remember { mutableStateOf<District?>(null) }

    // Coordinates used for the full-month calendar (falls back to Dhaka if nothing picked yet)
    var activeLat by remember { mutableStateOf(23.8103) }
    var activeLon by remember { mutableStateOf(90.4125) }
    var showFullCalendar by remember { mutableStateOf(false) }

    fun calculateAndShowPrayerTimes(lat: Double, lon: Double) {
        activeLat = lat
        activeLon = lon
        val prayerTimes = prayerTimeHelper.getTodayPrayerTimes(lat, lon)
        sehriTime = prayerTimes.fajrDate
        iftarTime = prayerTimes.maghribDate

        PrayerTimeStorage.saveTodayTimes(
            context,
            prayerTimes.fajrDate.time,
            prayerTimes.maghribDate.time
        )

        WorkScheduler.scheduleIftarReminder(context, prayerTimes.maghribDate)
        WorkScheduler.scheduleHydrationReminder(context)
    }

    if (showFullCalendar) {
        val startCal = remember(activeLat, activeLon) { Calendar.getInstance() }
        val calendarEntries = remember(activeLat, activeLon) {
            generateRamadanCalendar(prayerTimeHelper, activeLat, activeLon, startCal, days = 30)
        }
        Column(modifier = modifier.fillMaxSize()) {
            TextButton(onClick = { showFullCalendar = false }) {
                Text("← ফিরে যান")
            }
            RamadanCalendarScreen(
                entries = calendarEntries,
                districtName = selectedDistrict?.name ?: "ঢাকা",
                onDistrictClick = { showFullCalendar = false },
                modifier = Modifier.weight(1f)
            )
        }
        return
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            locationText = "Fetching location..."
            coroutineScope.launch {
                val location = locationHelper.getCurrentLocation()
                if (location != null) {
                    locationText = "Location found"
                    calculateAndShowPrayerTimes(location.first, location.second)
                } else {
                    locationText = "Couldn't get location (turn on GPS)"
                }
            }
        } else {
            locationText = "Permission not granted"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Nourish",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "\uD83D\uDCCD " + (selectedDistrict?.name ?: locationText),
            fontSize = 14.sp,
            color = SubtleGray
        )
        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Ramadan Mode Countdown Card (circular ring) ----------
        RamadanCountdownCard(sehriTime = sehriTime, iftarTime = iftarTime)

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Hydration + Calorie summary row ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoStatCard(
                modifier = Modifier.weight(1f),
                emoji = "\uD83D\uDCA7",
                title = "Hydration",
                statusLabel = "Paused",
                accentColor = HydrationBlue,
                description = "Reminders paused during the fast. Drink 2.5L after Iftar."
            )
            InfoStatCard(
                modifier = Modifier.weight(1f),
                emoji = "\uD83C\uDF7D\uFE0F",
                title = "Calorie Tracking",
                statusLabel = "Restricted",
                accentColor = CalorieOrange,
                description = "Goal: 1800kcal (Iftar to Sehri window)"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------- Get My Location Button ----------
        Button(
            onClick = {
                if (locationHelper.hasLocationPermission()) {
                    coroutineScope.launch {
                        val location = locationHelper.getCurrentLocation()
                        if (location != null) {
                            locationText = "Location found"
                            selectedDistrict = null
                            calculateAndShowPrayerTimes(location.first, location.second)
                        } else {
                            locationText = "Couldn't get location"
                        }
                    }
                } else {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCBB9F5))
        ) {
            Text("Use My Location", color = Color(0xFF2A1B5C), fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { showFullCalendar = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E8449))
        ) {
            Text("পূর্ণ রমজান ক্যালেন্ডার দেখুন", color = Color.White, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Or pick your district:", fontWeight = FontWeight.Medium, color = DarkText)
        Spacer(modifier = Modifier.height(8.dp))

        // ---------- District Dropdown ----------
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedDistrict?.name ?: "Select district",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = DarkText,
                    unfocusedTextColor = DarkText,
                    disabledTextColor = DarkText,
                    focusedContainerColor = CardWhite,
                    unfocusedContainerColor = CardWhite,
                    disabledContainerColor = CardWhite
                )
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = CardWhite,
                modifier = Modifier.background(CardWhite)
            ) {
                DistrictData.districts.forEach { district ->
                    DropdownMenuItem(
                        text = { Text(district.name, color = DarkText) },
                        onClick = {
                            selectedDistrict = district
                            expanded = false
                            calculateAndShowPrayerTimes(district.latitude, district.longitude)
                        },
                        modifier = Modifier.background(CardWhite)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---------- Nutrition Suggestions Card ----------
        SectionCard(title = "Iftar Nutrition Tips") {
            NutritionData.iftarSuggestions.forEach { item ->
                Text(
                    text = "•  ${item.name} — ${item.benefit}",
                    fontSize = 14.sp,
                    color = DarkText,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Dua Card ----------
        SectionCard(title = "Sehri / Fasting Intention (Niyyah)") {
            Text(text = DuaData.sehriDua.title, fontWeight = FontWeight.Medium, color = DarkText)
            Text(
                text = DuaData.sehriDua.arabic,
                fontSize = 15.sp,
                color = DarkText,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(text = DuaData.sehriDua.meaning, fontSize = 13.sp, color = SubtleGray)

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = DuaData.iftarDua.title, fontWeight = FontWeight.Medium, color = DarkText)
            Text(
                text = DuaData.iftarDua.arabic,
                fontSize = 15.sp,
                color = DarkText,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(text = DuaData.iftarDua.meaning, fontSize = 13.sp, color = SubtleGray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Things That Break Fast Card ----------
        SectionCard(title = "Things That Break the Fast") {
            DuaData.thingsThatBreakFast.forEach { reason ->
                Text(
                    text = "•  $reason",
                    fontSize = 14.sp,
                    color = DarkText,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * Top dark navy card — shows a circular progress ring around a live countdown
 * to Iftar (while fasting) or to the end of Sehri (while not fasting).
 */
@Composable
fun RamadanCountdownCard(sehriTime: Date?, iftarTime: Date?) {
    var remainingText by remember { mutableStateOf("--:--:--") }
    var labelText by remember { mutableStateOf("Calculating...") }
    var elapsedText by remember { mutableStateOf("") }
    var isFasting by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(sehriTime, iftarTime) {
        while (true) {
            if (sehriTime != null && iftarTime != null) {
                val now = Calendar.getInstance().time

                val target: Date
                val phaseStart: Date
                if (now.before(iftarTime) && now.after(sehriTime)) {
                    // Fasting -> counting down to Iftar
                    isFasting = true
                    labelText = "Until Iftar"
                    target = iftarTime
                    phaseStart = sehriTime
                } else {
                    // Not fasting -> counting down to end of next Sehri
                    isFasting = false
                    labelText = "Until Sehri Ends"
                    target = Calendar.getInstance().apply {
                        time = sehriTime
                        if (now.after(sehriTime)) add(Calendar.DAY_OF_YEAR, 1)
                    }.time
                    phaseStart = iftarTime
                }

                val diffMillis = target.time - now.time
                if (diffMillis > 0) {
                    val hours = diffMillis / (1000 * 60 * 60)
                    val minutes = (diffMillis / (1000 * 60)) % 60
                    val seconds = (diffMillis / 1000) % 60
                    remainingText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                } else {
                    remainingText = "00:00:00"
                }

                val totalPhaseMillis = target.time - phaseStart.time
                val elapsedMillis = now.time - phaseStart.time
                progress = if (totalPhaseMillis > 0) {
                    (elapsedMillis.toFloat() / totalPhaseMillis.toFloat()).coerceIn(0f, 1f)
                } else 0f

                if (isFasting) {
                    val eh = elapsedMillis / (1000 * 60 * 60)
                    val em = (elapsedMillis / (1000 * 60)) % 60
                    elapsedText = "Current Fast: ${eh}h ${em}m (Fajr was ${formatTime(sehriTime)})"
                } else {
                    elapsedText = ""
                }
            }
            delay(1000)
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isFasting) "\uD83C\uDF19 RAMADAN MODE ACTIVE" else "RAMADAN MODE",
                color = GoldAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(200.dp)) {
                    val strokeWidth = 14.dp.toPx()
                    val diameter = size.minDimension - strokeWidth
                    val topLeft = androidx.compose.ui.geometry.Offset(
                        (size.width - diameter) / 2f,
                        (size.height - diameter) / 2f
                    )
                    val arcSize = Size(diameter, diameter)

                    // Track
                    drawArc(
                        color = RingTrack,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    // Progress
                    drawArc(
                        color = GoldAccent,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = labelText,
                        color = MutedTextOnDark,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = remainingText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (elapsedText.isNotEmpty()) {
                Text(
                    text = elapsedText,
                    color = MutedTextOnDark,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            if (sehriTime != null && iftarTime != null) {
                Text(
                    text = "Sehri ends: ${formatTime(sehriTime)}   •   Iftar: ${formatTime(iftarTime)}",
                    color = MutedTextOnDark,
                    fontSize = 12.sp
                )
            }
        }
    }
}

/**
 * Small stat card used for the Hydration / Calorie Tracking row under the countdown.
 */
@Composable
fun InfoStatCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    statusLabel: String,
    accentColor: Color,
    description: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkText)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = statusLabel,
                color = accentColor,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                color = SubtleGray,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}

/**
 * Generic white section card, reused for Nutrition, Dua, etc.
 */
@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DarkText,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}

private fun formatTime(date: Date): String {
    val fmt = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
    return fmt.format(date)
}