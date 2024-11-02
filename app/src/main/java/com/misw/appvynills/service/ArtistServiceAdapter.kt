package com.misw.appvynills.service
import com.misw.appvynills.dto.ArtistResponse
import retrofit2.http.GET
interface ArtistServiceAdapter {

    @GET("/musicians")
    suspend fun getArtists(): List<ArtistResponse>
}