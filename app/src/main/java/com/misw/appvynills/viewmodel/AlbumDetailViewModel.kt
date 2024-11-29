package com.misw.appvynills.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.appvynills.models.Album
import com.misw.appvynills.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailViewModel (private val albumRepository: AlbumRepository) : ViewModel() {

    private val _albumDetail = MutableLiveData<Album?>()
    val albumDetail: LiveData<Album?> get() = _albumDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchAlbumDetails(albumId: Int) {
        Log.d("AlbumDetailViewModel", "Obteniendo detalles del álbum para ID: $albumId")

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val album = withContext(Dispatchers.IO) {
                    // Obtén los datos del servidor primero
                    val updatedAlbum = albumRepository.getAlbumDetails(albumId)
                    // Guarda los datos en la base de datos local
                    if (updatedAlbum != null) {
                        albumRepository.saveAlbumsToDatabase(listOf(updatedAlbum))
                    }
                    // Devuelve los datos más recientes desde la base de datos local
                    albumRepository.getAlbumDetails(albumId)
                }
                _albumDetail.postValue(album)
                _albumDetail.postValue(_albumDetail.value?.copy())
                _error.value = null
            } catch (e: Exception) {
                Log.e("AlbumDetailViewModel", "Error obteniendo detalles del álbum", e)
                _error.postValue(e.message ?: "Error desconocido")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}