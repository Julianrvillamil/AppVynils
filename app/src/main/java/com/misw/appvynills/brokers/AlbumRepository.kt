package com.misw.appvynills.brokers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Response
import com.android.volley.VolleyError
import com.misw.appvynills.model.Album
import org.json.JSONArray
import org.json.JSONException

class AlbumRepository(private val context: Context) {

    private val albumsLiveData = MutableLiveData<List<Album>>()
    private val volleyBroker = VolleyBroker(context)

    fun getAlbums():LiveData<List<Album>> {
        val urlPath = "albums"
        val request = VolleyBroker.getRequest(
            path = urlPath,
            responseListener = { response ->
                try {
                    val albumList = mutableListOf<Album>()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val albumJson = jsonArray.getJSONObject(i)
                        val album = Album(
                            id = albumJson.getInt("id"),
                            name = albumJson.getString("name"),
                            genre = albumJson.getString("genre"),
                            cover = albumJson.getString("cover")
                        )
                        albumList.add(album)
                    }
                    albumsLiveData.postValue(albumList)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    albumsLiveData.postValue(emptyList())  // En caso de error, pasa una lista vacía
                    Log.e("AlbumRepository", "Error parsing JSON: ${e.message}")
                }
            },
            errorListener = { error: VolleyError ->
                error.printStackTrace()
                albumsLiveData.postValue(emptyList())
            }
        )
        volleyBroker.instance.add(request)
        return albumsLiveData
    }

}