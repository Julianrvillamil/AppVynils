package com.misw.appvynills.repository

import android.service.autofill.Dataset
import com.misw.appvynills.model.Artist
import com.misw.appvynills.model.DTO
import com.misw.appvynills.service.ArtistServiceAdapter
import com.misw.appvynills.utils.DataState
import com.misw.appvynills.utils.resultOrError

interface ArtistRepository {
    suspend fun getArtists(): DataState<List<Artist>>
    suspend fun getArtistById(id: Int): DataState<Artist>
}

class ArtistRepositoryImpl(
    private val artistService: ArtistServiceAdapter
): ArtistRepository {
    override suspend fun getArtists(): DataState<List<Artist>> {
        return try {
            // Obtiene la lista de ArtistResponse desde el servicio
            val artistResponses = artistService.getArtists()
            // Transforma la lista de ArtistResponse a Artist utilizando la función de extensión
            val artists = artistResponses.DTO()
            DataState.Success(artists) // Devuelve la lista transformada
        } catch (e: Exception) {
            e.printStackTrace()
            DataState.Error(e) // Manejo de error
        }
    }

    override suspend fun getArtistById(id: Int): DataState<Artist> {
        return try {
            // Obtiene el ArtistResponse desde el servicio
            val artistResponse = artistService.getArtistById(id)
            // Transforma el ArtistResponse a Artist utilizando la función de extensión
            val artist = artistResponse.DTO()
            DataState.Success(artist) // Devuelve el objeto tranformado
        } catch (e: Exception) {
            e.printStackTrace()
            DataState.Error(e) // Manejo de error
        }
    }

}

