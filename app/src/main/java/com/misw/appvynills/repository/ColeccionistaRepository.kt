package com.misw.appvynills.repository

import android.content.Context
import android.util.Log
import com.misw.appvynills.brokers.VolleyBroker
import com.misw.appvynills.model.Collector
import com.misw.appvynills.model.CollectorAlbum
import com.misw.appvynills.model.CollectorComment
import com.misw.appvynills.model.CollectorPerformer
import org.json.JSONArray
import org.json.JSONObject

class CollectorRepository(private val context: Context) {

    private val volleyBroker = VolleyBroker(context)

    fun getCollectors(callback: (List<Collector>?) -> Unit) {
        val path = "collectors" // Agrega el endpoint sin la base URL

        val request = VolleyBroker.getRequest(
            path,
            responseListener =  { response ->
                Log.d("CollectorRepository", "Verificando response collectors como String: $response")
                // Convierte el String response a JSONArray
                val jsonArray = JSONArray(response)
                Log.d("CollectorRepository", "Verificando response collectors como JSONArray: $jsonArray")
                val collectorList = parseCollectors(jsonArray)
                Log.d("CollectorRepository", "Verificando response collectors como collectorList: $collectorList")
                callback(collectorList)
            },
            errorListener =  { error ->
                error.printStackTrace()
                callback(null)
            }
        )

        volleyBroker.instance.add(request)
    }

    fun getCollectorDetails(collectorId: Int, callback: (Collector?) -> Unit) {
        val path = "collectors/$collectorId" // Endpoint específico del coleccionista

        val request = VolleyBroker.getRequest(
            path,
            responseListener = { response ->
                Log.d("CollectorRepository", "getCollectorDetails -> Verificando response collectors como String: $response")
                val collectorJson = JSONObject(response)
                val collectorDetails = parseCollector(collectorJson)
                Log.d("CollectorRepository", "getCollectorDetails -> Verificando detalles del coleccionista: $collectorDetails")
                callback(collectorDetails)
            },
            errorListener = { error ->
                error.printStackTrace()
                callback(null)
            }
        )
        volleyBroker.instance.add(request)
    }

    private fun parseCollectors(response: JSONArray): List<Collector> {
        val collectorList = mutableListOf<Collector>()
        for (i in 0 until response.length()) {
            val collectorJson = response.getJSONObject(i)
            val collector = parseCollector(collectorJson)
            collectorList.add(collector)
        }
        return collectorList
    }

    private fun parseCollector(json: JSONObject): Collector {
        return Collector(
            id = json.getInt("id"),
            name = json.getString("name"),
            telephone = json.getString("telephone"),
            email = json.getString("email"),
            comments = parseComments(json.getJSONArray("comments")),
            favoritePerformers = parsePerformers(json.getJSONArray("favoritePerformers")),
            collectorAlbums = parseAlbums(json.getJSONArray("collectorAlbums"))
        )
    }

    private fun parseComments(commentsJson: JSONArray): List<CollectorComment> {
        val commentList = mutableListOf<CollectorComment>()
        for (i in 0 until commentsJson.length()) {
            val commentJson = commentsJson.getJSONObject(i)
            val comment = CollectorComment(
                id = commentJson.getInt("id"),
                description = commentJson.getString("description"),
                rating = commentJson.getInt("rating")
            )
            commentList.add(comment)
        }
        return commentList
    }

    private fun parsePerformers(performersJson: JSONArray): List<CollectorPerformer> {
        val performerList = mutableListOf<CollectorPerformer>()
        for (i in 0 until performersJson.length()) {
            val performerJson = performersJson.getJSONObject(i)
            val performer = CollectorPerformer(
                id = performerJson.getInt("id"),
                name = performerJson.getString("name"),
                image = performerJson.getString("image"),
                description = performerJson.getString("description"),
                birthDate = performerJson.optString("birthDate", null),
                creationDate = performerJson.optString("creationDate", null)
            )
            performerList.add(performer)
        }
        return performerList
    }

    private fun parseAlbums(albumsJson: JSONArray): List<CollectorAlbum> {
        val albumList = mutableListOf<CollectorAlbum>()
        for (i in 0 until albumsJson.length()) {
            val albumJson = albumsJson.getJSONObject(i)
            val album = CollectorAlbum(
                id = albumJson.getInt("id"),
                price = albumJson.getInt("price"),
                status = albumJson.getString("status")
            )
            albumList.add(album)
        }
        return albumList
    }
}
