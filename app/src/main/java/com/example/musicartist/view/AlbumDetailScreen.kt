package com.example.musicartist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.musicartist.model.AlbumDetail
import com.example.musicartist.model.Track
import com.example.musicartist.viewmodel.UiState
import java.util.Locale

@Composable
fun AlbumDetailScreen(state: UiState<AlbumDetail>, onBack: () -> Unit, onRetry: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali", tint = Color.White)
            }
            Text("Detail Album", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        when (state) {
            UiState.Loading -> LoadingView("Memuat detail album & track list...")
            is UiState.Error -> ErrorView(state.message, onRetry)
            is UiState.Success -> DetailContent(state.data)
        }
    }
}

@Composable
private fun DetailContent(detail: AlbumDetail) {
    val album = detail.album
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)) {
        item {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(album.cover, album.title, contentScale = ContentScale.Crop,
                    modifier = Modifier.size(220.dp).clip(RoundedCornerShape(20.dp)).background(MSurface))
                Spacer(Modifier.height(16.dp))
                Text(album.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp,
                    textAlign = TextAlign.Center)
                Text(album.artist, color = MGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Pill("Tahun ${album.year}")
                    Pill(album.genre)
                }
                Spacer(Modifier.height(20.dp))
            }
        }
        item {
            ExpandableText("Deskripsi Album", album.description ?: "Deskripsi tidak tersedia.",
                "Baca selengkapnya", "Tutup ringkasan")
            Spacer(Modifier.height(24.dp))
        }
        item {
            Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MusicNote, null, tint = MGreen, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Daftar Lagu", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Text("${detail.tracks.size} Lagu", color = MGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clip(CircleShape).background(MGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp))
            }
        }
        if (detail.tracks.isEmpty()) {
            item { Text("Daftar lagu tidak tersedia.", color = MMuted, fontSize = 12.sp) }
        }
        items(detail.tracks) { TrackRow(it) }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun TrackRow(track: Track) {
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(String.format(Locale.US, "%02d", track.number), color = MGreen, fontWeight = FontWeight.Bold,
            fontSize = 13.sp, modifier = Modifier.width(32.dp))
        Column(Modifier.weight(1f)) {
            Text(track.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(track.genre, color = MMuted, fontSize = 11.sp)
        }
        Icon(Icons.Default.Schedule, null, tint = MMuted, modifier = Modifier.size(13.dp))
        Spacer(Modifier.width(4.dp))
        Text(formatDuration(track.durationMs), color = MMuted, fontSize = 11.sp)
    }
}

private fun formatDuration(ms: Long): String {
    val total = ms / 1000
    return String.format(Locale.US, "%02d:%02d", total / 60, total % 60)
}
