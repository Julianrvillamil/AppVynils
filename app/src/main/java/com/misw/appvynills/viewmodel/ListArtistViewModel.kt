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

    private val _singleArtistResult = MutableStateFlow<DataState<Artist>>(DataState.Loading)
    val singleArtistResult: StateFlow<DataState<Artist>> = _singleArtistResult

    fun getArtists() {
        viewModelScope.launch {
            artistRepository.getArtists().collect { dataState ->
                _artistResult.value = dataState
            }
            /*try {
                _artistResult.value = DataState.Loading
                artistRepository.getArtists().collect { dataState ->
                    _artistResult.value = dataState
                }
            } catch (e: Exception) {
                _artistResult.value = DataState.Error(e)
            }*/
        }
    }

    fun getArtistById(id: Int) {
        viewModelScope.launch {

            artistRepository.getArtistById(id).collect { dataState ->
                _singleArtistResult.value = dataState
            }
            /*try {
                _singleArtistResult.value = DataState.Loading
                artistRepository.getArtistById(id).collect { dataState ->
                    _singleArtistResult.value = dataState
                }
            } catch (e: Exception) {
                _singleArtistResult.value = DataState.Error(e)
            }*/
        }
    }
}