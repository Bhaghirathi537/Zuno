# 🎵 PulsePlay

A clean Android music player built for the **Android Development Club Recruitment Task**.

PulsePlay targets **Level 3** of the recruitment brief by combining:

- API-powered song discovery
- Remote artwork loading
- Real local MP3 playback
- Play/Pause controls
- Background playback
- Android system media controls
- Clean Kotlin + XML architecture

## ✨ Features

### Level 1
- Home screen with 5+ songs
- Song thumbnail, title and artist
- Clickable song cards
- Separate Now Playing screen
- Back navigation
- Play/Pause control

### Level 2
- Three bundled MP3 demo tracks in `app/src/main/res/raw/`
- Actual audio playback using Media3 ExoPlayer

### Level 3
- Public iTunes Search API for song metadata and artwork
- Retrofit + Gson for network access
- Coil for artwork loading
- Media3 `MediaSessionService` for background playback
- Foreground media playback notification
- Playback continues when the app is minimized or the screen is locked

## 🛠 Tech Stack

- Kotlin
- XML layouts
- RecyclerView
- ViewModel
- Repository pattern
- Retrofit 3
- Gson
- Coil 3
- AndroidX Media3 ExoPlayer
- MediaSessionService

## 🧠 Architecture

```text
MainActivity
    ↓
SongViewModel
    ↓
SongRepository
    ↓
Retrofit / iTunes Search API
    ↓
Song model
    ↓
RecyclerView
```

Playback is intentionally separated from the Activity:

```text
NowPlayingActivity
       ↓
MediaController
       ↓
MediaSession
       ↓
PlaybackService
       ↓
ExoPlayer
       ↓
Local MP3
```

This allows the player to continue while the UI is no longer visible.

## 🌐 API

PulsePlay uses the public **iTunes Search API** as a metadata source.

The app requests music results and uses:

- `trackName` → song title
- `artistName` → artist
- `artworkUrl100` → artwork

The app does not depend on the API for its core audio playback. Local bundled audio is used so the playback demonstration remains reliable.

## 📁 Project Structure

```text
PulsePlay/
├── app/
│   ├── src/main/
│   │   ├── java/com/pulseplay/app/
│   │   │   ├── data/
│   │   │   │   ├── ApiClient.kt
│   │   │   │   ├── ItunesApi.kt
│   │   │   │   └── SongRepository.kt
│   │   │   ├── model/
│   │   │   │   └── Song.kt
│   │   │   ├── playback/
│   │   │   │   └── PlaybackService.kt
│   │   │   └── ui/
│   │   │       ├── MainActivity.kt
│   │   │       ├── NowPlayingActivity.kt
│   │   │       ├── SongAdapter.kt
│   │   │       ├── SongViewModel.kt
│   │   │       └── SongViewModelFactory.kt
│   │   ├── res/
│   │   │   ├── drawable/
│   │   │   ├── layout/
│   │   │   ├── raw/
│   │   │   └── values/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── README.md
```

## 🚀 Run the Project

1. Install a current stable Android Studio.
2. Open the `PulsePlay` folder in Android Studio.
3. Allow Gradle to sync and download dependencies.
4. Connect an Android phone or create an emulator.
5. Enable USB debugging if using a physical phone.
6. Run the `app` configuration.
7. Open PulsePlay and wait for the API results.

An internet connection is required for the Home screen's API-powered metadata and artwork.

## 🎧 Audio Files

The repository contains three short **original generated demo MP3s**:

```text
app/src/main/res/raw/pulse_one.mp3
app/src/main/res/raw/pulse_two.mp3
app/src/main/res/raw/pulse_three.mp3
```

They are included only to make the repository immediately runnable without distributing copyrighted music.

For the strongest final recruitment presentation, these can be replaced with 1–3 music tracks that you have the right to use. Keep the filenames unchanged so no Kotlin code needs to change.

## 🔐 Secrets

No API key is required by the chosen metadata endpoint, so there are no secrets in this repository.

## 🧪 Demo Checklist

- [ ] Home loads at least 5 API songs
- [ ] Artwork loads
- [ ] Song title and artist appear
- [ ] Tapping a song opens Now Playing
- [ ] Play starts audio
- [ ] Pause pauses audio
- [ ] App can be minimized while playing
- [ ] Audio continues in background
- [ ] System media controls appear
- [ ] Screen can be locked while audio continues
- [ ] Back returns to Home
- [ ] API error state shows Retry
- [ ] Release APK installs successfully

## 📦 Recruitment Submission

The recruitment brief requires:

1. A **public GitHub repository link**
2. An **APK file**

Both should be entered in the recruitment Google Form.

## 🔮 Future Improvements

- Search bar for songs
- Mini-player on the Home screen
- Queue/playlist support
- Favorites
- Persistent playback position
- Offline metadata cache
- More detailed media notification controls
- Playback resumption after service/device restart

## 📚 Reference Documentation

- Android Media3 background playback: https://developer.android.com/media/media3/session/background-playback
- Android foreground services: https://developer.android.com/develop/background-work/services/fgs/declare
- Media3: https://developer.android.com/jetpack/androidx/releases/media3
- iTunes Search API: https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/
- Retrofit: https://square.github.io/retrofit/
- Coil: https://coil-kt.github.io/coil/

## 👩‍💻 Project

**PulsePlay — Android Development Recruitment Task**

Built with a focus on clean UI, reliable functionality and understandable architecture.
