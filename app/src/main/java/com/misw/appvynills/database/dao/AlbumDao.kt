package com.misw.appvynills.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.misw.appvynills.database.entity.AlbumEntity
import com.misw.appvynills.database.entity.AlbumPerformerCrossRef
import com.misw.appvynills.database.entity.AlbumWithRelations
import com.misw.appvynills.database.entity.CommentEntity
import com.misw.appvynills.database.entity.PerformerEntity
import com.misw.appvynills.database.entity.TrackEntity

@Dao
interface AlbumDao {

    @Transaction
    @Query("SELECT * FROM albums")
    suspend fun getAllAlbumsWithRelations(): List<AlbumWithRelations>

    @Transaction
    @Query("SELECT * FROM albums WHERE id = :albumId")
    suspend fun getAlbumWithRelations(albumId: Int): AlbumWithRelations?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerformers(performers: List<PerformerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumPerformerCrossRefs(crossRefs: List<AlbumPerformerCrossRef>)

    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()

    @Query("DELETE FROM tracks")
    suspend fun deleteAllTracks()

    @Query("DELETE FROM performers")
    suspend fun deleteAllPerformers()

    @Query("DELETE FROM comments")
    suspend fun deleteAllComments()

    @Query("DELETE FROM album_performer_cross_ref")
    suspend fun deleteAllCrossRefs()

    @Transaction
    suspend fun deleteEverything() {
        deleteAllComments()
        deleteAllTracks()
        deleteAllCrossRefs()
        deleteAllPerformers()
        deleteAllAlbums()
    }
}

