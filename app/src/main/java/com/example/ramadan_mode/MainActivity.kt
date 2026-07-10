package com.example.ramadan_mode

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ramadan_mode.ui.theme.RamadanmodeTheme
import kotlinx.coroutines.launch

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

@Composable
fun LocationScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    val prayerTimeHelper = remember { PrayerTimeHelper() }
    val coroutineScope = rememberCoroutineScope()

    var locationText by remember { mutableStateOf("Location: not fetched yet") }
    var sehriText by remember { mutableStateOf("") }
    var iftarText by remember { mutableStateOf("") }

    // লোকেশন পাওয়ার পর prayer time ক্যালকুলেট করার ফাংশন
    fun calculateAndShowPrayerTimes(lat: Double, lon: Double) {
        val prayerTimes = prayerTimeHelper.getTodayPrayerTimes(lat, lon)
        sehriText = "Sehri ends (Fajr): ${prayerTimes.sehriEndTime}"
        iftarText = "Iftar time (Maghrib): ${prayerTimes.iftarTime}"

        WorkScheduler.scheduleIftarReminder(context, prayerTimes.maghribDate)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            locationText = "Permission granted. Fetching location..."
            coroutineScope.launch {
                val location = locationHelper.getCurrentLocation()
                if (location != null) {
                    locationText = "Latitude: ${location.first}, Longitude: ${location.second}"
                    calculateAndShowPrayerTimes(location.first, location.second)
                } else {
                    locationText = "Could not fetch location (turn on GPS and try again)"
                }
            }
        } else {
            locationText = "Permission denied. We need location to calculate Sehri/Iftar time."
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Ramadan Mode")
        Text(text = locationText)
        Text(text = sehriText)
        Text(text = iftarText)
        Button(onClick = {
            if (locationHelper.hasLocationPermission()) {
                coroutineScope.launch {
                    val location = locationHelper.getCurrentLocation()
                    if (location != null) {
                        locationText = "Latitude: ${location.first}, Longitude: ${location.second}"
                        calculateAndShowPrayerTimes(location.first, location.second)
                    } else {
                        locationText = "Could not fetch location (turn on GPS and try again)"
                    }
                }
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }) {
            Text("Get My Location")
        }

        // Iftar Nutrition Suggestions দেখানো
        Text(text = "Iftar Nutrition Suggestions:")
        NutritionData.iftarSuggestions.forEach { item ->
            Text(text = "• ${item.name} — ${item.benefit}")
        }
    }
}