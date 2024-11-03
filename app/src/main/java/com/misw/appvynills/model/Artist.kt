package com.misw.appvynills.model

import com.misw.appvynills.dto.AlbumResponse
import com.misw.appvynills.dto.ArtistResponse

data class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birtDate: String,
    val albums: List<Album>? = null
)

fun ArtistResponse.DTO() = Artist(
    id = id,
    name = name,
    image = image,
    description = description,
    birtDate = birtDate ?: "Fecha no disponible",
    albums = albums?.map { it.toAlbum() }
)

fun AlbumResponse.DTO() = Album(
    id = id,
    name = name,
    cover = cover,
    releaseDate = releaseDate,
    description = description,
    genre = genre,
    recordLabel = recordLabel,
    tracks = tracks?.map { it.toTrackDTO() } ?: emptyList(),
    performers = performers?.map { it.toPerformerDTO() } ?: emptyList(),
    comments = comments?.map { it.toCommentsDTO() } ?: emptyList()
)

fun List<ArtistResponse>?.DTO() = this?.map { it.DTO() } ?: emptyList()

// Extension function to map AlbumResponse to Album
fun AlbumResponse.toAlbum() = Album(
    id = id,
    name = name,
    cover = cover,
    comments = comments?.map { it.toCommentsDTO() } ?: emptyList(),
    description = description,
    genre = genre,
    performers = performers?.map { it.toPerformerDTO() } ?: emptyList(),
    recordLabel = recordLabel,
    releaseDate = releaseDate,
    tracks = tracks?.map { it.toTrackDTO() } ?: emptyList()
)

fun Performer.toPerformerDTO() = Performer(
    id = id,
    name = name,
    image = image,
    description = description,
    birthDate = birthDate
)

fun Track.toTrackDTO() = Track(
    id = id,
    name = name,
    duration = duration
)

fun Comment.toCommentsDTO() = Comment(
    id = id,
    description = description,
    rating = rating
)