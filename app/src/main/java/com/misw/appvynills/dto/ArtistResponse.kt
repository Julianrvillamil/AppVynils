package com.misw.appvynills.dto

data class ArtistResponse(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String,
    val albums: List<AlbumResponse>,

)
