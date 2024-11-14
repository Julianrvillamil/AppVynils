package com.misw.appvynills.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val albumRepository: AlbumRepository) : ViewModel() {

    private val _albumsLiveData = MutableLiveData<List<Album>>()
    //val albumsLiveData = MutableLiveData<List<Album>>()
    val albumsLiveData: LiveData<List<Album>> get() = _albumsLiveData

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    init {
        fetchAlbums()
    }

    public fun fetchAlbums()  {
        viewModelScope.launch(Dispatchers.Default) {

            try{
                withContext(Dispatchers.IO){
                    val albums = albumRepository.getAlbums() // Ahora esta función debería ser 'suspend'
                    _albumsLiveData.postValue(albums)

                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching albums", e)
                _eventNetworkError.postValue(true)
            }

        }


    }
}