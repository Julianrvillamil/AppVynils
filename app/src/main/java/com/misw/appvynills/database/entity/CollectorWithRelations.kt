package com.misw.appvynills.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.misw.appvynills.model.Collector
import com.misw.appvynills.model.CollectorAlbum
import com.misw.appvynills.model.CollectorComment
import com.misw.appvynills.model.CollectorPerformer

data class CollectorWithRelations(
    @Embedded val collector: CollectorEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectorId"
    )
    val comments: List<CollectorCommentEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectorId"
    )
    val favoritePerformers: List<CollectorPerformerEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "collectorId"
    )
    val collectorAlbums: List<CollectorAlbumEntity>
)

fun CollectorWithRelations.toDomainModel(): Collector {
    return Collector(
        id = collector.id,
        name = collector.name,
        telephone = collector.telephone,
        email = collector.email,
        comments = comments.map { commentEntity ->
            CollectorComment(
                id = commentEntity.id,
                description = commentEntity.description,
                rating = commentEntity.rating
            )
        },
        favoritePerformers = favoritePerformers.map { performerEntity ->
            CollectorPerformer(
                id = performerEntity.id,
                name = performerEntity.name,
                image = performerEntity.image,
                description = performerEntity.description,
                birthDate = performerEntity.birthDate,
                creationDate = performerEntity.creationDate
            )
        },
        collectorAlbums = collectorAlbums.map { albumEntity ->
            CollectorAlbum(
                id = albumEntity.id,
                price = albumEntity.price,
                status = albumEntity.status
            )
        }
    )
}
