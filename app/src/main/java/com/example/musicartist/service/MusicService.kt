package com.example.musicartist.service

import com.example.musicartist.model.AlbumResponse
import com.example.musicartist.model.ArtistResponse
import com.example.musicartist.model.TrackResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {
    @GET("search.php")
    suspend fun searchArtist(@Query("s") name: String): ArtistResponse

    @GET("searchalbum.php")
    suspend fun searchAlbums(@Query("s") name: String): AlbumResponse

    @GET("album.php")
    suspend fun albumDetail(@Query("m") albumId: String): AlbumResponse

    @GET("track.php")
    suspend fun albumTracks(@Query("m") albumId: String): TrackResponse
}

object MusicApi {
    val service: MusicService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.theaudiodb.com/api/v1/json/123/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicService::class.java)
    }
}
