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
                    albumRepository.getAlbumDetails(albumId)
                }
                if (album != null){
                    _albumDetail.postValue(album)
                } else {
                    _error.postValue("No se logro cargar los detalles del album :C")
                }
            } catch (e: Exception) {
                Log.e("AlbumDetailViewModel", "Error obteniendo detalles del álbum", e)
                _error.postValue(e.message ?: "Error desconocido")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}