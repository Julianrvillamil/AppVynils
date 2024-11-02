package com.misw.appvynills.repository

import com.misw.appvynills.brokers.NetworkModule
import com.misw.appvynills.model.Artist
import com.misw.appvynills.service.ArtistServiceAdapter
import com.misw.appvynills.utils.DataState
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
}
