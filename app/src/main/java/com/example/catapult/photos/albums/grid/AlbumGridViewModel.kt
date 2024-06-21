package com.example.catapult.photos.albums.grid

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.navigation.breeds_id
import com.example.catapult.photos.albums.model.AlbumUiModel
import com.example.catapult.photos.db.Album
import com.example.catapult.photos.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.catapult.photos.albums.grid.AlbumGridContract.AlbumGridUiState
import com.example.catapult.photos.mappers.asAlbumUiModel
import javax.inject.Inject

@HiltViewModel
class AlbumGridViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotosRepository,
) : ViewModel() {

    private val breedId: String = savedStateHandle.breeds_id

    private val _state = MutableStateFlow(AlbumGridUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: AlbumGridUiState.() -> AlbumGridUiState) = _state.update(reducer)

    init {
        fetchAlbums()
        observeAlbums()

    }

    // fetchujemo albume za odredjeni breed
    private fun fetchAlbums() {
        viewModelScope.launch {
            setState { copy(updating = true) }
            try {
                withContext(Dispatchers.IO) {
                    photoRepository.fetchBreedAlbums(breedId = breedId)
                }
            } catch (error: Exception) {
                Log.d("RMA", "Exception", error)
            }
            setState { copy(updating = false) }
        }
    }

    // okida se kada se promeni bilo koji album u bazi
    private fun observeAlbums() {
        viewModelScope.launch {
            photoRepository.observeBreedAlbums(breedId = breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(albums = it.map { it.asAlbumUiModel() }) }
                }
        }
    }

}
