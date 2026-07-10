package com.example.ramadan_mode

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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

// ---------- কালার প্যালেট (মকআপ অনুযায়ী) ----------
private val NavyCard = Color(0xFF0F1B3D)
private val CreamBackground = Color(0xFFFBF3E3)
private val GoldAccent = Color(0xFFE8C468)
private val TealCard = Color(0xFF0E4D4A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    val prayerTimeHelper = remember { PrayerTimeHelper() }
    val coroutineScope = rememberCoroutineScope()

    var locationText by remember { mutableStateOf("অবস্থান এখনো নেওয়া হয়নি") }
    var sehriTime by remember { mutableStateOf<Date?>(null) }
    var iftarTime by remember { mutableStateOf<Date?>(null) }

    var expanded by remember { mutableStateOf(false) }
    var selectedDistrict by remember { mutableStateOf<District?>(null) }

    fun calculateAndShowPrayerTimes(lat: Double, lon: Double) {
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

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            locationText = "লোকেশন আনা হচ্ছে..."
            coroutineScope.launch {
                val location = locationHelper.getCurrentLocation()
                if (location != null) {
                    locationText = "লোকেশন পাওয়া গেছে"
                    calculateAndShowPrayerTimes(location.first, location.second)
                } else {
                    locationText = "লোকেশন পাওয়া যায়নি (GPS অন করুন)"
                }
            }
        } else {
            locationText = "পারমিশন দেওয়া হয়নি"
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
            text = selectedDistrict?.name ?: locationText,
            fontSize = 14.sp,
            color = Color(0xFF6B6B6B)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Ramadan Mode Countdown Card ----------
        RamadanCountdownCard(sehriTime = sehriTime, iftarTime = iftarTime)

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Get My Location Button ----------
        Button(
            onClick = {
                if (locationHelper.hasLocationPermission()) {
                    coroutineScope.launch {
                        val location = locationHelper.getCurrentLocation()
                        if (location != null) {
                            locationText = "লোকেশন পাওয়া গেছে"
                            selectedDistrict = null
                            calculateAndShowPrayerTimes(location.first, location.second)
                        } else {
                            locationText = "লোকেশন পাওয়া যায়নি"
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
            Text("আমার লোকেশন নিন", color = Color(0xFF2A1B5C), fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("অথবা আপনার জেলা বেছে নিন:", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))

        // ---------- District Dropdown ----------
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedDistrict?.name ?: "জেলা সিলেক্ট করুন",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DistrictData.districts.forEach { district ->
                    DropdownMenuItem(
                        text = { Text(district.name) },
                        onClick = {
                            selectedDistrict = district
                            expanded = false
                            calculateAndShowPrayerTimes(district.latitude, district.longitude)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---------- Nutrition Suggestions Card ----------
        SectionCard(title = "ইফতার পুষ্টি পরামর্শ") {
            NutritionData.iftarSuggestions.forEach { item ->
                Text(
                    text = "•  ${item.name} — ${item.benefit}",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Dua Card ----------
        SectionCard(title = "সেহরি/রোজার নিয়ত") {
            Text(text = DuaData.sehriDua.title, fontWeight = FontWeight.Medium)
            Text(text = DuaData.sehriDua.arabic, fontSize = 15.sp, modifier = Modifier.padding(vertical = 4.dp))
            Text(text = DuaData.sehriDua.meaning, fontSize = 13.sp, color = Color(0xFF6B6B6B))

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = DuaData.iftarDua.title, fontWeight = FontWeight.Medium)
            Text(text = DuaData.iftarDua.arabic, fontSize = 15.sp, modifier = Modifier.padding(vertical = 4.dp))
            Text(text = DuaData.iftarDua.meaning, fontSize = 13.sp, color = Color(0xFF6B6B6B))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Things That Break Fast Card ----------
        SectionCard(title = "যেসব কারণে রোজা ভঙ্গ হয়") {
            DuaData.thingsThatBreakFast.forEach { reason ->
                Text(
                    text = "•  $reason",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * উপরের ডার্ক নেভি কার্ড — countdown timer এবং সেহরি/ইফতার সময় দেখায়।
 * প্রতি সেকেন্ডে আপডেট হয় (live countdown)।
 */
@Composable
fun RamadanCountdownCard(sehriTime: Date?, iftarTime: Date?) {
    var remainingText by remember { mutableStateOf("--:--:--") }
    var labelText by remember { mutableStateOf("সময় গণনা করা হচ্ছে") }
    var isFasting by remember { mutableStateOf(true) }

    LaunchedEffect(sehriTime, iftarTime) {
        while (true) {
            if (sehriTime != null && iftarTime != null) {
                val now = Calendar.getInstance().time

                val target: Date
                if (now.before(iftarTime) && now.after(sehriTime)) {
                    // রোজা চলছে -> ইফতারের জন্য কাউন্টডাউন
                    isFasting = true
                    labelText = "ইফতারের বাকি সময়"
                    target = iftarTime
                } else {
                    // রোজা নাই -> পরের দিনের সেহরির জন্য কাউন্টডাউন
                    isFasting = false
                    labelText = "সেহরি শেষ হতে বাকি"
                    val nextSehri = Calendar.getInstance().apply {
                        time = sehriTime
                        if (now.after(sehriTime)) add(Calendar.DAY_OF_YEAR, 1)
                    }.time
                    target = nextSehri
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
                text = if (isFasting) "🌙 রামাদান মোড সক্রিয়" else "রামাদান মোড",
                color = GoldAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = labelText,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = remainingText,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 42.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (sehriTime != null && iftarTime != null) {
                Text(
                    text = "সেহরি শেষ: ${formatTime(sehriTime)}   •   ইফতার: ${formatTime(iftarTime)}",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

/**
 * সাধারণ সেকশন কার্ড — Nutrition, Dua, ইত্যাদির জন্য পুনঃব্যবহারযোগ্য।
 */
@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
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