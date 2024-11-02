package com.misw.appvynills.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.misw.appvynills.model.Album
import com.misw.appvynills.repository.AlbumRepository

class AlbumDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AlbumRepository(application)

    // LiveData que contendrá los detalles del álbum
    private val _albumDetail = MutableLiveData<Album>()
    val albumDetail: LiveData<Album> get() = _albumDetail

    // Método para cargar los datos del álbum específico usando su ID
    fun fetchAlbumById(id: Int) {
        repository.getAlbumById(id) { album ->
            album?.let {
                _albumDetail.postValue(it)
            }
        }
    }
}
