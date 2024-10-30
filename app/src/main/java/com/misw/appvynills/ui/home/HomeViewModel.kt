package com.misw.appvynills.ui.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misw.appvynills.brokers.AlbumRepository
import com.misw.appvynills.model.Album

class HomeViewModel(private val albumRepository: AlbumRepository) : ViewModel() {

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text*/

    //private val albumRepository: AlbumRepository = AlbumRepository(application)

    private val _albumsLiveData = MutableLiveData<List<Album>>()
    val albumsLiveData: LiveData<List<Album>> get() = _albumsLiveData

    fun fetchAlbums() {
        albumRepository.getAlbums()
    }
}