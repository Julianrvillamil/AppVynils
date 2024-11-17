package com.misw.appvynills.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.appvynills.models.Artist
import com.misw.appvynills.repository.ArtistRepository
import com.misw.appvynills.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListArtistViewModel(private val artistRepository: ArtistRepository) : ViewModel() {

    private val _artistResult = MutableStateFlow<DataState<List<Artist>>>(DataState.Loading)
    val artistResult: StateFlow<DataState<List<Artist>>> = _artistResult

    fun getArtists() {
        viewModelScope.launch {
            try {
                _artistResult.value = DataState.Loading
                artistRepository.getArtists().collect { dataState ->
                    _artistResult.value = dataState
                }
            } catch (e: Exception) {
                _artistResult.value = DataState.Error(e)
            }
        }
    }
}