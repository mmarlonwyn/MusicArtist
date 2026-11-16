package com.example.musicartist.repository

import com.example.musicartist.common.AppException
import com.example.musicartist.common.safeApi
import com.example.musicartist.model.*
import com.example.musicartist.service.MusicService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MusicRepository(private val service: MusicService) {

    suspend fun getArtistPage(name: String): Result<ArtistPage> = safeApi {
        coroutineScope {
            val artistReq = async { service.searchArtist(name) }
            val albumsReq = async { service.searchAlbums(name) }
            val artist = artistReq.await().artists?.firstOrNull()
                ?: throw AppException("Artis \"$name\" tidak ditemukan")
            val albums = albumsReq.await().album.orEmpty().map { it.toDomain() }
            ArtistPage(artist.toDomain(), albums)
        }
    }

    suspend fun getAlbumDetail(albumId: String): Result<AlbumDetail> = safeApi {
        coroutineScope {
            val albumReq = async { service.albumDetail(albumId) }
            val tracksReq = async { service.albumTracks(albumId) }
            val album = albumReq.await().album?.firstOrNull()
                ?: throw AppException("Detail album tidak ditemukan")
            val tracks = tracksReq.await().track.orEmpty().map { it.toDomain() }.sortedBy { it.number }
            AlbumDetail(album.toDomain(), tracks)
        }
    }
}
