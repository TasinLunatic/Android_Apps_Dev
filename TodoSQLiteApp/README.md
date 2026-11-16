# Todo SQLite App

A simple Android Todo List application using SQLite database.

## Features

- Add todo tasks via input field
- Display tasks in a ListView
- Delete individual tasks
- Data persisted in SQLite database (`todo.db`)

## Project Structure

```
TodoSQLiteApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/w07_sqllite/
│   │   │   ├── MainActivity.java
│   │   │   ├── Task.java
│   │   │   └── MyDatabaseOpener.java
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   └── task_item.xml
│   │   │   ├── drawable/
│   │   │   │   ├── add1.xml
│   │   │   │   └── delete.xml
│   │   │   └── values/
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

## How to Run in Android Studio

### Prerequisites
- Android Studio (Hedgehog | 2023.1.1 or newer recommended)
- JDK 17 or compatible
- Android SDK with API Level 34

### Steps

1. **Open the Project**
   - Launch Android Studio
   - Select **File → Open**
   - Navigate to the `TodoSQLiteApp` folder and select it
   - Click **OK**

2. **Sync Gradle**
   - Android Studio will automatically prompt to sync Gradle
   - Click **Sync Now** if prompted
   - Wait for the Gradle sync to complete (this may take a few minutes the first time)

3. **Create/Start an Emulator (or connect a physical device)**
   - Go to **Tools → Device Manager**
   - Click **Create Device** if you don't have one
   - Select a phone (e.g., Pixel 6) → Next
   - Download a system image (API 34 recommended) if needed → Finish
   - Click the **Play** button next to the device to start the emulator

4. **Run the App**
   - Click the green **Run** button (▶) in the toolbar
   - Or press `Shift + F10`
   - Select your emulator/device and click **OK**

5. **Using the App**
   - Type a task in the input field at the top
   - Click the green **+** (add) icon to save the task
   - Tasks will appear in the list below
   - Click the red **delete** icon next to any task to remove it
   - Data is automatically saved to SQLite and persists across app restarts

## Notes

- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34
- The database file is stored internally as `todo.db`
- Empty tasks are ignored when adding

## Package Name
`com.example.w07_sqllite`
