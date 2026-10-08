# PulsePlay — Project Explanation & Interview Defence

## 1. Requirement mapping

| Recruitment requirement | Implementation | Demo |
|---|---|---|
| 5+ songs | iTunes Search API + RecyclerView | Open Home and show the loaded list |
| Thumbnail/title/artist | Song model + SongAdapter + Coil | Inspect a song card |
| Clickable songs | RecyclerView click listener | Tap a song |
| Now Playing | NowPlayingActivity | Show artwork/title/artist |
| Play/Pause | MediaController → MediaSession → ExoPlayer | Play and pause |
| 1–3 MP3 files | Three files in `res/raw` | Play a track |
| Public API | iTunes Search API through Retrofit | Disable network and demonstrate Retry |
| Background playback | Media3 MediaSessionService | Minimize/lock phone while playing |

## 2. How the app starts

`MainActivity` creates a `SongViewModel`.

The ViewModel asks `SongRepository` to fetch songs.

The repository calls the Retrofit API service.

The API returns JSON.

Gson converts the JSON into Kotlin data classes.

The repository maps the API objects into the app's `Song` model.

The ViewModel publishes `Success` through a `StateFlow`.

The Activity receives the state and gives the songs to the RecyclerView adapter.

## 3. How the API becomes a song card

The API gives fields such as:

```text
trackName
artistName
artworkUrl100
```

The repository converts them to:

```text
Song(
    id,
    title,
    artist,
    artworkUrl,
    audioResId
)
```

The adapter binds the title and artist to TextViews and asks Coil to load the artwork URL into the ImageView.

## 4. How playback works

The Activity does not own the ExoPlayer.

Instead:

```text
Activity
   ↓
MediaController
   ↓
MediaSession
   ↓
MediaSessionService
   ↓
ExoPlayer
```

This separation is the important Level 3 architecture.

The service creates the ExoPlayer and MediaSession in `onCreate()` and releases them in `onDestroy()`.

When the user presses Play, the MediaController sends the playback command through the MediaSession to ExoPlayer.

## 5. Why the Service is necessary

An Activity is a UI component. It is not the correct place to own long-running background playback.

A MediaSessionService can keep the media player/session alive independently of the visible Activity. Android's Media3 documentation recommends this approach for background playback.

## 6. Why local MP3s are used

The recruitment brief requires 1–3 actual MP3 resources for Level 2 and real Play/Pause behavior.

The API is therefore used for Level 3 metadata and artwork, while local demo audio provides deterministic playback.

This means the app does not fail its playback demonstration because an external audio URL disappears.

## 7. Interview answers

### Why Kotlin + XML?

It is one of the technologies explicitly allowed in the recruitment task. Kotlin is concise and XML keeps the UI layout structure easy to inspect and explain.

### Why RecyclerView?

The Home screen is a dynamic list of songs. RecyclerView efficiently reuses item views instead of creating a separate view for every song.

### Why ViewModel?

The ViewModel owns UI state outside the Activity's view code and can survive normal Activity recreation.

### Why Repository?

The Repository separates data access from the UI. The Activity/ViewModel does not need to know how the API request is implemented.

### Why Retrofit?

Retrofit turns a Kotlin interface into HTTP API calls and works cleanly with coroutines.

### Why Coil?

Coil is a lightweight Kotlin-first image loader. It handles downloading, decoding and caching remote artwork for ImageViews.

### Why Media3?

Media3 is Android's current media library and provides the ExoPlayer, MediaSession and MediaController building blocks used here.

### Why ExoPlayer?

ExoPlayer is the Media3 player implementation responsible for actual media playback.

### Why MediaSessionService?

It lets the Player and MediaSession live in a service separate from the Activity, which is the architecture needed for background playback.

### Why a foreground service?

Long-running background media playback is a user-visible task. Android requires the appropriate foreground-service type and permissions for this use case.

### How does background playback work?

The Activity talks to a MediaController. The controller communicates with the MediaSession in the service. ExoPlayer stays in that service, so hiding the Activity does not destroy the player.

### What happens if the API fails?

The ViewModel exposes an Error state and the Home screen displays an error message with a Retry button.

### How are resources cleaned up?

The Activity releases its MediaController. The playback service releases the MediaSession and ExoPlayer in `onDestroy()`.

### What would you improve next?

I would add a mini-player, search, favorites, a queue, persistent playback position and better offline caching.

## 8. Two-minute demo

1. Launch PulsePlay.
2. Point out that the Home screen is populated from the public API.
3. Show artwork, song names and artists.
4. Tap a song.
5. Explain that the Now Playing screen receives the selected song data.
6. Press Play and let the audio start.
7. Press Pause.
8. Press Play again.
9. Minimize the app.
10. Show that playback continues and Android's media controls are available.
11. Lock the screen if the phone allows it.
12. Return to PulsePlay.
13. Finish by explaining the Activity → MediaController → MediaSession → Service → ExoPlayer flow.
