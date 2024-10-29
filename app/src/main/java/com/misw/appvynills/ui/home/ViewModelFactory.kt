package com.misw.appvynills.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.misw.appvynills.brokers.AlbumRepository

class ViewModelFactory(private val albumRepository: AlbumRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(albumRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
