package com.example.ramadan_mode

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ramadan_mode.ui.theme.RamadanmodeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        NotificationHelper.createNotificationChannel(this)
        setContent { RamadanRoot() }
    }
}

enum class Screen { Splash, Onboarding, Language, Auth, District, Home, Today, Quran, Ibadah, Dua, Zakat, Journey, Profile, Settings, Calendar }

data class AppState(
    var onboardingCompleted: Boolean = false,
    var language: String = "en",
    var district: String = "Dhaka",
    var isAuthenticated: Boolean = false,
    var userName: String = "Guest",
    var themeMode: String = "system",
    var notificationsEnabled: Boolean = true,
    var lastSurah: Int = 1,
    var lastAyah: Int = 1,
    var quranGoal: Int = 5
)

class AppPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("ramadan_app_state", Context.MODE_PRIVATE)
    fun load() = AppState(
        onboardingCompleted = prefs.getBoolean("onboardingCompleted", false),
        language = prefs.getString("selectedLanguage", "en") ?: "en",
        district = prefs.getString("selectedDistrict", "Dhaka") ?: "Dhaka",
        isAuthenticated = prefs.getBoolean("authenticationState", false),
        userName = prefs.getString("userProfile", "Guest") ?: "Guest",
        themeMode = prefs.getString("themeMode", "system") ?: "system",
        notificationsEnabled = prefs.getBoolean("notificationSettings", true),
        lastSurah = prefs.getInt("lastSurah", 1),
        lastAyah = prefs.getInt("lastAyah", 1),
        quranGoal = prefs.getInt("quranGoal", 5)
    )
    fun save(s: AppState) = prefs.edit()
        .putBoolean("onboardingCompleted", s.onboardingCompleted).putString("selectedLanguage", s.language)
        .putString("selectedDistrict", s.district).putBoolean("authenticationState", s.isAuthenticated)
        .putString("userProfile", s.userName).putString("themeMode", s.themeMode)
        .putBoolean("notificationSettings", s.notificationsEnabled).putInt("lastSurah", s.lastSurah)
        .putInt("lastAyah", s.lastAyah).putInt("quranGoal", s.quranGoal).apply()
    fun set(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()
    fun getSet(key: String) = (prefs.getStringSet(key, emptySet()) ?: emptySet()).toMutableSet()
    fun saveSet(key: String, values: Set<String>) = prefs.edit().putStringSet(key, values).apply()
    fun saveString(key: String, value: String) = prefs.edit().putString(key, value).apply()
    fun getString(key: String, fallback: String = "") = prefs.getString(key, fallback) ?: fallback
}

private val Green = Color(0xFF087A4B)
private val Gold = Color(0xFFE6B84A)
private val Cream = Color(0xFFFFF8EA)

@Composable
fun RamadanRoot() {
    val context = LocalContext.current
    val prefs = remember { AppPrefs(context) }
    var state by remember { mutableStateOf(prefs.load()) }
    var screen by remember { mutableStateOf(Screen.Splash) }
    val snackbar = remember { SnackbarHostState() }
    fun update(block: (AppState) -> Unit) { val s = state.copy(); block(s); state = s; prefs.save(s) }
    RamadanmodeTheme(darkTheme = state.themeMode == "dark", dynamicColor = false) {
        Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { padding ->
            Box(Modifier.padding(padding).fillMaxSize().background(Cream)) {
                when (screen) {
                    Screen.Splash -> SplashScreen(context) { screen = if (!state.onboardingCompleted) Screen.Onboarding else if (!state.isAuthenticated) Screen.Auth else Screen.Home }
                    Screen.Onboarding -> OnboardingScreen(state.language) { update { it.onboardingCompleted = true }; screen = Screen.Language }
                    Screen.Language -> LanguageScreen(state.language) { update { s -> s.language = it }; screen = Screen.Auth }
                    Screen.Auth -> AuthScreen(state.language, onAuth = { name -> update { it.isAuthenticated = true; it.userName = name }; screen = Screen.District })
                    Screen.District -> DistrictScreen(state, prefs, snackbar, update) { screen = Screen.Home }
                    Screen.Home -> HomeScreen(state) { screen = it }
                    Screen.Today -> TodayScreen(state) { screen = Screen.Home }
                    Screen.Calendar -> CalendarForState(state) { screen = Screen.District }
                    Screen.Quran -> QuranScreen(state, prefs, update) { screen = Screen.Home }
                    Screen.Ibadah -> IbadahScreen(prefs) { screen = Screen.Home }
                    Screen.Dua -> DuaScreen(prefs) { screen = Screen.Home }
                    Screen.Zakat -> ZakatScreen(prefs) { screen = Screen.Home }
                    Screen.Journey -> JourneyScreen(prefs) { screen = Screen.Home }
                    Screen.Profile -> ProfileScreen(state, { screen = Screen.Settings }, { update { it.isAuthenticated = false }; screen = Screen.Auth }, { screen = Screen.Home })
                    Screen.Settings -> SettingsScreen(state, update) { screen = Screen.Profile }
                }
            }
        }
    }
}

@Composable fun SplashScreen(context: Context, done: () -> Unit) { LaunchedEffect(Unit) { delay(1500); done() }; val online = isOnline(context); Center { Text("🌙 Ramadan Mode", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Green); Text(if (online) "Initializing your Ramadan companion" else "Offline mode: cached/local data available"); Spacer(Modifier.height(20.dp)); CircularProgressIndicator(color = Gold) } }
private fun isOnline(context: Context): Boolean { val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager; val nw = cm.activeNetwork ?: return false; val caps = cm.getNetworkCapabilities(nw) ?: return false; return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) }

@Composable fun Center(content: @Composable Column.() -> Unit) = Column(Modifier.fillMaxSize().padding(24.dp), Arrangement.Center, Alignment.CenterHorizontally, content = content)
@Composable fun Top(title: String, back: (() -> Unit)? = null) { Row(Modifier.fillMaxWidth().background(Green).padding(16.dp), verticalAlignment = Alignment.CenterVertically) { if (back != null) Text("‹ Back", color = Color.White, modifier = Modifier.clickable { back() }); Spacer(Modifier.width(12.dp)); Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp) } }
@Composable fun T(en: String, bn: String, lang: String) = Text(if (lang == "bn") bn else en)

@Composable fun OnboardingScreen(lang: String, finish: () -> Unit) { var page by remember { mutableStateOf(0) }; val pages = listOf("Prayer, fasting and Quran in one place", "Track ibadah streaks and goals", "Bangladesh district-aware Sehri and Iftar"); Center { Text("Welcome", fontSize = 28.sp, fontWeight = FontWeight.Bold); Text(pages[page], Modifier.padding(20.dp)); Row { TextButton(finish) { Text("Skip") }; Button({ if (page < pages.lastIndex) page++ else finish() }) { Text(if (page < pages.lastIndex) "Next" else "Finish") } }; T("Swipe-like pages with Next/Skip", "নেক্সট/স্কিপ দিয়ে শুরু করুন", lang) } }
@Composable fun LanguageScreen(selected: String, save: (String) -> Unit) = Center { Text("Choose Language / ভাষা", fontSize = 26.sp, fontWeight = FontWeight.Bold); Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { Button({ save("en") }) { Text("English ${if (selected=="en") "✓" else ""}") }; Button({ save("bn") }) { Text("বাংলা ${if (selected=="bn") "✓" else ""}") } } }

@Composable fun AuthScreen(lang: String, onAuth: (String) -> Unit) { var signup by remember { mutableStateOf(false) }; var name by remember { mutableStateOf("") }; var email by remember { mutableStateOf("") }; var pass by remember { mutableStateOf("") }; var error by remember { mutableStateOf("") }; Center { Text(if (signup) "Create Account" else "Login", fontSize = 28.sp, fontWeight = FontWeight.Bold); if (signup) OutlinedTextField(name, { name = it }, label={Text("Name")}); OutlinedTextField(email, { email = it }, label={Text("Email")}); OutlinedTextField(pass, { pass = it }, label={Text("Password")}, visualTransformation = PasswordVisualTransformation()); if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error); Button({ if (!email.contains('@') || pass.length < 4 || (signup && name.isBlank())) error = "Enter valid email, 4+ char password${if (signup) ", and name" else ""}" else onAuth(if (name.isBlank()) email.substringBefore('@') else name) }) { Text(if (signup) "Sign Up" else "Login") }; TextButton({ signup = !signup }) { T(if (signup) "Have an account? Login" else "Need an account? Sign up", if (signup) "লগইন করুন" else "সাইন আপ করুন", lang) } } }

@Composable fun DistrictScreen(state: AppState, prefs: AppPrefs, snackbar: SnackbarHostState, update: ((AppState) -> Unit) -> Unit, next: () -> Unit) { val context = LocalContext.current; val scope = rememberCoroutineScope(); val helper = remember { LocationHelper(context) }; var query by remember { mutableStateOf("") }; var status by remember { mutableStateOf("Select your district or detect location") }; val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok -> scope.launch { if (ok) { val loc = helper.getCurrentLocation(); val nearest = loc?.let { nearestDistrict(it.first, it.second) }; if (nearest != null) { update { it.district = nearest.name }; status = "Detected ${nearest.name}" } else status = "Location unavailable; choose manually" } else status = "Permission denied; choose manually" } }; Column(Modifier.fillMaxSize()) { Top("District Selection"); Column(Modifier.padding(16.dp)) { Text(status); Button({ if (helper.hasLocationPermission()) scope.launch { helper.getCurrentLocation()?.let { val d=nearestDistrict(it.first,it.second); update { s -> s.district=d.name }; status="Detected ${d.name}" } } else permission.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) { Text("Use my location") }; OutlinedTextField(query, { query=it }, label={Text("Search 64 districts")}, modifier=Modifier.fillMaxWidth()); LazyColumn(Modifier.weight(1f)) { items(DistrictData.districts.filter { it.name.contains(query, true) }) { d -> Row(Modifier.fillMaxWidth().clickable { update { it.district = d.name }; prefs.saveString("selectedDistrict", d.name) }.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(d.name); if (d.name==state.district) Text("✓") } } }; Button(next, Modifier.fillMaxWidth()) { Text("Continue with ${state.district}") } } } }
private fun nearestDistrict(lat: Double, lon: Double) = DistrictData.districts.minBy { (it.latitude-lat)*(it.latitude-lat)+(it.longitude-lon)*(it.longitude-lon) }

@Composable fun HomeScreen(state: AppState, nav: (Screen) -> Unit) { val items = listOf("Today" to Screen.Today,"Quran" to Screen.Quran,"Ibadah Tracker" to Screen.Ibadah,"Daily Dua" to Screen.Dua,"Zakat Calculator" to Screen.Zakat,"Ramadan Journey" to Screen.Journey,"Profile" to Screen.Profile,"Calendar" to Screen.Calendar); Column(Modifier.fillMaxSize()) { Top("Ramadan Dashboard"); Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Hero("Assalamu alaikum, ${state.userName}", "${state.district} • Ramadan day ${ramadanDay()} • ${today()}"); items.forEach { (title, screen) -> Card(Modifier.fillMaxWidth().clickable { nav(screen) }) { Text(title, Modifier.padding(18.dp), fontWeight = FontWeight.Bold) } } } } }
@Composable fun Hero(title:String, sub:String) = Card(colors=CardDefaults.cardColors(containerColor=Green), modifier=Modifier.fillMaxWidth()) { Column(Modifier.padding(18.dp)) { Text(title, color=Color.White, fontSize=22.sp, fontWeight=FontWeight.Bold); Text(sub, color=Color.White) } }
fun today() = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date())
fun ramadanDay(): Int = ((Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 49).floorMod(30)) + 1
fun Int.floorMod(m:Int)=((this % m)+m)%m

@Composable fun TodayScreen(state: AppState, back: () -> Unit) { val context = LocalContext.current; val d = DistrictData.districts.first { it.name == state.district }; val times = remember(state.district) { PrayerTimeHelper().getTodayPrayerTimes(d.latitude, d.longitude) }; LaunchedEffect(times) { PrayerTimeStorage.saveTodayTimes(context, times.fajrDate.time, times.maghribDate.time); WorkScheduler.scheduleIftarReminder(context, times.maghribDate); WorkScheduler.scheduleHydrationReminder(context) }; Column(Modifier.fillMaxSize()) { Top("Today", back); Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) { Hero("Ramadan ${ramadanDay()}", fastingStatus(times)); listOf("Suhoor/Fajr" to fmt(times.fajrDate), "Iftar/Maghrib" to fmt(times.maghribDate), "Dhuhr" to "12:10", "Asr" to "16:25", "Isha" to "19:45", "Quran reminder" to "Read ${state.quranGoal} ayahs", "Verse" to "Indeed, with hardship comes ease.", "Reward points" to "${ramadanDay()*10}", "Daily ibadah progress" to "Update tracker").forEach { Info(it.first, it.second) } } } }
fun fmt(d: Date)=SimpleDateFormat("hh:mm a", Locale.getDefault()).format(d)
fun fastingStatus(t: PrayerTimesResult): String { val now=System.currentTimeMillis(); return when { now < t.fajrDate.time -> "Fast starts in ${((t.fajrDate.time-now)/60000)} minutes"; now < t.maghribDate.time -> "Fasting now • Iftar in ${((t.maghribDate.time-now)/60000)} minutes"; else -> "Fast completed today" } }
@Composable fun Info(k:String,v:String)=Card(Modifier.fillMaxWidth()){ Row(Modifier.padding(14.dp), Arrangement.SpaceBetween){ Text(k,fontWeight=FontWeight.Bold); Text(v) } }

@Composable fun QuranScreen(state: AppState, prefs: AppPrefs, update: ((AppState)->Unit)->Unit, back:()->Unit) { val surahs = listOf("Al-Fatihah" to 7,"Al-Baqarah" to 286,"Ali 'Imran" to 200,"An-Nisa" to 176,"Al-Ma'idah" to 120,"Al-An'am" to 165,"Al-A'raf" to 206); val bookmarks = remember { mutableStateOf(prefs.getSet("bookmarks")) }; Column(Modifier.fillMaxSize()){ Top("Quran", back); LazyColumn(Modifier.padding(16.dp)){ item{ Hero("Continue reading", "Surah ${state.lastSurah}, Ayah ${state.lastAyah} • Goal ${state.quranGoal} ayahs/day"); Text("Dataset-ready: add a full Quran dataset under app/src/main/assets/quran for production text.", Modifier.padding(8.dp))}; items(surahs.withIndex().toList()){ (i,s) -> Card(Modifier.fillMaxWidth().padding(vertical=4.dp).clickable { update { it.lastSurah=i+1; it.lastAyah=1 } }){ Row(Modifier.padding(14.dp), Arrangement.SpaceBetween){ Text("${i+1}. ${s.first} (${s.second})"); Text(if (bookmarks.value.contains(s.first)) "★" else "☆", Modifier.clickable { val set=bookmarks.value.toMutableSet(); if(!set.add(s.first)) set.remove(s.first); bookmarks.value=set; prefs.saveSet("bookmarks", set) }) } } } } } }
@Composable fun IbadahScreen(prefs: AppPrefs, back:()->Unit) { val acts=listOf("Salah","Quran reading","Dhikr","Dua","Good deeds","Zakat"); val key="ibadah_${today()}"; val done= remember { mutableStateMapOf<String,Boolean>().apply { val set=prefs.getSet(key); acts.forEach{ put(it,set.contains(it)) } } }; Column(Modifier.fillMaxSize()){ Top("Ibadah Tracker", back); Column(Modifier.padding(16.dp)){ val count=done.values.count{it}; LinearProgressIndicator(progress = { count / acts.size.toFloat() }, modifier = Modifier.fillMaxWidth()); Text("Daily completion $count/${acts.size} • Current streak ${prefs.getString("streak","1")}"); acts.forEach{ Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment=Alignment.CenterVertically){ Checkbox(done[it]==true,{v-> done[it]=v; prefs.saveSet(key, done.filter{it.value}.keys) }); Text(it) } }; Text("Weekly statistics: ${count*7} activity points") } } }
@Composable fun DuaScreen(prefs: AppPrefs, back:()->Unit) { val duas=listOf(DuaData.sehriDua, DuaData.iftarDua, DuaItem("Forgiveness","رَبِّ اغْفِرْ لِي","My Lord, forgive me"), DuaItem("Guidance","اهدِنَا الصِّرَاطَ المُستَقِيمَ","Guide us to the straight path")); val fav= remember { mutableStateOf(prefs.getSet("duaBookmarks")) }; Column(Modifier.fillMaxSize()){ Top("Daily Dua", back); LazyColumn(Modifier.padding(16.dp)){ items(duas){ dua -> Card(Modifier.fillMaxWidth().padding(vertical=6.dp)){ Column(Modifier.padding(14.dp)){ Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween){ Text(dua.title,fontWeight=FontWeight.Bold); Text(if(fav.value.contains(dua.title))"★" else "☆", Modifier.clickable{ val s=fav.value.toMutableSet(); if(!s.add(dua.title)) s.remove(dua.title); fav.value=s; prefs.saveSet("duaBookmarks",s) })}; Text(dua.arabic, fontSize=22.sp); Text(dua.meaning) } } } } } }
@Composable fun ZakatScreen(prefs: AppPrefs, back:()->Unit) { var values by remember { mutableStateOf(List(6){""}) }; val cats=listOf("Cash savings","Gold","Silver","Business assets","Agricultural produce","Livestock"); val total=values.sumOf{it.toDoubleOrNull() ?: 0.0}; val nisab=5000.0; val zakat=if(total>=nisab) total*0.025 else 0.0; Column(Modifier.fillMaxSize()){ Top("Zakat Calculator", back); Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp)){ cats.forEachIndexed{ i,c -> OutlinedTextField(values[i], { v -> values=values.toMutableList().also{it[i]=v} }, label={Text(c)}, modifier=Modifier.fillMaxWidth())}; Text("Configurable nisab: $nisab BDT equivalent. Standard rate applied where applicable: 2.5%; consult a scholar for category-specific methods."); Hero("Payable Zakat", "%.2f".format(zakat)); Button({ prefs.saveString("zakatHistory", prefs.getString("zakatHistory") + "\n${today()}: %.2f".format(zakat)) }){Text("Save calculation")}; Text("History:${prefs.getString("zakatHistory")}") } } }
@Composable fun JourneyScreen(prefs: AppPrefs, back:()->Unit) { Column(Modifier.fillMaxSize()){ Top("Ramadan Journey", back); Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(10.dp)){ Hero("Overall Progress", "${(ramadanDay()/30f*100).roundToInt()}% of Ramadan"); Text("Worship streak: ${prefs.getString("streak","1")} days"); Text("Achievements: First Fast, Quran Starter, Dua Collector"); repeat(30){ day -> FilterChip(selected=day < ramadanDay(), onClick={}, label={Text("${day+1}")}, modifier=Modifier.padding(2.dp)) }; Text("Daily summary and weekly statistics are based on saved Ibadah records.") } } }
@Composable fun ProfileScreen(state: AppState, settings:()->Unit, logout:()->Unit, back:()->Unit) { Column(Modifier.fillMaxSize()){ Top("Profile", back); Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){ Hero(state.userName, "District ${state.district} • Language ${state.language}"); Text("Worship statistics, achievements, notification preferences and account options"); Button(settings){Text("Settings")}; Button(logout){Text("Logout")} } } }
@Composable fun SettingsScreen(state: AppState, update: ((AppState)->Unit)->Unit, back:()->Unit) { Column(Modifier.fillMaxSize()){ Top("Settings", back); Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){ Text("Language"); Row{ Button({update{it.language="en"}}){Text("English")}; Spacer(Modifier.width(8.dp)); Button({update{it.language="bn"}}){Text("বাংলা")}}; Text("Theme"); Row{ listOf("light","dark","system").forEach{ mode -> Button({update{it.themeMode=mode}}, Modifier.padding(3.dp)){Text(mode)} } }; Row(verticalAlignment=Alignment.CenterVertically){ Text("Notifications"); Switch(state.notificationsEnabled,{v->update{it.notificationsEnabled=v}}) }; Text("Account settings are stored locally for demo authentication.") } } }
@Composable fun CalendarForState(state: AppState, districtClick:()->Unit) { val d=DistrictData.districts.first{it.name==state.district}; val start=Calendar.getInstance().apply { set(2026, Calendar.FEBRUARY, 18) }; RamadanCalendarScreen(generateRamadanCalendar(PrayerTimeHelper(), d.latitude, d.longitude, start), state.district, districtClick) }
