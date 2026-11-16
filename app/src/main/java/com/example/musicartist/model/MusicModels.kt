package com.example.musicartist.model

// ---------- DTO ----------
data class ArtistResponse(val artists: List<ArtistDto>? = null)
data class ArtistDto(
    val idArtist: String? = null,
    val strArtist: String? = null,
    val strGenre: String? = null,
    val strStyle: String? = null,
    val strCountry: String? = null,
    val strBiographyEN: String? = null,
    val strArtistThumb: String? = null,
    val strArtistBanner: String? = null,
    val strArtistFanart: String? = null
)

data class AlbumResponse(val album: List<AlbumDto>? = null)
data class AlbumDto(
    val idAlbum: String? = null,
    val strAlbum: String? = null,
    val strArtist: String? = null,
    val intYearReleased: String? = null,
    val strGenre: String? = null,
    val strStyle: String? = null,
    val strDescriptionEN: String? = null,
    val strAlbumThumb: String? = null
)

data class TrackResponse(val track: List<TrackDto>? = null)
data class TrackDto(
    val idTrack: String? = null,
    val strTrack: String? = null,
    val intTrackNumber: String? = null,
    val intDuration: String? = null,
    val strGenre: String? = null
)

// ---------- Model UI ----------
data class Artist(
    val name: String,
    val genre: String,
    val country: String,
    val bio: String,
    val thumb: String?,
    val banner: String?
)

data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val year: String,
    val genre: String,
    val description: String?,
    val cover: String?
)

data class Track(val number: Int, val title: String, val genre: String, val durationMs: Long)

data class ArtistPage(val artist: Artist, val albums: List<Album>)
data class AlbumDetail(val album: Album, val tracks: List<Track>)

fun ArtistDto.toDomain() = Artist(
    name = strArtist ?: "-",
    genre = strGenre ?: strStyle ?: "-",
    country = strCountry ?: "",
    bio = strBiographyEN ?: "Biografi tidak tersedia.",
    thumb = strArtistThumb,
    banner = strArtistBanner ?: strArtistFanart ?: strArtistThumb
)

fun AlbumDto.toDomain() = Album(
    id = idAlbum.orEmpty(),
    title = strAlbum ?: "-",
    artist = strArtist ?: "-",
    year = intYearReleased ?: "-",
    genre = strGenre ?: strStyle ?: "-",
    description = strDescriptionEN,
    cover = strAlbumThumb
)

fun TrackDto.toDomain() = Track(
    number = intTrackNumber?.toIntOrNull() ?: 0,
    title = strTrack ?: "-",
    genre = strGenre ?: "-",
    durationMs = intDuration?.toLongOrNull() ?: 0L
)
