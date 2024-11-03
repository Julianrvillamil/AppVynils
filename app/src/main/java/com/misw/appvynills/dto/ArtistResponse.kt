package com.misw.appvynills.dto

import com.misw.appvynills.model.Album

data class ArtistResponse(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birtDate: String,
    val albums: List<AlbumResponse>,

)
