package com.misw.appvynills.repository

import com.misw.appvynills.brokers.NetworkModule
import com.misw.appvynills.utils.DataState
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class ArtistRepositoryImplTest {


    private lateinit var artistRepository: ArtistRepository

    @Before
    fun setUp() {
        // Usa NetworkModule para obtener el servicio
        val artistService = NetworkModule.artistServiceAdapter
        artistRepository = ArtistRepository(artistService)
    }

    @Test
    fun testGetArtistsFromAPI() = runBlocking {
        // Ejecuta la llamada real al API
        val response = artistRepository.getArtists()

        println(response)
        // Verifica si la respuesta es exitosa
        assertTrue(response is DataState.Success<*>)
    }

    @Test
    fun testGetArtistByIdFromAPI() = runBlocking {
        println("Entrando a testGetArtistByIdFromAPI")

        // Given
        val artistId = 100
        var loadingReceived = false
        var successReceived = false

        // When & Then
        artistRepository.getArtistById(artistId).collect { state ->
            when (state) {
                is DataState.Loading -> {
                    loadingReceived = true
                }
                is DataState.Success -> {
                    successReceived = true
                    val artist = state.data

                    // Verificaciones básicas
                    assertNotNull("Los datos del artista no deberían ser nulos", artist)
                    assertEquals("El ID debería ser $artistId", artistId, artist.id)
                    assertNotNull("El nombre del artista no debería ser nulo", artist.name)
                    assertTrue("El nombre del artista no debería estar vacío", artist.name.isNotEmpty())
                    assertNotNull("La imagen del artista no debería ser nula", artist.image)
                    assertNotNull("La descripción del artista no debería ser nula", artist.description)
                    assertNotNull("La fecha de nacimiento no debería ser nula", artist.birthDate)

                    // Verificación de álbumes si existen
                    artist.albums?.let { albums ->
                        albums.forEach { album ->
                            assertNotNull("El ID del álbum no debería ser nulo", album.id)
                            assertNotNull("El nombre del álbum no debería ser nulo", album.name)
                            assertNotNull("La portada del álbum no debería ser nula", album.cover)
                        }
                    }

                    println("Test exitoso con artista: $artist")
                }
                is DataState.Error -> {
                    fail("No debería recibir un error: ${state.error.message}")
                }
            }
        }

        assertTrue("Debería haber recibido Loading", loadingReceived)
        assertTrue("Debería haber recibido Success", successReceived)
    }
}
