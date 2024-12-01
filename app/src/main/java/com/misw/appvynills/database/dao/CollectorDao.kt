package com.misw.appvynills.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.misw.appvynills.database.entity.CollectorAlbumEntity
import com.misw.appvynills.database.entity.CollectorCommentEntity
import com.misw.appvynills.database.entity.CollectorEntity
import com.misw.appvynills.database.entity.CollectorPerformerEntity
import com.misw.appvynills.database.entity.CollectorWithRelations

@Dao
interface CollectorDao {

    @Transaction
    @Query("SELECT * FROM collectors")
    suspend fun getAllCollectors(): List<CollectorWithRelations>

    @Transaction
    @Query("SELECT * FROM collectors WHERE id = :collectorId")
    suspend fun getCollectorById(collectorId: Int): CollectorWithRelations?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectors(collectors: List<CollectorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectorComments(comments: List<CollectorCommentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectorPerformers(performers: List<CollectorPerformerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectorAlbums(albums: List<CollectorAlbumEntity>)

    @Query("DELETE FROM collectors")
    suspend fun deleteAllCollectors()

    @Query("DELETE FROM collector_comments")
    suspend fun deleteAllCollectorComments()

    @Query("DELETE FROM collector_performers")
    suspend fun deleteAllCollectorPerformers()

    @Query("DELETE FROM collector_albums")
    suspend fun deleteAllCollectorAlbums()
}