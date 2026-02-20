# FocusFlow

A gamified Pomodoro app built with Kotlin 2.0 and Jetpack Compose.

## Features

- **Pomodoro timer** – 25-minute focus sessions with a custom circular progress UI
- **Foreground service** – Timer continues with a notification when the app is in the background
- **Penalty logic** – Leaving the app while the timer is running records a "Withered" tree and resets the coin multiplier
- **Tasks** – Create and track tasks with estimated pomodoros
- **Forest** – View Healthy (completed sessions) and Withered trees
- **Coins & multiplier** – Earn coins per minute; multiplier resets on app leave during a session

## Architecture

- **MVVM + Clean Architecture**
- **Room** 2.6+ for persistence (User, Task, Tree entities)
- **Hilt** for dependency injection (KSP)
- **Jetpack Navigation** with a `@Serializable` sealed class for type-safe routes
- **LifecycleService** for the timer with `StateFlow` for remaining time

## Setup & building (no Android Studio required)

### Option A: Build online with GitHub (recommended)

1. Create a new repository on [GitHub](https://github.com/new).
2. Push this project (e.g. `git init`, `git add .`, `git commit -m "FocusFlow"`, `git remote add origin <your-repo-url>`, `git push -u origin main`).
3. Open the repo → **Actions** tab. The workflow will run on push (or trigger **Run workflow** manually).
4. When the run finishes, open the run → **Artifacts** and download **focusflow-debug-apk** (contains `app-debug.apk`).
5. Copy the APK to your Android phone and install (enable “Install from unknown sources” for your file manager if asked).

### Option B: Build on your PC (command line)

You do **not** need Android Studio. You need:

- **JDK 17** – [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/).
- **Android SDK command-line tools** – [Download](https://developer.android.com/studio#command-tools), unzip, then run:
  ```bash
  bin/sdkmanager "platform-tools" "build-tools;35.0.0" "platforms;android-35"
  ```
- **Gradle wrapper** (one-time): if the project has no `gradlew`, install [Gradle](https://gradle.org/install/) and run in the project folder: `gradle wrapper --gradle-version 8.9`.

Then in the project folder:

- **Windows:** `gradlew.bat assembleDebug`
- **macOS/Linux:** `./gradlew assembleDebug`

The APK is at: `app\build\outputs\apk\debug\app-debug.apk`. Copy it to your phone and install.

### Option C: Use Android Studio

Open the project in Android Studio, sync Gradle, then **Run** on a device or emulator (minSdk 26).

## Theme

- **SageGreen** `#8DAA91`
- **ForestGreen** `#2D5A27`
- **WoodBrown** `#795548`
- **SoftBeige** `#F5F5DC`
