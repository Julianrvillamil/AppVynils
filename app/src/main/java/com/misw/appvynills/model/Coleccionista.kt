package com.misw.appvynills.model

import org.json.JSONArray
import org.json.JSONObject

import java.io.Serializable

// Modelo de Collector
data class Collector(
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val comments: List<CollectorComment>,
    val favoritePerformers: List<CollectorPerformer>,
    val collectorAlbums: List<CollectorAlbum>
) : Serializable

// Modelo de Comment
data class CollectorComment(
    val id: Int,
    val description: String,
    val rating: Int
) : Serializable

// Modelo de Performer
data class CollectorPerformer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String? = null,
    val creationDate: String? = null
) : Serializable

// Modelo de Album
data class CollectorAlbum(
    val id: Int,
    val price: Int,
    val status: String
) : Serializable
