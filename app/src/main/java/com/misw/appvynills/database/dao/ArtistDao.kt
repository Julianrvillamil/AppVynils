package com.misw.appvynills.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.misw.appvynills.database.entity.ArtistEntity

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artists")
    suspend fun getAllArtists(): List<ArtistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<ArtistEntity>)

    @Query("SELECT * FROM artists WHERE id = :artistId")
    suspend fun getArtistById(artistId: Int): ArtistEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artists: ArtistEntity)

    @Query("DELETE FROM artists")
    suspend fun deleteAllArtists()
}