# SMS Monitor App (Content Provider Experiment)

This Android app demonstrates the use of **ContentResolver** and **ContentObserver** to read and monitor SMS messages.

## Features

- Read all SMS from the inbox using `ContentResolver`
- Automatically detect new SMS using `ContentObserver`
- Beautiful Material Design UI with cards
- Refresh button for manual reload
- Proper runtime permission handling
- Shows sender, message body, and formatted date

## How to Run

1. Open the project in Android Studio
2. Sync Gradle
3. Start an emulator (API 24+)
4. Run the app
5. Grant **SMS** permission when asked
6. To test:
   - Open the emulator’s **Messages** app (or use Extended Controls → Phone → SMS)
   - Send a test SMS to the emulator
   - Click **Refresh SMS List** or just wait — new SMS will appear automatically

## Project Structure

```
com.example.w09_sms_monitor
├── MainActivity.java      → Main logic + ContentResolver + ContentObserver
├── MySMS.java             → Data model
├── activity_main.xml      → Main UI
└── list_item.xml          → SMS card layout
```

## Experiment Requirements Covered

1. ✅ Read SMS via ContentResolver
2. ✅ Display in ListView
3. ✅ Monitor new SMS with ContentObserver
4. ✅ Auto-refresh when new SMS arrives
