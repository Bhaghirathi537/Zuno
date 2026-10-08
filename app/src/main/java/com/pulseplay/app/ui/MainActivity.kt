package com.pulseplay.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pulseplay.app.R
import com.pulseplay.app.data.ApiClient
import com.pulseplay.app.data.SongRepository
import com.pulseplay.app.model.Song
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: SongViewModel by viewModels {
        SongViewModelFactory(SongRepository(ApiClient.api))
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorContainer: View
    private lateinit var errorText: TextView
    private lateinit var emptyText: TextView
    private lateinit var adapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.songRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        errorContainer = findViewById(R.id.errorContainer)
        errorText = findViewById(R.id.errorText)
        emptyText = findViewById(R.id.emptyText)
        val retryButton: Button = findViewById(R.id.retryButton)

        adapter = SongAdapter { song -> openNowPlaying(song) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        retryButton.setOnClickListener { viewModel.loadSongs() }

        lifecycleScope.launch {
            viewModel.uiState.collect { state -> render(state) }
        }
    }

    private fun render(state: SongUiState) {
        progressBar.visibility = if (state is SongUiState.Loading) View.VISIBLE else View.GONE
        recyclerView.visibility = View.GONE
        errorContainer.visibility = View.GONE
        emptyText.visibility = View.GONE

        when (state) {
            SongUiState.Loading -> Unit
            is SongUiState.Success -> {
                if (state.songs.isEmpty()) {
                    emptyText.visibility = View.VISIBLE
                } else {
                    adapter.submitList(state.songs)
                    recyclerView.visibility = View.VISIBLE
                }
            }
            is SongUiState.Error -> {
                errorText.text = state.message
                errorContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun openNowPlaying(song: Song) {
        startActivity(
            Intent(this, NowPlayingActivity::class.java).apply {
                putExtra(NowPlayingActivity.EXTRA_ID, song.id)
                putExtra(NowPlayingActivity.EXTRA_TITLE, song.title)
                putExtra(NowPlayingActivity.EXTRA_ARTIST, song.artist)
                putExtra(NowPlayingActivity.EXTRA_ARTWORK, song.artworkUrl)
                putExtra(NowPlayingActivity.EXTRA_AUDIO_RES, song.audioResId)
            }
        )
    }
}
