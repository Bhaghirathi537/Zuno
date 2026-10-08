package com.pulseplay.app.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val artworkUrl: String,
    val audioResId: Int
)
