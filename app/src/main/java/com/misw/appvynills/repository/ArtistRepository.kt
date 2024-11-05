package com.misw.appvynills.repository

import com.misw.appvynills.dto.ArtistResponse
import com.misw.appvynills.model.Artist
import com.misw.appvynills.model.DTO
import com.misw.appvynills.service.ArtistServiceAdapter
import com.misw.appvynills.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class ArtistRepository(private val artistService: ArtistServiceAdapter) {

    fun getArtists(): Flow<DataState<List<Artist>>> = flow {
        emit(DataState.Loading)
        try {
            val response: List<ArtistResponse> = artistService.getArtists()
            emit(DataState.Success(response.map { it.DTO() }))
        } catch (e: IOException) {
            emit(DataState.Error(e))
        } catch (e: HttpException) {
            emit(DataState.Error(e))
        }
    }

    fun getArtistById(id: Int): Flow<DataState<Artist>> = flow {
        emit(DataState.Loading)
        try {
            val response: ArtistResponse = artistService.getArtistById(id)
            emit(DataState.Success(response.DTO()))
        } catch (e: IOException) {
            emit(DataState.Error(e))
        } catch (e: HttpException) {
            emit(DataState.Error(e))
        }
    }
}