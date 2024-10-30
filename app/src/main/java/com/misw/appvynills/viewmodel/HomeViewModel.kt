package com.misw.appvynills.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.model.Album

class HomeViewModel(private val albumRepository: AlbumRepository) : ViewModel() {

    private val _albumsLiveData = MutableLiveData<List<Album>>()
    val albumsLiveData: LiveData<List<Album>> get() = _albumsLiveData

    fun fetchAlbums()  {
        /*albumRepository.getAlbums { result ->
            result?.let {
                _albumsLiveData.value = it
            }
        }*/
        // Aquí llamamos a la función para que inicie la solicitud
        albumRepository.getAlbums()
    }
}