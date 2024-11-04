package com.misw.appvynills.brokers

import com.misw.appvynills.service.ArtistServiceAdapter
import com.misw.appvynills.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val artistServiceAdapter: ArtistServiceAdapter = getRetrofit().create(ArtistServiceAdapter::class.java)
}