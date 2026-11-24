# Service Simulated Downloader

Android project for the "Service-Based Simulated Downloader" assignment.

## Features
- Bound Service for background simulated download
- Progress updates via Listener + Handler
- Cancel support
- Clean Material-style UI with CardView

## How to Open & Run in Android Studio

1. Unzip `ServiceDownloader.zip`
2. Open Android Studio
3. Choose **File → Open** and select the `ServiceDownloader` folder
4. Wait for Gradle Sync to finish (Android Studio will download the Gradle wrapper if needed)
5. If prompted about missing SDK, install the recommended SDK
6. Click the green **Run** button (or press Shift+F10)
7. Choose an emulator or connected phone

## Project Structure
```
app/src/main/
├── java/com/example/servicedownloader/
│   ├── MainActivity.java
│   └── DownloadService.java
├── res/layout/activity_main.xml
└── AndroidManifest.xml
```

## Notes
- Minimum SDK 24
- Uses AndroidX + Material Components + CardView
- White background UI suitable for screenshots
