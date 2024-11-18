package com.misw.appvynills.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.misw.appvynills.models.Artist

@Entity(tableName = "artists")
data class ArtistEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String?
)

fun ArtistEntity.toDomainModel(): Artist {
    return Artist(
        id = id,
        name = name,
        image = image,
        description = description,
        birthDate = birthDate ?: "" //valor por defecto si es null
    )
}

fun Artist.toEntityModel(): ArtistEntity {
    return ArtistEntity(
        id = id,
        name = name,
        image = image,
        description = description,
        birthDate = birthDate
    )
}
