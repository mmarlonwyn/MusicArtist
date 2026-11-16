package com.example.musicartist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicartist.model.AlbumDetail
import com.example.musicartist.model.ArtistPage
import com.example.musicartist.repository.MusicRepository
import com.example.musicartist.service.MusicApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

class MusicViewModel(
    private val repo: MusicRepository = MusicRepository(MusicApi.service)
) : ViewModel() {

    private val _query = MutableStateFlow("Coldplay")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _home = MutableStateFlow<UiState<ArtistPage>>(UiState.Loading)
    val home: StateFlow<UiState<ArtistPage>> = _home.asStateFlow()

    // null = sedang di halaman artis, bukan di detail album
    private val _detail = MutableStateFlow<UiState<AlbumDetail>?>(null)
    val detail: StateFlow<UiState<AlbumDetail>?> = _detail.asStateFlow()

    private var lastAlbumId: String? = null

    init { search() }

    fun onQueryChange(q: String) { _query.value = q }

    fun search(q: String = _query.value) {
        if (q.isBlank()) return
        _query.value = q
        viewModelScope.launch {
            _home.value = UiState.Loading
            repo.getArtistPage(q.trim()).fold(
                onSuccess = { _home.value = UiState.Success(it) },
                onFailure = { _home.value = UiState.Error(it.message ?: "Terjadi kesalahan") }
            )
        }
    }

    fun openAlbum(albumId: String) {
        lastAlbumId = albumId
        viewModelScope.launch {
            _detail.value = UiState.Loading
            repo.getAlbumDetail(albumId).fold(
                onSuccess = { _detail.value = UiState.Success(it) },
                onFailure = { _detail.value = UiState.Error(it.message ?: "Terjadi kesalahan") }
            )
        }
    }

    fun retryAlbum() { lastAlbumId?.let { openAlbum(it) } }
    fun closeAlbum() { _detail.value = null }
}
