package com.example.catapult.photos.albums.grid

import com.example.catapult.photos.albums.model.AlbumUiModel

interface AlbumGridContract {
    data class AlbumGridUiState(
        val updating: Boolean = false,
        val albums: List<AlbumUiModel> = emptyList(),
    )
}