package com.example.catapult.photos.gallery

import com.example.catapult.details.model.PhotoUiModel
import com.example.catapult.photos.albums.model.AlbumUiModel

interface PhotoGalleryContract {
    data class AlbumGalleryUiState(
        val photos: List<AlbumUiModel> = emptyList(),
    )
}
