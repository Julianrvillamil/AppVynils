package com.misw.appvynills.service
import com.misw.appvynills.dto.ArtistResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtistServiceAdapter {

    @GET("/musicians")
    suspend fun getArtists(): List<ArtistResponse>

    @GET("musicians/{id}")
    suspend fun getArtistById(
        @Path("id") artistId: Int
    ): ArtistResponse
}