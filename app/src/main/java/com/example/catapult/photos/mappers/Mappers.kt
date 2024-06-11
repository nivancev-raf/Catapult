package com.example.catapult.photos.mappers

import com.example.catapult.details.api.model.PhotoApiModel
import com.example.catapult.photos.albums.model.AlbumUiModel
import com.example.catapult.photos.api.model.AlbumApiModel
import com.example.catapult.photos.db.Album


fun AlbumApiModel.asAlbumDbModel(breedId: String): Album {
    return Album(
        albumId = this.albumId,
        breedOwnerId = breedId,
        imageUrl = this.imageUrl,
    )
}

fun Album.asAlbumUiModel() = AlbumUiModel(
    id = this.albumId,
    coverPhotoUrl = this.imageUrl
)

