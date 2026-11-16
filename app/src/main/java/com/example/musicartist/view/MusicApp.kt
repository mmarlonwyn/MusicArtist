package com.example.musicartist.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicartist.viewmodel.MusicViewModel

@Composable
fun MusicApp(modifier: Modifier = Modifier, vm: MusicViewModel = viewModel()) {
    val detail by vm.detail.collectAsState()

    BackHandler(enabled = detail != null) { vm.closeAlbum() }

    Box(modifier.background(MBg)) {
        val d = detail
        if (d == null) {
            ArtistScreen(vm)
        } else {
            AlbumDetailScreen(state = d, onBack = vm::closeAlbum, onRetry = vm::retryAlbum)
        }
    }
}
