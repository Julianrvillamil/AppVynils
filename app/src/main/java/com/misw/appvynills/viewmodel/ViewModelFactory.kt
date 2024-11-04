package com.misw.appvynills.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.repository.ArtistRepository

class ViewModelFactory(
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository? = null // Parámetro opcional
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(albumRepository) as T
            }
            modelClass.isAssignableFrom(ListArtistViewModel::class.java) -> {
                ListArtistViewModel(artistRepository ?: throw IllegalArgumentException("ArtistRepository is required")) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

