package com.misw.appvynills.model

import com.misw.appvynills.dto.ArtistResponse

data class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birtDate: String
)

fun ArtistResponse.DTO() = Artist(
    id,
    name,
    image,
    description,
    birtDate = birtDate ?: "Fecha no disponible"
)

fun List<ArtistResponse>.DTO() = map { it.DTO() }