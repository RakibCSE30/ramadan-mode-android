# 🌙 Ramadan Mode — Android

A context-aware Android module that adapts app behavior to the Ramadan fasting schedule — providing accurate Sehri/Iftar timings, fasting-aware notifications, and Iftar nutrition suggestions tailored for Bangladeshi users.

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-purple?logo=kotlin)
![Platform](https://img.shields.io/badge/Platform-Android-green?logo=android)
![License](https://img.shields.io/badge/License-MIT-blue)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow)

---

## 📖 Overview

Most mainstream fitness and diet-tracking apps (MyFitnessPal, HealthifyMe, etc.) are not designed with Ramadan's fasting pattern in mind. They send meal reminders during fasting hours, miscalculate hydration goals, and lack any awareness of Sehri/Iftar timing — creating a disconnected experience for fasting users.

**Ramadan Mode** solves this by dynamically adjusting app behavior based on the daily fasting window (Fajr to Maghrib), determined using the user's location. When enabled, it recalculates Sehri and Iftar times daily and shifts notification logic, hydration reminders, and nutrition suggestions accordingly.

> This module focuses purely on scheduling and lifestyle adaptation. It does not provide religious rulings, fatwa verification, or any form of religious guidance.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🕌 **Sehri & Iftar Timing** | Location-based daily prayer time calculation (Fajr & Maghrib) |
| 🔔 **Smart Notifications** | Alerts before Sehri ends and when Iftar begins |
| 💧 **Fasting-Aware Hydration** | Hydration reminders automatically pause during fasting hours |
| 🍽️ **Iftar Nutrition Suggestions** | Balanced Iftar meal ideas based on daily calorie budget |
| ⏱️ **Countdown Timer** | Live countdown to next Sehri/Iftar on the dashboard |
| 🌐 **Toggle On/Off** | Simple settings switch to enable or disable Ramadan Mode |

---

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Local Database:** Room
- **Background Scheduling:** WorkManager / AlarmManager
- **Networking:** Retrofit (Aladhan API for prayer times)
- **Architecture:** MVVM

---

## 📂 Project Structure

app/
 └── src/main/java/com/rakibcse30/ramadanmode/
      ├── data/          # Room entities, DAOs, repository
      ├── network/       # Retrofit API service (prayer times)
      ├── ui/            # Jetpack Compose screens
      ├── viewmodel/     # ViewModels for state management
      └── util/          # Prayer time calculation, notification scheduling

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest stable version)
- Minimum SDK: API 24 (Android 7.0)
- Kotlin 1.9+

### Installation

git clone
```bash
https://github.com/RakibCSE30/ramadan-mode-android.git

```
---

1. Open the project in Android Studio
2. Let Gradle sync complete
3. Run the app on an emulator or physical device

---

## 🗺️ Roadmap

- [x] Project setup & repository structure
- [ ] Prayer time API integration (Sehri/Iftar)
- [ ] Notification scheduling system
- [ ] Hydration reminder logic
- [ ] Iftar nutrition suggestion engine
- [ ] Countdown timer UI
- [ ] Offline prayer time calculation (astronomical formula, no internet dependency)
- [ ] Hijri calendar integration

---

## 🤝 Contributing

This is currently a solo academic/personal project. Suggestions and feedback are welcome via [Issues](https://github.com/RakibCSE30/ramadan-mode-android/issues).

---

## 📄 License

This project is licensed under the MIT License.

---

## 👤 Author

**Rakib**
GitHub: [@RakibCSE30](https://github.com/RakibCSE30)


