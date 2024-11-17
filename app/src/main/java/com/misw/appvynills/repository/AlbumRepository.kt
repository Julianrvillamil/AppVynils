package com.misw.appvynills.repository

import android.content.Context
import android.util.Log
import com.bumptech.glide.load.engine.Resource

import com.misw.appvynills.brokers.VolleyBroker
import com.misw.appvynills.database.VinylRoomDatabase
import com.misw.appvynills.database.entity.AlbumPerformerCrossRef
import com.misw.appvynills.database.entity.toDomainModel
import com.misw.appvynills.database.entity.toEntityModels
import com.misw.appvynills.models.Album
import com.misw.appvynills.models.Comment
import com.misw.appvynills.models.Performer
import com.misw.appvynills.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AlbumRepository(private val context: Context) {

    private val volleyBroker = VolleyBroker(context)
    private val albumDao = VinylRoomDatabase.getDatabase(context).albumDao()

    suspend fun getAlbums(): Result<List<Album>> = withContext(Dispatchers.IO) {
        try {
            // Intenta obtener datos de la red primero
            val remoteAlbums = getAlbumsFromNetwork()
            saveAlbumsToDatabase(remoteAlbums)
            Result.success(remoteAlbums)
        } catch (e: Exception) {
            // Si hay error en la red, usa datos locales si existen
            try {
                val localAlbums = albumDao.getAllAlbumsWithRelations().map { albumWithRelations ->
                    Album(
                        id = albumWithRelations.album.id,
                        name = albumWithRelations.album.name,
                        genre = albumWithRelations.album.genre,
                        cover = albumWithRelations.album.cover,
                        releaseDate = albumWithRelations.album.releaseDate,
                        description = albumWithRelations.album.description,
                        recordLabel = albumWithRelations.album.recordLabel,
                        tracks = albumWithRelations.tracks.map { trackEntity ->
                            Track(
                                id = trackEntity.id,
                                name = trackEntity.name,
                                duration = trackEntity.duration
                            )
                        },
                        performers = albumWithRelations.performers.map { performerEntity ->
                            Performer(
                                id = performerEntity.id,
                                name = performerEntity.name,
                                image = performerEntity.image,
                                description = performerEntity.description,
                                birthDate = performerEntity.birthDate
                            )
                        },
                        comments = albumWithRelations.comments.map { commentEntity ->
                            Comment(
                                id = commentEntity.id,
                                description = commentEntity.description,
                                rating = commentEntity.rating
                            )
                        }
                    )
                }

                if (localAlbums.isNotEmpty()) {
                    Result.success(localAlbums)
                } else {
                    Result.failure(Exception("No hay datos disponibles"))
                }
            } catch (dbError: Exception) {
                Result.failure(dbError)
            }
        }
    }

    private suspend fun getAlbumsFromNetwork(): List<Album> = suspendCoroutine { cont ->
        Log.d("AlbumRepository", "Iniciando solicitud de álbumes desde la red...")
        val request = VolleyBroker.getRequest(
            "albums",
            responseListener = { response ->
                try {
                    Log.d("AlbumRepository", "Respuesta recibida: $response")
                    Log.d("AlbumRepository", "Verificando response albums como String: $response")
                    // Convierte el String response a JSONArray
                    val jsonArray = JSONArray(response)
                    Log.d("AlbumRepository", "Verificando response albums como JSONArray : $jsonArray")
                    val albumList = parseAlbums(jsonArray)
                    Log.d("AlbumRepository", "Verificando response albums como albumList : $albumList")
                    cont.resume(albumList) // Retorna la lista de álbumes
                } catch (e: Exception) {
                    Log.e("AlbumRepository", "Error procesando la respuesta", e)
                    cont.resumeWithException(e) // Retorna la excepción en caso de error de parsing
                }
            },
            errorListener = { error ->
                Log.e("AlbumRepository", "Error en la petición de red", error)
                error.printStackTrace()
                cont.resumeWithException(error) // Retorna la excepción en caso de error de red
            }
        )
        volleyBroker.instance.add(request)
    }

    private suspend fun saveAlbumsToDatabase(albums: List<Album>) {
        albumDao.deleteEverything()

        albums.forEach { album ->
            val relations = album.toEntityModels()
            albumDao.insertAlbum(relations.album)
            albumDao.insertTracks(relations.tracks)
            albumDao.insertPerformers(relations.performers)
            albumDao.insertComments(relations.comments)
            albumDao.insertAlbumPerformerCrossRefs(
                relations.performers.map { performer ->
                    AlbumPerformerCrossRef(album.id, performer.id)
                }
            )
        }
    }


    suspend fun getAlbumDetails(albumId: Int): Album? = withContext(Dispatchers.IO){

        try {
            val albumWithRelations = albumDao.getAlbumWithRelations(albumId)
            albumWithRelations?.toDomainModel()
        } catch (e: Exception) {
            Log.e("AlbumRepository", "Error obteniendo detalles del álbum desde la base de datos", e)
            null
        }
        /*try{
            // Primero, intenta obtener el álbum desde la base de datos local
            val localAlbum = albumDao.getAlbumWithRelations(albumId)?.let { albumWithRelations ->
                Album(
                    id = albumWithRelations.album.id,
                    name = albumWithRelations.album.name,
                    genre = albumWithRelations.album.genre,
                    cover = albumWithRelations.album.cover,
                    releaseDate = albumWithRelations.album.releaseDate,
                    description = albumWithRelations.album.description,
                    recordLabel = albumWithRelations.album.recordLabel,
                    tracks = albumWithRelations.tracks.map { trackEntity ->
                        Track(
                            id = trackEntity.id,
                            name = trackEntity.name,
                            duration = trackEntity.duration
                        )
                    },
                    performers = albumWithRelations.performers.map { performerEntity ->
                        Performer(
                            id = performerEntity.id,
                            name = performerEntity.name,
                            image = performerEntity.image,
                            description = performerEntity.description,
                            birthDate = performerEntity.birthDate
                        )
                    },
                    comments = albumWithRelations.comments.map { commentEntity ->
                        Comment(
                            id = commentEntity.id,
                            description = commentEntity.description,
                            rating = commentEntity.rating
                        )
                    }
                )
            }

            // Si existe el álbum localmente, lo retornamos
            if (localAlbum != null) {
                return@withContext localAlbum
            }

            // Si no existe localmente, intenta obtenerlo de la red
            val remoteAlbum = getAlbumDetailsFromNetwork(albumId)
            remoteAlbum?.let {
                saveAlbumsToDatabase(listOf(it)) // Guarda en la base de datos local
            }
            remoteAlbum
        } catch (e: Exception) {
            Log.e("AlbumRepository", "Error obteniendo detalles del álbum", e)
            null // Retorna null en caso de error
        }*/
    }

    private suspend fun getAlbumDetailsFromNetwork(albumId: Int): Album? = suspendCoroutine { cont ->
        val path = "albums/$albumId" // Endpoint specific del álbum id

        val request = VolleyBroker.getRequest(
            path,
            responseListener = { response ->
                try {
                    Log.d("AlbumRepository", "getAlbumDetails -> Verification response albums como String: $response")
                    val albumJson = JSONObject(response)
                    val albumDetails = parseAlbum(albumJson)
                    Log.d("AlbumRepository", "getAlbumDetails 3-> Verification response albums details: $albumDetails")
                    cont.resume(albumDetails)
                }catch (e: Exception) {
                    cont.resumeWithException(e)
                }

            },
            errorListener = { error ->
                cont.resumeWithException(error)
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

                tracks = parseTracks(albumJson.getJSONArray("tracks")),
                performers = parsePerformers(albumJson.getJSONArray("performers")),
                comments = parseComments(albumJson.getJSONArray("comments"))

            /*tracks = albumJson.get("tracks"),
                performers = albumJson.getJSONArray("performers"),
                comments = albumJson.getJSONArray("comments")
                */
            )
            albumList.add(album)
        }
        return albumList
    }

    private fun parseAlbum(json: JSONObject): Album {
        return Album(
            id = json.getInt("id"),
            name = json.getString("name"),
            cover = json.getString("cover"),
            releaseDate = json.getString("releaseDate"),
            description = json.getString("description"),
            genre = json.getString("genre"),
            recordLabel = json.getString("recordLabel"),
            tracks = parseTracks(json.getJSONArray("tracks")),
            performers = parsePerformers(json.getJSONArray("performers")),
            comments = parseComments(json.getJSONArray("comments"))
        )
    }

    private fun parseTracks(tracksJson: JSONArray): List<Track> {
        val trackList = mutableListOf<Track>()
        for (i in 0 until tracksJson.length()) {
            val trackJson = tracksJson.getJSONObject(i)
            val track = Track(
                id = trackJson.getInt("id"),
                name = trackJson.getString("name"),
                duration = trackJson.getString("duration")
            )
            trackList.add(track)
        }
        return trackList
    }

    private fun parsePerformers(performersJson: JSONArray): List<Performer> {
        val performerList = mutableListOf<Performer>()
        for (i in 0 until performersJson.length()) {
            val performerJson = performersJson.getJSONObject(i)
            val performer = Performer(
                id = performerJson.getInt("id"),
                name = performerJson.getString("name"),
                image = performerJson.getString("image"),
                description = performerJson.getString("description"),
                birthDate = if (performerJson.has("birthDate")) performerJson.getString("birthDate") else ""
            )
            performerList.add(performer)
        }
        return performerList
    }

    private fun parseComments(commentsJson: JSONArray): List<Comment> {
        val commentList = mutableListOf<Comment>()
        for (i in 0 until commentsJson.length()) {
            val commentJson = commentsJson.getJSONObject(i)
            val comment = Comment(
                id = commentJson.getInt("id"),
                description = commentJson.getString("description"),
                rating = commentJson.getInt("rating")
            )
            commentList.add(comment)
        }
        return commentList
    }

}