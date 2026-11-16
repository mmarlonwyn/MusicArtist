package com.example.musicartist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.musicartist.model.Album
import com.example.musicartist.model.Artist
import com.example.musicartist.model.ArtistPage
import com.example.musicartist.viewmodel.MusicViewModel
import com.example.musicartist.viewmodel.UiState

@Composable
fun ArtistScreen(vm: MusicViewModel) {
    val query by vm.query.collectAsState()
    val home by vm.home.collectAsState()

    Column(Modifier.fillMaxSize()) {
        AppHeader()
        MusicSearchBar(query, vm::onQueryChange, onSearch = { vm.search() })
        Spacer(Modifier.height(12.dp))

        when (val s = home) {
            UiState.Loading -> LoadingView("Mencari artis...")
            is UiState.Error -> ErrorView(s.message, onRetry = { vm.search() })
            is UiState.Success -> ArtistContent(s.data, onAlbumClick = vm::openAlbum)
        }
    }
}

@Composable
private fun ArtistContent(page: ArtistPage, onAlbumClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) { ArtistBanner(page.artist) }
        item(span = { GridItemSpan(maxLineSpan) }) {
            ExpandableText("Tentang Artis", page.artist.bio, "Baca selengkapnya", "Tutup ringkasan")
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Diskografi Album", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Koleksi rilisan resmi", color = MMuted, fontSize = 11.sp)
                }
                Text("${page.albums.size} Album", color = MGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clip(CircleShape).background(MGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp))
            }
        }
        items(page.albums) { album -> AlbumCard(album, onClick = { onAlbumClick(album.id) }) }
    }
}

@Composable
private fun ArtistBanner(artist: Artist) {
    Box(Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(20.dp)).background(MSurface)) {
        AsyncImage(artist.banner, artist.name, contentScale = ContentScale.Crop, modifier = Modifier.matchParentSize())
        Box(Modifier.matchParentSize().background(
            Brush.verticalGradient(listOf(Color.Transparent, MBg.copy(alpha = 0.95f)), startY = 120f)))
        Row(Modifier.align(Alignment.BottomStart).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(artist.thumb, null, contentScale = ContentScale.Crop,
                modifier = Modifier.size(46.dp).clip(CircleShape).background(MSurface2))
            Spacer(Modifier.width(10.dp))
            Column {
                Text(artist.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text(buildString {
                    append(artist.genre)
                    if (artist.country.isNotBlank()) append("  •  ${artist.country}")
                }, color = MGreen, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun ExpandableText(title: String, text: String, moreLabel: String, lessLabel: String) {
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))
        Text(text, color = MMuted, fontSize = 12.sp, lineHeight = 18.sp,
            maxLines = if (expanded) Int.MAX_VALUE else 3, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(6.dp))
        Text(if (expanded) lessLabel else moreLabel, color = MGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { expanded = !expanded })
    }
}

@Composable
private fun AlbumCard(album: Album, onClick: () -> Unit) {
    Column(Modifier.clickable(onClick = onClick)) {
        AsyncImage(album.cover, album.title, contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(14.dp)).background(MSurface))
        Spacer(Modifier.height(8.dp))
        Text(album.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(album.year, color = MGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(album.genre, color = MMuted, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
