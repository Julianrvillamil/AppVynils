package com.misw.appvynills.repository

import android.content.Context
import android.util.Log

import androidx.lifecycle.MutableLiveData
import com.misw.appvynills.brokers.VolleyBroker
import com.misw.appvynills.model.Album
import org.json.JSONArray

class AlbumRepository(private val context: Context) {

    private val volleyBroker = VolleyBroker(context)

    fun getAlbums(callback: (List<Album>?) -> Unit) {
        val path = "albums" // Agrega el endpoint sin la base URL

        val request = VolleyBroker.getRequest(
            path,
            responseListener =  { response ->
                Log.d("AlbumRepository", "Verificando response albums como String: $response")
                // Convierte el String response a JSONArray
                val jsonArray = JSONArray(response)
                Log.d("AlbumRepository", "Verificando response albums como JSONArray : $jsonArray")
                val albumList = parseAlbums(jsonArray)
                Log.d("AlbumRepository", "Verificando response albums como albumList : $albumList")
                callback(albumList)
            },
            errorListener =  { error ->
                error.printStackTrace()
                callback(null)
            }
        )

        volleyBroker.instance.add(request)
    }

    private fun parseAlbums(response: JSONArray): List<Album> {
        val albumList = mutableListOf<Album>()
        for (i in 0 until response.length()) {
            val albumJson = response.getJSONObject(i)
            val album = Album(
                id = albumJson.getInt("id"),
                name = albumJson.getString("name"),
                genre = albumJson.getString("genre"),
                cover = albumJson.getString("cover"),
                releaseDate = albumJson.getString("releaseDate"),
                description = albumJson.getString("description"),
                recordLabel = albumJson.getString("recordLabel"),
                tracks = albumJson.getJSONArray("tracks"),
                performers = albumJson.getJSONArray("performers"),
                comments = albumJson.getJSONArray("comments")
            )
            albumList.add(album)
        }
        return albumList
    }

}