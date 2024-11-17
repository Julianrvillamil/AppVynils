package com.misw.appvynills.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.misw.appvynills.models.Album
import com.misw.appvynills.models.Comment
import com.misw.appvynills.models.Performer
import com.misw.appvynills.models.Track

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)


// TrackEntity.kt
@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val duration: String,
    val albumId: Int // Clave foránea
)

// PerformerEntity.kt
@Entity(tableName = "performers")
data class PerformerEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String
)

// AlbumPerformerCrossRef.kt (tabla de relación muchos a muchos)
@Entity(
    tableName = "album_performer_cross_ref",
    primaryKeys = ["albumId", "performerId"]
)
data class AlbumPerformerCrossRef(
    val albumId: Int,
    val performerId: Int
)

// CommentEntity.kt
@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey
    val id: Int,
    val description: String,
    val rating: Int,
    val albumId: Int // Clave foránea
)

fun AlbumWithRelations.toDomainModel(): Album {
    return Album(
        id = album.id,
        name = album.name,
        cover = album.cover,
        releaseDate = album.releaseDate,
        description = album.description,
        genre = album.genre,
        recordLabel = album.recordLabel,
        tracks = tracks.map { it.toDomainModel() },
        performers = performers.map { it.toDomainModel() },
        comments = comments.map { it.toDomainModel() }
    )
}

fun Album.toEntityModels(): AlbumWithRelations {
    val albumEntity = AlbumEntity(
        id = id,
        name = name,
        cover = cover,
        releaseDate = releaseDate,
        description = description,
        genre = genre,
        recordLabel = recordLabel
    )

    val trackEntities = tracks.map {
        TrackEntity(
            id = it.id,
            name = it.name,
            duration = it.duration,
            albumId = id
        )
    }

    val performerEntities = performers.map {
        PerformerEntity(
            id = it.id,
            name = it.name,
            image = it.image,
            description = it.description,
            birthDate = it.birthDate
        )
    }

    val commentEntities = comments.map {
        CommentEntity(
            id = it.id,
            description = it.description,
            rating = it.rating,
            albumId = id
        )
    }

    return AlbumWithRelations(
        album = albumEntity,
        tracks = trackEntities,
        performers = performerEntities,
        comments = commentEntities
    )
}

// Funciones de mapeo adicionales
fun TrackEntity.toDomainModel() = Track(id, name, duration)
fun PerformerEntity.toDomainModel() = Performer(id, name, image, description, birthDate)
fun CommentEntity.toDomainModel() = Comment(id, description, rating)
