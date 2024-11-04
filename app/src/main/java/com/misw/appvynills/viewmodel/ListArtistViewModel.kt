package com.misw.appvynills.viewmodel

import com.misw.appvynills.repository.ArtistRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.appvynills.model.Artist
import com.misw.appvynills.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListArtistViewModel(private val artistRepository: ArtistRepository): ViewModel() {

    private val _artistResult: MutableStateFlow<DataState<List<Artist>>?> = MutableStateFlow(null)

    val artistResult: StateFlow<DataState<List<Artist>>?>
        get() = _artistResult

    fun getArtists() {
        viewModelScope.launch {
            _artistResult.value = DataState.Loading
            _artistResult.value = artistRepository.getArtists()
        }
    }

}