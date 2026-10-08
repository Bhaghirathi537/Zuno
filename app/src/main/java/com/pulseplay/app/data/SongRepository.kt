package com.pulseplay.app.data

import com.pulseplay.app.R
import com.pulseplay.app.model.Song

class SongRepository(private val api: ItunesApi) {

    suspend fun fetchSongs(): Result<List<Song>> {
        return runCatching {
            val response = api.searchSongs(term = "pop")
            val audioResources = listOf(R.raw.zuno_demo_1, R.raw.zuno_demo_2, R.raw.zuno_demo_3)

            response.results
                .filter { !it.trackName.isNullOrBlank() && !it.artistName.isNullOrBlank() }
                .take(10)
                .mapIndexed { index, track ->
                    Song(
                        id = (track.trackId ?: index.toLong()).toString(),
                        title = track.trackName!!.trim(),
                        artist = track.artistName!!.trim(),
                        artworkUrl = track.artworkUrl100.orEmpty().replace("http://", "https://"),
                        audioResId = audioResources[index % audioResources.size]
                    )
                }
                .also {
                    require(it.size >= 5) { "The music API returned fewer than 5 usable songs." }
                }
        }
    }
}
