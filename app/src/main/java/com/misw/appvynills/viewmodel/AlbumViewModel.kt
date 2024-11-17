package com.misw.appvynills.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.models.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumViewModel(private val albumRepository: AlbumRepository) : ViewModel() {

    private val _albumsLiveData = MutableLiveData<List<Album>>()
    //val albumsLiveData = MutableLiveData<List<Album>>()
    val albumsLiveData: LiveData<List<Album>> get() = _albumsLiveData

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    sealed class UiState {
        object Loading : UiState()
        data class Success(val albums: List<Album>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        _albumsLiveData.value = emptyList()
        _isLoading.value = false
        _error.value = null
    }

    fun fetchAlbums()  {
        viewModelScope.launch {

            try{
                _isLoading.value = true
                //_uiState.value = UiState.Loading
                _error.value = null
                Log.d("Debug", "fetchAlbums llamado")
                val result = withContext(Dispatchers.IO) {
                        albumRepository.getAlbums()
                }
                result.fold(
                    onSuccess = { albums ->
                        _albumsLiveData.value = albums
                    },
                    onFailure = { exception ->
                        Log.e("AlbumViewModel", "Error al obtener álbumes", exception)
                        _error.value = exception.message ?: "Error desconocido"
                    }
                )
            }catch (e: Exception) {
                Log.e("AlbumViewModel", "Error fetching albums", e)
                _error.value = e.message ?: "Error desconocido"

            }finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }

        }


    }
}