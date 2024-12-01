package com.misw.appvynills.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.misw.appvynills.model.Collector
import com.misw.appvynills.model.CollectorAlbum
import com.misw.appvynills.model.CollectorComment
import com.misw.appvynills.model.CollectorPerformer

// CollectorEntity.kt
@Entity(tableName = "collectors")
data class CollectorEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val telephone: String,
    val email: String
)

//CollectorCommentEntity.kt
//Entity(tableName = "collector_comments")
@Entity(
    tableName = "collector_comments",
    foreignKeys = [
        ForeignKey(
            entity = CollectorEntity::class,
            parentColumns = ["id"],
            childColumns = ["collectorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CollectorCommentEntity(
    @PrimaryKey val id: Int,
    val description: String,
    val rating: Int,
    val collectorId: Int
)

//CollectorPerformerEntity.kt
@Entity(
    tableName = "collector_performers",
    foreignKeys = [
        ForeignKey(
            entity = CollectorEntity::class,
            parentColumns = ["id"],
            childColumns = ["collectorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
//@Entity(tableName = "collector_performers")
data class CollectorPerformerEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String?,
    val creationDate: String?,
    val collectorId: Int
)

@Entity(
    tableName = "collector_albums",
    foreignKeys = [
        ForeignKey(
            entity = CollectorEntity::class,
            parentColumns = ["id"],
            childColumns = ["collectorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["collectorId"])]
)
data class CollectorAlbumEntity(
    @PrimaryKey val id: Int,
    val price: Int,
    val status: String,
    val collectorId: Int
)


// Función de extensión para convertir Collector a CollectorEntity
fun Collector.toEntityModel(): CollectorEntity {
    return CollectorEntity(
        id = id,
        name = name,
        telephone = telephone,
        email = email
    )
}

// Función de extensión para convertir CollectorComment a CollectorCommentEntity
fun CollectorComment.toEntityModel(collectorId: Int): CollectorCommentEntity {
    return CollectorCommentEntity(
        id = id,
        description = description,
        rating = rating,
        collectorId = collectorId
    )
}

// Función de extensión para convertir CollectorPerformer a CollectorPerformerEntity
fun CollectorPerformer.toEntityModel(collectorId: Int): CollectorPerformerEntity {
    return CollectorPerformerEntity(
        id = id,
        name = name,
        image = image,
        description = description,
        birthDate = birthDate,
        creationDate = creationDate,
        collectorId = collectorId
    )
}

// Función de extensión para convertir CollectorAlbum a CollectorAlbumEntity
fun CollectorAlbum.toEntityModel(collectorId: Int): CollectorAlbumEntity {
    return CollectorAlbumEntity(
        id = id,
        price = price,
        status = status,
        collectorId = collectorId
    )
}


