 # 🌙 Ramadan App

> A Bangla-first Islamic and Ramadan Companion App designed to help users manage their daily Ramadan activities, prayer schedule, Quran reading, Islamic learning, Ibadah tracking, and Ramadan progress from a single application.

---

## 📱 Overview

**Ramadan App** is a user-friendly Islamic mobile application focused on providing practical and reliable Ramadan-related information and daily worship tools.

The application is primarily designed for **Bangla-speaking users**. **Bangla is the default language**, while users can optionally switch to **English** from the Settings screen.

The app provides Ramadan timings, prayer schedules, Sehri and Iftar reminders, Quran reading, Hadith, Daily Dua, Namaz learning, Hajj & Umrah information, Qurbani information, Zakat calculation, Ibadah tracking, and Ramadan progress monitoring.

The application does **not require Login or Sign Up**, keeping the user experience simple and accessible.

---

## 🎯 Project Vision

The goal of Ramadan App is to provide a single, simple, and practical digital companion for users during Ramadan and their daily Islamic activities.

Instead of requiring users to use multiple applications for prayer times, Quran, Dua, Hadith, Ramadan timings, and Ibadah tracking, the application brings these core features together in one place.

### Core Vision

> **Make daily Islamic activities easier, more organized, and accessible through a simple digital companion.**

---

# ✨ Key Features

## 🌙 1. Ramadan Dashboard

The Home Dashboard provides an overview of the user's daily Ramadan information.

### Includes

- Current date
- Hijri date
- Bangla date
- Ramadan day
- Current Islamic time
- Sehri time
- Iftar time
- Next prayer
- Prayer countdown
- Daily Ibadah progress
- Quran reminder
- Important daily information

### Example

```text
Today's Ramadan Information

Sehri
05:00 AM

Iftar
06:15 PM

Next Prayer
Maghrib

Remaining
00:25:40
```

---

# 🌙 2. Sehri & Iftar Timing

The application provides daily **Sehri and Iftar timings** based on the user's selected district or location.

### Features

- Daily Sehri ending time
- Daily Iftar time
- Current Ramadan day
- Remaining time
- Automatic daily timing update

### Timing Flow

```text
User District
      ↓
Retrieve Daily Timing
      ↓
Display Sehri & Iftar
      ↓
Schedule Notifications
```

---

# 🔔 3. Sehri & Iftar Notifications

One of the core features of the application is the ability to send scheduled **Sehri and Iftar notifications** directly to the user's phone.

### Sehri Reminder

The application can notify the user before Sehri ends.

Example:

```text
🌙 Sehri Reminder

Sehri time will end in 15 minutes.

Sehri Ends: 05:00 AM
```

### Iftar Notification

At Iftar time, the application can notify the user.

Example:

```text
🌅 Iftar Time

It is time for Iftar.

Iftar: 06:15 PM
```

### User Controls

Users can configure:

- Sehri notification ON/OFF
- Iftar notification ON/OFF
- Prayer notification ON/OFF
- Quran reminder ON/OFF
- Dua reminder ON/OFF
- Notification sound
- Sehri reminder lead time

### Notification Flow

```text
District / Location
        ↓
Get Sehri & Iftar Time
        ↓
Calculate Notification Time
        ↓
Schedule Local Notification
        ↓
User's Phone
        ↓
🔔 Notification
```

> Notification permission will be requested when required by the operating system.

---

# 🕌 4. Prayer Times

The application provides daily Islamic prayer timings.

### Main Prayers

- Fajr
- Dhuhr
- Asr
- Maghrib
- Isha

### Additional Islamic Times

- Tahajjud
- Ishraq
- Chasht
- Sunrise
- Sunset
- Prohibited prayer times

### Features

- Today's prayer schedule
- Current prayer
- Next prayer
- Countdown to next prayer
- Prayer reminders
- Location/district-based timing

### Example

```text
Prayer Times

Fajr       05:00 AM
Dhuhr      12:15 PM
Asr        04:30 PM
Maghrib    06:15 PM
Isha       07:45 PM
```

---

# ⏱️ 5. Prayer & Islamic Time Countdown

The Home screen can display the current Islamic time and countdown to the next important prayer or Islamic time period.

### Example

```text
Current Time

Chasht

08:55 AM - 11:41 AM

Next Period

Prohibited Time

Remaining
00:03:49
```

This allows users to quickly understand:

- What Islamic time is currently active
- What comes next
- How much time remains

---

# 📖 6. Al Quran

The Quran section provides a convenient reading experience.

### Features

- Complete Quran
- Surah list
- Para list
- Arabic Quran text
- Bangla translation
- English translation
- Search
- Bookmark
- Last reading position
- Reading progress

### Reading Flow

```text
Quran
  ↓
Surah / Para List
  ↓
Select
  ↓
Read
  ↓
Bookmark / Save Position
  ↓
Continue Later
```

The application can save the user's last reading position locally so that the user can continue reading later.

---

# 📚 7. Hadith

The Hadith section provides Islamic teachings and references in an accessible format.

### Features

- Hadith collection
- Hadith categories
- Arabic text
- Bangla translation
- English translation
- Search
- Bookmark/Favorite
- Daily Hadith

### Example Categories

- Ramadan
- Fasting
- Salah
- Dua
- Good Deeds
- Character
- Charity
- Faith

> Hadith content should be sourced from reliable and properly referenced collections.

---

# 🤲 8. Daily Dua

The Daily Dua section provides commonly used Islamic supplications.

### Features

- Daily Dua
- Arabic text
- Bangla meaning
- English meaning
- Transliteration
- Dua categories
- Bookmark/Favorite

### Categories

- Morning & Evening
- Before Sleep
- Food
- Salah
- Ramadan
- Forgiveness
- Travel
- Difficult Times
- General Supplications

### Dua Flow

```text
Daily Dua
    ↓
Select Category
    ↓
Select Dua
    ↓
Read Arabic
    ↓
Read Translation
    ↓
Bookmark
```

---

# 🧎 9. Namaz Shikkha

A dedicated learning section for users who want to learn the basic rules and steps of Salah.

### Features

- Wudu instructions
- Salah intention
- Salah steps
- Required Surahs
- Required Duas
- Men's Salah guidelines
- Women's Salah guidelines

### Learning Flow

```text
Namaz Shikkha
      ↓
Select Topic
      ↓
Read Instructions
      ↓
Follow Step-by-Step Guide
```

---

# 🕋 10. Hajj & Umrah

The application provides educational information about Hajj and Umrah.

### Features

- Basic Hajj guide
- Basic Umrah guide
- Important steps
- Required Duas
- Important instructions
- Checklist

This module is intended as an educational and reference feature.

---

# 🌙 11. Qurbani

The application provides basic Islamic information related to Qurbani.

### Features

- Qurbani rules
- Qurbani timing
- Intention
- Relevant Duas
- Basic guidelines regarding animals
- Basic meat distribution guidelines

---

# 📊 12. Ibadah Tracker

The Ibadah Tracker helps users monitor their daily worship activities.

### Trackable Activities

- Salah
- Quran
- Dua
- Dhikr
- Good Deeds
- Sadaqah

### Example

```text
Today's Ibadah

Fajr          ✓
Dhuhr         ✓
Asr           ✓
Maghrib       ✓
Isha          ✓

Quran         ✓
Dua           ✓
Dhikr         ○

Daily Progress
██████████░░ 80%
```

The tracker is intended to encourage consistency and help users understand their daily worship habits.

---

# 💰 13. Zakat Calculator

The Zakat Calculator helps users estimate Zakat based on applicable assets.

### Supported Asset Categories

- Cash savings
- Gold
- Silver
- Business assets
- Other applicable assets

### Calculation Flow

```text
Enter Assets
     ↓
Calculate Total
     ↓
Check Nisab
     ↓
Apply Applicable Rate
     ↓
Calculate Zakat
     ↓
Display Result
```

The application design uses **2.5% as the standard rate for applicable Zakat assets**.

> The calculator is intended as a calculation aid. Users should consult a qualified scholar for personal or complex Zakat decisions.

---

# 🏆 14. Ramadan Journey

Ramadan Journey provides an overview of the user's progress throughout Ramadan.

### Features

- Daily Ibadah progress
- Weekly progress
- Worship streak
- Ramadan calendar
- Completed activities
- Achievement/badge system

### Example

```text
Ramadan Journey

Overall Progress
████████░░ 80%

Worship Streak
7 Days

This Week
██████████ 90%
```

---

# 📅 15. Islamic Calendar

The application provides Islamic and standard calendar information.

### Information

- Hijri date
- Bangla date
- English/Gregorian date
- Ramadan day
- Important Islamic dates

---

# 📍 16. District Selection

The application uses the user's selected district to provide appropriate prayer, Sehri, and Iftar timings.

### Options

- Search district
- Select from 64 districts of Bangladesh
- Automatic location detection

### Flow

```text
Select District
      │
      ├── Search
      │
      ├── Select from 64 Districts
      │
      └── Detect Current Location
```

Changing the district should update:

- Sehri time
- Iftar time
- Prayer times
- Related daily timing information

---

# 🌐 17. Language System

The application follows a **Bangla-first approach**.

## Default Language

🇧🇩 **Bangla**

Bangla will be automatically selected when the application is used for the first time.

Users do not need to select a language during the initial setup.

## Optional Language

🇬🇧 **English**

Users can switch to English from:

```text
Settings
   ↓
Language
   ↓
English
```

### Language Flow

```text
First Launch
     ↓
Default = Bangla
     ↓
Use Application
     ↓
Settings
     ↓
Language
     ↓
English (Optional)
```

The selected language preference should be saved locally.

---

# 🔔 18. Notification System

The application provides useful Islamic reminders.

### Notification Types

| Notification | Purpose |
|---|---|
| 🌙 Sehri Reminder | Reminds the user before Sehri ends |
| 🌅 Iftar Notification | Notifies the user at Iftar time |
| 🕌 Prayer Reminder | Reminds the user about prayer |
| 📖 Quran Reminder | Encourages Quran reading |
| 🤲 Dua Reminder | Provides a daily Dua reminder |

### Notification Settings

Users can control:

- Notification enable/disable
- Sehri reminder
- Iftar notification
- Prayer reminders
- Quran reminder
- Dua reminder
- Notification sound
- Reminder timing

---

# 👤 19. Profile

The application does **not require Login or Sign Up**.

Therefore, the Profile section is designed for local preferences and personal progress rather than account management.

### Profile Includes

- Selected district
- Language
- Ibadah statistics
- Worship streak
- Notification preferences
- Application preferences

---

# ⚙️ 20. Settings

Users can customize the application from Settings.

### Settings

```text
Settings
│
├── Language
│   ├── বাংলা ✓
│   └── English
│
├── District
│
├── Sehri Notification
│
├── Iftar Notification
│
├── Prayer Notification
│
├── Quran Reminder
│
├── Dua Reminder
│
├── Notification Sound
│
└── Theme
```

---

# 🧭 Application Navigation

```text
                         ┌──────────────┐
                         │    Splash    │
                         └──────┬───────┘
                                ↓
                         ┌──────────────┐
                         │  Onboarding  │
                         └──────┬───────┘
                                ↓
                    ┌──────────────────────┐
                    │  District Selection  │
                    └──────────┬───────────┘
                               ↓
                         ┌──────────────┐
                         │     Home     │
                         └──────┬───────┘
                                │
          ┌─────────────┬───────┼───────┬─────────────┐
          ↓             ↓       ↓       ↓             ↓
        Today         Quran    Deen   Ibadah       Profile
          │             │       │       │             │
          ↓             ↓       ↓       ↓             ↓
   Ramadan Info      Reading  Hadith  Tracking     Settings
   Sehri/Iftar               Prayer
   Countdown                 Dua
          │                  Namaz
          │                  Shikkha
          │                  Hajj/Umrah
          │                  Qurbani
          │
          ├── Sehri Notification
          ├── Iftar Notification
          └── Prayer Reminder
```

---

# 🗂️ Application Modules

```text
Ramadan App
│
├── Splash
├── Onboarding
├── District Selection
├── Home Dashboard
│   ├── Today
│   ├── Quran
│   ├── Deen
│   ├── Ibadah Tracker
│   ├── Daily Dua
│   ├── Zakat Calculator
│   └── Ramadan Journey
│
├── Deen
│   ├── Al Quran
│   ├── Hadith
│   ├── Prayer Time
│   ├── Namaz Shikkha
│   ├── Daily Dua
│   ├── Hajj & Umrah
│   └── Qurbani
│
├── Notifications
│   ├── Sehri
│   ├── Iftar
│   ├── Prayer
│   ├── Quran
│   └── Dua
│
└── Profile
    └── Settings
```

---

# 🔄 First-Time User Flow

```text
1. Open Application
        ↓
2. Splash Screen
        ↓
3. Onboarding
        ↓
4. District Selection
        ↓
5. Home Dashboard
        ↓
6. Default Language = Bangla
        ↓
7. User can use the application
        ↓
8. Notification permission when required
        ↓
9. Sehri & Iftar notifications are scheduled
```

---

# 👤 Returning User Flow

```text
Open Application
      ↓
Splash
      ↓
Check Saved Preferences
      ↓
Load District
      ↓
Load Language
      ↓
Load Notification Settings
      ↓
Home Dashboard
```

The application should remember local preferences such as:

- Selected district
- Language
- Notification preferences
- Quran reading position
- Bookmarks
- Ibadah records
- User settings

---

# 🗃️ Local Data

Since the application does not require Login or Sign Up, important user preferences and progress can be stored locally.

### Possible Local Data

```text
App Preferences
├── language
├── district
├── notification settings
├── theme
└── first launch status

Quran
├── last reading position
├── bookmarks
└── reading progress

Ibadah
├── daily Salah
├── Quran
├── Dua
├── Dhikr
├── Good Deeds
└── Sadaqah

Ramadan
├── daily progress
├── streak
└── achievements
```

---

# 🔐 Privacy & Permissions

The application should request only the permissions required for its functionality.

## Location

Used only when the user chooses automatic location detection.

```text
Location → Detect District
```

Users should also be able to manually select a district without granting location permission.

## Notifications

Required for:

- Sehri reminder
- Iftar notification
- Prayer reminder
- Quran reminder
- Dua reminder

The application should clearly explain why notification permission is required.

---

# ⚡ Functional Requirements

The application should:

1. Launch with Bangla as the default language.
2. Allow users to switch to English.
3. Allow users to select their district.
4. Support all 64 districts of Bangladesh.
5. Provide daily Sehri timing.
6. Provide daily Iftar timing.
7. Provide daily prayer timings.
8. Show countdown to the next prayer/time.
9. Schedule Sehri notifications.
10. Schedule Iftar notifications.
11. Allow notification settings to be customized.
12. Provide Quran reading.
13. Save the last Quran reading position.
14. Provide Hadith content.
15. Provide Daily Dua.
16. Provide Namaz learning content.
17. Provide Hajj & Umrah information.
18. Provide Qurbani information.
19. Provide Zakat calculation.
20. Track daily Ibadah.
21. Calculate Ramadan progress.
22. Maintain worship streaks.
23. Provide Islamic calendar information.
24. Store necessary preferences locally.
25. Work without requiring user authentication.

---

# 🛡️ Non-Functional Requirements

## Usability

- Simple and clean UI
- Bangla-first interface
- Easy navigation
- One-hand friendly design
- Clear typography
- Easy-to-understand Islamic information

## Performance

- Fast application startup
- Smooth navigation
- Efficient local data storage
- Minimal unnecessary network requests

## Reliability

- Correct daily timing calculation
- Reliable notification scheduling
- Data persistence for user progress
- Graceful handling of unavailable network/data

## Accessibility

- Readable font sizes
- Clear icons
- Sufficient contrast
- Simple navigation
- Bengali text support

## Privacy

- No mandatory account
- Minimal permissions
- Location only when required
- Local storage for personal progress where appropriate

---

# ⚠️ Data Accuracy Considerations

Prayer, Sehri, and Iftar timings are time-sensitive and location-dependent.

Therefore, the implementation should use a **reliable prayer-time calculation method or trusted timing source**.

The application should:

- Use the selected district/location
- Handle timezone correctly
- Account for date changes
- Update daily timings
- Handle Ramadan date correctly
- Schedule notifications according to local time

Islamic educational content such as Quran, Hadith, Dua, Hajj, Umrah, Namaz, and Zakat should be sourced from reliable and properly referenced sources.

---

# ❌ Out of Scope

The following features are intentionally excluded from the current version:

- Login
- Sign Up
- User authentication
- Password management
- Social login
- Khatme Quran module
- Nearby Mosque finder
- Social networking
- Online donation/payment
- User-to-user messaging

These may be considered for future versions if required.

---

# 🚀 Future Scope

Possible future improvements include:

- More detailed Islamic learning courses
- Audio Quran
- Quran recitation
- Tajweed learning
- More personalized reminders
- Home screen widgets
- Offline-first content
- Cloud backup
- Optional user account
- Cross-device synchronization
- Additional Islamic educational content
- Advanced Ramadan analytics

---

# 📊 Feature Summary

| Module | Features |
|---|---|
| 🚀 Splash | Application initialization |
| 👋 Onboarding | Application introduction |
| 🌐 Language | **Bangla default, English optional** |
| 📍 District | Search, 64 districts, location detection |
| 🏠 Home | Daily Ramadan overview |
| 🌙 Sehri | Timing + notification |
| 🌅 Iftar | Timing + notification |
| 🕌 Prayer | Daily prayer schedule |
| ⏱️ Countdown | Next prayer / Islamic time |
| 📖 Quran | Reading, search, bookmark, progress |
| 📚 Hadith | Reading, categories, bookmark |
| 🤲 Dua | Daily Dua, categories, bookmark |
| 🧎 Namaz Shikkha | Step-by-step learning |
| 🕋 Hajj & Umrah | Educational guide |
| 🌙 Qurbani | Islamic information |
| 📊 Ibadah | Daily worship tracking |
| 💰 Zakat | Zakat calculation |
| 🏆 Ramadan Journey | Progress, streak, achievements |
| 📅 Islamic Calendar | Hijri, Bangla & Gregorian dates |
| 🔔 Notifications | Sehri, Iftar, Prayer, Quran, Dua |
| 👤 Profile | Preferences & statistics |
| ⚙️ Settings | Language, notifications, district, theme |

---

# 🛠️ Technology

> The implementation technology will be defined separately during development.

The current project specification intentionally does not lock the application to a particular programming language, framework, database, or backend technology.

The application can be implemented using an appropriate modern mobile development stack while maintaining the functionality and architecture described in this document.

---

# 📂 Recommended Project Structure

A modular structure can be used during implementation:

```text
RamadanApp/
│
├── app/
│
├── core/
│   ├── navigation/
│   ├── database/
│   ├── network/
│   ├── notifications/
│   ├── location/
│   └── utilities/
│
├── features/
│   ├── onboarding/
│   ├── home/
│   ├── today/
│   ├── quran/
│   ├── hadith/
│   ├── dua/
│   ├── prayer/
│   ├── namaz/
│   ├── hajj_umrah/
│   ├── qurbani/
│   ├── zakat/
│   ├── ibadah/
│   ├── ramadan_journey/
│   ├── profile/
│   └── settings/
│
├── data/
│   ├── models/
│   ├── repositories/
│   └── sources/
│
└── resources/
    ├── quran/
    ├── hadith/
    ├── dua/
    └── islamic_content/
```

---

# 📌 Development Priorities

## Phase 1 — Core Setup

- Splash
- Onboarding
- Default Bangla language
- District selection
- Home Dashboard

## Phase 2 — Ramadan Timing

- Sehri timing
- Iftar timing
- Prayer timings
- Countdown
- Timing update

## Phase 3 — Notification

- Notification permission
- Sehri reminder
- Iftar notification
- Prayer reminder
- Notification settings

## Phase 4 — Islamic Content

- Quran
- Hadith
- Daily Dua
- Namaz Shikkha
- Hajj & Umrah
- Qurbani

## Phase 5 — User Tools

- Ibadah Tracker
- Zakat Calculator
- Ramadan Journey
- Islamic Calendar

## Phase 6 — Settings & Polish

- English language
- Theme
- Profile
- Local data persistence
- Error handling
- Performance optimization
- UI improvements

---

# 🎨 UI/UX Principles

The application should follow a clean, calm, and Islamic visual style.

### Design Principles

- Minimal and uncluttered interface
- Clear information hierarchy
- Large and readable Bengali typography
- Soft Islamic visual elements
- Easy one-hand navigation
- Consistent icons
- Consistent spacing
- Clear cards for timing information
- Clear notification settings
- Accessible color contrast

### Primary User Experience

The user should be able to answer these questions immediately after opening the application:

> **What time is it?**  
> **When is the next prayer?**  
> **When is Sehri?**  
> **When is Iftar?**  
> **How is my Ibadah progress today?**

---

# 📊 Success Criteria

The application will be considered successful when a user can:

- Open the application without creating an account
- Use Bangla immediately
- Select their district
- View accurate daily prayer times
- View Sehri and Iftar timings
- Receive scheduled Sehri and Iftar notifications
- Read Quran
- Read Hadith
- Read Daily Dua
- Learn basic Namaz
- Access Hajj & Umrah information
- Access Qurbani information
- Calculate Zakat
- Track daily Ibadah
- View Ramadan progress
- Change the language to English
- Customize notification preferences

---

# 👨‍🎓 Student Information

**Name:** Md. Rakibul Islam  
**Roll No:** 2134  
**Department:** Computer Science and Engineering  
**Institution:** Jahangirnagar University

### Instructor

**Name:** Masum Bhuiyan  
**Designation:** Lecturer  
**Institution:** Jahangirnagar University

---

# 📌 Project Status

**Status:** 🚧 In Development

The current document defines the functional scope, navigation, features, user experience, data requirements, notification behavior, and development direction of the Ramadan App.

Implementation technologies may be finalized during the development phase.

---

# 🌙 Final Description

**Ramadan App** is a Bangla-first Islamic and Ramadan Companion designed to make daily Ramadan activities easier and more organized.

With features such as **Sehri & Iftar timing, automatic Sehri/Iftar notifications, prayer schedules, Quran, Hadith, Daily Dua, Namaz Shikkha, Hajj & Umrah, Qurbani information, Zakat Calculator, Ibadah Tracker, Ramadan Journey, and Islamic Calendar**, the application brings essential Islamic tools together in one simple platform.

**Bangla is the default language, while English is available as an optional language from Settings. The application does not require Login or Sign Up, allowing users to access the core features quickly and easily.**

> ### 🌙 “Your simple digital companion for a more organized Ramadan and daily Islamic life.”
