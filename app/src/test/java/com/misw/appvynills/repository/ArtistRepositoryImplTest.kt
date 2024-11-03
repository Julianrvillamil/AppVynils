package com.misw.appvynills.repository

import com.misw.appvynills.brokers.NetworkModule
import com.misw.appvynills.model.Artist
import com.misw.appvynills.service.ArtistServiceAdapter
import com.misw.appvynills.utils.DataState
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ArtistRepositoryImplTest {


    private lateinit var artistRepository: ArtistRepositoryImpl

    @Before
    fun setUp() {
        // Usa NetworkModule para obtener el servicio
        val artistService = NetworkModule.artistServiceAdapter
        artistRepository = ArtistRepositoryImpl(artistService)
    }

    @Test
    fun testGetArtistsFromAPI() = runBlocking {
        // Ejecuta la llamada real al API
        val response = artistRepository.getArtists()

        println(response)
        // Verifica si la respuesta es exitosa
        assertTrue(response is DataState.Success && response.data.isNotEmpty())
    }

    @Test
    fun testGetArtistByIdFromAPI() = runBlocking {
        print("Entrando a testGetArtistByIdFromAPI \n")
        // Given
        val artistId = 100

        // When
        val response = artistRepository.getArtistById(artistId)

        // Then
        assertNotNull("La respuesta no debería ser nula", response)

        assertTrue("La respuesta debería ser DataState.Success", response is DataState.Success)

        // Si es Success, verificamos los datos del artista
        if (response is DataState.Success) {
            val artist = response.data
            print(artist)
            assertNotNull("Los datos del artista no deberían ser nulos", artist)

            // Verificamos los campos específicos del artista
            assertNotNull("El ID del artista no debería ser nulo", artist.id)
            assertNotNull("El nombre del artista no debería ser nulo", artist.name)
            // Añade más verificaciones según los campos que tenga tu clase Artist
        }
    }
}
