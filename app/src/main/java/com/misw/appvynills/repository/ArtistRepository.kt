package com.misw.appvynills.repository

import com.misw.appvynills.database.VinylRoomDatabase
import com.misw.appvynills.database.entity.toDomainModel
import com.misw.appvynills.database.entity.toEntityModel
import com.misw.appvynills.dto.ArtistResponse
import com.misw.appvynills.models.Artist
import com.misw.appvynills.models.DTO
import com.misw.appvynills.service.ArtistServiceAdapter
import com.misw.appvynills.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ArtistRepository(
    private val artistService: ArtistServiceAdapter,
    private val database: VinylRoomDatabase) {

    private val artistDao = database.artistDao()

    suspend fun getArtists(): Flow<DataState<List<Artist>>> = flow {
        emit(DataState.Loading)
        try {
            val response: List<ArtistResponse> = artistService.getArtists()
            val artists = response.map { it.DTO() }

            //Save artists in DB
            artistDao.deleteAllArtists()
            artistDao.insertArtists(artists.map { it.toEntityModel() })

            emit(DataState.Success(artists))
            //emit(DataState.Success(response.map { it.DTO() }))

        } catch (e: IOException) {
            // Si hay un error, usa los datos locales si existen
            val localArtists = artistDao.getAllArtists().map { it.toDomainModel() }
            if (localArtists.isNotEmpty()) {
                emit(DataState.Success(localArtists))
            } else {
                emit(DataState.Error(e))
            }
        } catch (e: HttpException) {
            emit(DataState.Error(e))
        }
    }

    suspend fun getArtistById(id: Int): Flow<DataState<Artist>> = flow {
        emit(DataState.Loading)
        try {
            val response: ArtistResponse = artistService.getArtistById(id)
            val artist = response.DTO()

            // Save in DB
            artistDao.insertArtist(artist.toEntityModel())

            emit(DataState.Success(artist))
        } catch (e: IOException) {
            // Manejo de datos locales si falla la red
            val localArtist = withContext(Dispatchers.IO) {
                artistDao.getArtistById(id)?.toDomainModel()
            }
            if (localArtist != null) {
                emit(DataState.Success(localArtist))
            } else {
                emit(DataState.Error(e))
            }

        } catch (e: HttpException) {
            emit(DataState.Error(e))
        }
    }
}

