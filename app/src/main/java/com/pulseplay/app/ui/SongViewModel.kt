package com.pulseplay.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulseplay.app.data.SongRepository
import com.pulseplay.app.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SongUiState {
    data object Loading : SongUiState
    data class Success(val songs: List<Song>) : SongUiState
    data class Error(val message: String) : SongUiState
}

class SongViewModel(private val repository: SongRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<SongUiState>(SongUiState.Loading)
    val uiState: StateFlow<SongUiState> = _uiState.asStateFlow()

    init {
        loadSongs()
    }

    fun loadSongs() {
        viewModelScope.launch {
            _uiState.value = SongUiState.Loading
            repository.fetchSongs()
                .onSuccess { songs -> _uiState.value = SongUiState.Success(songs) }
                .onFailure { error ->
                    _uiState.value = SongUiState.Error(
                        error.message ?: "Unable to load songs. Check your internet connection."
                    )
                }
        }
    }
}
