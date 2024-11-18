package com.misw.appvynills.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ArtistWithAlbums(
    @Embedded val artist: ArtistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "artistId"
    )
    val albums: List<AlbumEntity>
)

