package com.misw.appvynills.repository

import android.content.Context
import android.util.Log
import com.misw.appvynills.brokers.VolleyBroker
import com.misw.appvynills.database.VinylRoomDatabase
import com.misw.appvynills.database.entity.toDomainModel
import com.misw.appvynills.database.entity.toEntityModel
import com.misw.appvynills.model.Collector
import com.misw.appvynills.model.CollectorAlbum
import com.misw.appvynills.model.CollectorComment
import com.misw.appvynills.model.CollectorPerformer
import com.misw.appvynills.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CollectorRepository(private val context: Context) {

    private val volleyBroker = VolleyBroker(context)
    private val collectorDao = VinylRoomDatabase.getDatabase(context).collectorDao()

    suspend fun getCollectors(): Result<List<Collector>> = withContext(Dispatchers.IO) {
        try {
            // Intenta obtener datos desde la API
            val remoteCollectors = getCollectorsFromNetwork()
            saveCollectorsToDatabase(remoteCollectors)
            Result.success(remoteCollectors)
        } catch (e: Exception) {
            Log.e("CollectorRepository", "Error al obtener coleccionistas desde la API: ${e.message}", e)
            try {
                // Si falla, intenta obtener datos locales
                val localCollectors = collectorDao.getAllCollectors().map { collectorWithRelations ->
                    collectorWithRelations.toDomainModel()
                }

                if (localCollectors.isNotEmpty()) {
                    Result.success(localCollectors)
                } else {
                    Result.failure(Exception("No hay datos disponibles en la API ni en la base de datos local"))
                }
            } catch (dbError: Exception) {
                Log.e("CollectorRepository", "Error al obtener coleccionistas locales: ${dbError.message}", dbError)
                Result.failure(dbError)
            }
        } as Result<List<Collector>>
    }

    suspend fun getCollectorDetails(collectorId: Int): Result<Collector> = withContext(Dispatchers.IO) {
        try {
            // Intenta obtener los detalles desde la API
            val collectorDetails = getCollectorDetailsFromNetwork(collectorId)
            saveCollectorDetailsToDatabase(collectorDetails) // Guarda en la base de datos local
            Result.success(collectorDetails)
        } catch (e: Exception) {
            Log.e("CollectorRepository", "Error al obtener detalles del coleccionista desde la API: ${e.message}", e)
            try {
                // Si falla, intenta obtener datos locales
                val localCollector = collectorDao.getCollectorById(collectorId)?.toDomainModel()
                if (localCollector != null) {
                    Result.success(localCollector)
                } else {
                    Result.failure(Exception("No hay detalles disponibles en la API ni en la base de datos local"))
                }
            } catch (dbError: Exception) {
                Log.e("CollectorRepository", "Error al obtener detalles del coleccionista local: ${dbError.message}", dbError)
                Result.failure(dbError)
            }
        }
    }


    private suspend fun getCollectorsFromNetwork(): List<Collector> = suspendCoroutine { cont ->
        Log.d("CollectionRepository", "Iniciando solicitud de Collecciones desde la red...")
        val request = VolleyBroker.getRequest(
            "collectors",
            responseListener =  { response ->
                try {
                    Log.d("CollectorRepository", "Verificando response collectors como String: $response")
                    // Convierte el String response a JSONArray
                    val jsonArray = JSONArray(response)
                    Log.d("CollectorRepository", "Verificando response collectors como JSONArray: $jsonArray")
                    val collectorList = parseCollectors(jsonArray)
                    Log.d("CollectorRepository", "Verificando response collectors como collectorList: $collectorList")
                    cont.resume(collectorList)
                }catch (e: Exception){
                    Log.e("CollectionRepository", "Error procesando la respuesta", e)
                    cont.resumeWithException(e) // Retorna la excepción en caso de error de parsing
                }

            },
            errorListener =  { error ->
                Log.e("CollectionRepository", "Error en la petición de red", error)
                error.printStackTrace()
                cont.resumeWithException(error) // Retorna la excepción en caso de error de red
            }
        )

        volleyBroker.instance.add(request)
    }

    private suspend fun saveCollectorsToDatabase(collectors: List<Collector>) {
        collectorDao.deleteAllCollectors()
        collectorDao.deleteAllCollectorComments()
        collectorDao.deleteAllCollectorPerformers()
        collectorDao.deleteAllCollectorAlbums()

        collectors.forEach { collector ->
            collectorDao.insertCollectors(listOf(collector.toEntityModel()))
            collectorDao.insertCollectorComments(collector.comments.map { it.toEntityModel(collector.id) })
            collectorDao.insertCollectorPerformers(collector.favoritePerformers.map { it.toEntityModel(collector.id) })
            collectorDao.insertCollectorAlbums(collector.collectorAlbums.map { it.toEntityModel(collector.id) })
        }
    }

    /**
     * Guarda los detalles de un coleccionista en la base de datos.
     */
    private suspend fun saveCollectorDetailsToDatabase(collector: Collector) {
        collectorDao.insertCollectors(listOf(collector.toEntityModel()))
        collectorDao.insertCollectorComments(collector.comments.map { it.toEntityModel(collector.id) })
        collectorDao.insertCollectorPerformers(collector.favoritePerformers.map { it.toEntityModel(collector.id) })
        collectorDao.insertCollectorAlbums(collector.collectorAlbums.map { it.toEntityModel(collector.id) })
    }


    private suspend fun getCollectorDetailsFromNetwork(collectorId: Int): Collector = suspendCoroutine { cont ->
        val path = "collectors/$collectorId" // Endpoint específico del coleccionista

        val request = VolleyBroker.getRequest(
            path,
            responseListener = { response ->
                Log.d("CollectorRepository", "getCollectorDetails -> Verificando response collectors como String: $response")
                val collectorJson = JSONObject(response)
                val collectorDetails = parseCollector(collectorJson)
                Log.d("CollectorRepository", "getCollectorDetails -> Verificando detalles del coleccionista: $collectorDetails")
                cont.resume(collectorDetails)
            },
            errorListener = { error ->
                cont.resumeWithException(error)
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
