package com.pulseplay.app.data

import retrofit2.http.GET
import retrofit2.http.Query

data class ItunesResponse(
    val resultCount: Int,
    val results: List<ItunesTrack>
)

data class ItunesTrack(
    val trackId: Long?,
    val trackName: String?,
    val artistName: String?,
    val artworkUrl100: String?
)

interface ItunesApi {
    @GET("search")
    suspend fun searchSongs(
        @Query("term") term: String,
        @Query("country") country: String = "us",
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 10,
        @Query("explicit") explicit: String = "No"
    ): ItunesResponse
}
