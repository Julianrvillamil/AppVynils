package com.misw.appvynills.model

import org.json.JSONArray
import java.io.Serializable

data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    /*val tracks: JSONArray,
    val performers: JSONArray,
    val comments: JSONArray*/
    val tracks: List<Track>,
    val performers: List<Performer>,
    val comments: List<Comment>
) : Serializable

data class Track(
    val id: Int,
    val name: String,
    val duration: String
) : Serializable

data class Performer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String
) : Serializable

data class Comment(
    val id: Int,
    val description: String,
    val rating: Int
) : Serializable

