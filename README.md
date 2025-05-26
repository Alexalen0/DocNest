# DocNest

An Android application that performs Optical Character Recognition (OCR) on images captured through the camera or selected from the gallery. The app allows users to scan, save, and manage text from images.

## Features

- Camera integration for capturing images
- Gallery access for selecting existing images
- Real-time OCR text extraction
- Local storage of scanned results
- View history of previous scans
- Material Design UI
- Dark theme support

## Technologies Used

- **Kotlin** - Primary programming language
- **Android Jetpack Components**
    - Room Database for local storage
    - ViewBinding for view interactions
    - Coroutines for asynchronous operations
- **MLKit Text Recognition** - For OCR functionality
- **AndroidX Libraries**
    - Core KTX
    - AppCompat
    - Material Design Components
    - ConstraintLayout
- **Build Tools**
    - Gradle with Version Catalogs
    - Kotlin KSP for annotation processing

## Requirements

- Android Studio Arctic Fox or newer
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 35
- Kotlin 1.9.22 or newer

## Setup

1. Clone the repository:
```bash
git clone https://github.com/Alexalen0/Ocr_cam.git
```

2. Open the project in Android Studio.

3. Sync the project with Gradle files.

4. Run the app on an emulator or physical device.

## Building

To build the project, run:
```bash
./gradlew build
```

## Contributing

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
