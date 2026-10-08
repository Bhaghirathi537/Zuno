package com.pulseplay.app.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import coil3.load
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.pulseplay.app.R
import com.pulseplay.app.playback.PlaybackService

class NowPlayingActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_ARTIST = "extra_artist"
        const val EXTRA_ARTWORK = "extra_artwork"
        const val EXTRA_AUDIO_RES = "extra_audio_res"
    }

    private var controller: MediaController? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private lateinit var playPauseButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_now_playing)

        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
        val artist = intent.getStringExtra(EXTRA_ARTIST).orEmpty()
        val artworkUrl = intent.getStringExtra(EXTRA_ARTWORK).orEmpty()
        val audioResId = intent.getIntExtra(EXTRA_AUDIO_RES, 0)
        val songId = intent.getStringExtra(EXTRA_ID).orEmpty()

        val titleView: TextView = findViewById(R.id.nowPlayingTitle)
        val artistView: TextView = findViewById(R.id.nowPlayingArtist)
        val artworkView: ImageView = findViewById(R.id.nowPlayingArtwork)
        playPauseButton = findViewById(R.id.playPauseButton)
        val backButton: ImageButton = findViewById(R.id.backButton)

        titleView.text = title
        artistView.text = artist
        artworkView.load(artworkUrl)

        backButton.setOnClickListener { finish() }

        val token = SessionToken(this, android.content.ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, token).buildAsync()
        controllerFuture?.addListener({
            controller = controllerFuture?.get()
            controller?.addListener(object : androidx.media3.common.Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    updatePlayPauseIcon(isPlaying)
                }
            })
            updatePlayPauseIcon(controller?.isPlaying == true)

            if (controller?.currentMediaItem?.mediaId != songId) {
                val mediaItem = MediaItem.Builder()
                    .setMediaId(songId)
                    .setUri(Uri.parse("android.resource://$packageName/$audioResId"))
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(title)
                            .setArtist(artist)
                            .build()
                    )
                    .build()

                controller?.setMediaItem(mediaItem)
                controller?.prepare()
            }
        }, MoreExecutors.directExecutor())

        playPauseButton.setOnClickListener {
            controller?.let {
                if (it.isPlaying) it.pause() else it.play()
                updatePlayPauseIcon(it.isPlaying)
            }
        }
    }

    private fun updatePlayPauseIcon(isPlaying: Boolean) {
        playPauseButton.setImageResource(
            if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        )
        playPauseButton.contentDescription = if (isPlaying) "Pause" else "Play"
    }

    override fun onDestroy() {
        controller?.release()
        controller = null
        controllerFuture = null
        super.onDestroy()
    }
}
