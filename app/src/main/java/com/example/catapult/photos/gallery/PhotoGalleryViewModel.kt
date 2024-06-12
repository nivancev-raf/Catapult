package com.example.catapult.photos.gallery

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.breeds.api.model.BreedApiModel
import com.example.catapult.breeds.mappers.asBreedDbModel
import com.example.catapult.navigation.breeds_id
import com.example.catapult.photos.albums.model.AlbumUiModel
import com.example.catapult.photos.db.Album
import com.example.catapult.photos.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.catapult.photos.gallery.PhotoGalleryContract.AlbumGalleryUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotosRepository,
) : ViewModel() {

    private val breedId = savedStateHandle.breeds_id

    private val _state = MutableStateFlow(AlbumGalleryUiState())
    val state = _state.asStateFlow()

    private fun setState(reducer: AlbumGalleryUiState.() -> AlbumGalleryUiState) =
        _state.update(reducer)

    init {
        fetchAlbumPhotos()
    }


    // fetchAlbumPhotos by breedId
    private fun fetchAlbumPhotos() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    Log.d("BREEDID", breedId.toString())
                    val albums = photoRepository.getAlbums(breedId)
                    // log all albums
                    Log.d("ALBUMI", albums.toString())
                    setState { copy(photos = albums.map { it.asAlbumUiModel() }) }
                }
            } catch (error: IOException) {
                setState { copy(error = error) }
            }
        }
    }

    private fun Album.asAlbumUiModel() = AlbumUiModel(
        id = albumId,
        breedOwnerId = breedOwnerId,
        coverPhotoUrl = imageUrl
    )
}

