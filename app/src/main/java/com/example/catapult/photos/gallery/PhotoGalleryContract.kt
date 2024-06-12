package com.example.catapult.photos.gallery

import com.example.catapult.photos.albums.model.AlbumUiModel
import java.io.IOException

interface PhotoGalleryContract {
    data class AlbumGalleryUiState(
        val photos: List<AlbumUiModel> = emptyList(),
        val error: IOException? = null
    )
}
