package com.misw.appvynills.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class AlbumWithRelations(
    @Embedded val album: AlbumEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val tracks: List<TrackEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AlbumPerformerCrossRef::class,
            parentColumn = "albumId",
            entityColumn = "performerId"
        )
    )
    val performers: List<PerformerEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val comments: List<CommentEntity>
)
