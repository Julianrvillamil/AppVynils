package com.misw.appvynills.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import com.misw.appvynills.brokers.VolleyBroker
import com.misw.appvynills.model.Album
import org.json.JSONArray
import org.json.JSONException

class AlbumRepository(private val context: Context) {

    private val albumsLiveData = MutableLiveData<List<Album>>()
    private val volleyBroker = VolleyBroker(context)

    fun getAlbums():LiveData<List<Album>> {
        val urlPath = "albums"
        Log.d("AlbumRepository", "Fetching albums from urlpath: $urlPath")
        val request = VolleyBroker.getRequest(
            path = urlPath,
            responseListener = { response ->
                try {
                    Log.d("AlbumRepository", "Verificando response albums: $response")
                    val albumList = mutableListOf<Album>()
                    val jsonArray = JSONArray(response)
                    Log.d("AlbumRepository", "Response received: $response")
                    for (i in 0 until jsonArray.length()) {
                        val albumJson = jsonArray.getJSONObject(i)

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
                    Log.d("AlbumRepository", "Albums parsed successfully: $albumList")
                    albumsLiveData.postValue(albumList)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    albumsLiveData.postValue(emptyList())  // En caso de error, pasa una lista vacía
                    Log.e("AlbumRepository", "Error parsing JSON: ${e.message}")
                }
            },
            errorListener = { error: VolleyError ->
                Log.e("AlbumRepository", "Volley Error: ${error.message}")
                error.printStackTrace()
                albumsLiveData.postValue(emptyList())
            }
        )
        volleyBroker.instance.add(request)
        return albumsLiveData
    }

}