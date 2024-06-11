package com.example.catapult.photos.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.navigation.albumId
import com.example.catapult.photos.albums.model.AlbumUiModel
import com.example.catapult.photos.db.Album
import com.example.catapult.photos.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.catapult.photos.gallery.PhotoGalleryContract.AlbumGalleryUiState
import javax.inject.Inject

@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotosRepository,
) : ViewModel() {

    private val albumId = savedStateHandle.albumId

    private val _state = MutableStateFlow(AlbumGalleryUiState())
    val state = _state.asStateFlow()

    private fun setState(reducer: AlbumGalleryUiState.() -> AlbumGalleryUiState) =
        _state.update(reducer)

    init {
        observeAlbumPhotos()
    }

    private fun observeAlbumPhotos() {
        viewModelScope.launch {
            photoRepository.observeAlbumPhotos(albumId = albumId)
                .distinctUntilChanged()
                .collect { photos ->
                    setState { copy(photos = photos.map { it.asAlbumUiModel() }) }
                }
        }
    }

    private fun Album.asAlbumUiModel() = AlbumUiModel(
        id = albumId,
        breedOwnerId = breedOwnerId,
        coverPhotoUrl = imageUrl
    )
}
