package com.misw.appvynills.model

data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks: List<Track>,
    val performers: List<Performer>,
    val comments: List<Comment>
)

data class Track(
    val id: Int,
    val name: String,
    val duration: String
)

data class Performer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String
)

data class Comment(
    val id: Int,
    val description: String,
    val rating: Int
)

