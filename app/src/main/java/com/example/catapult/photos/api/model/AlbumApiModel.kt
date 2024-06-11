package com.example.catapult.photos.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumApiModel(
    @SerialName("id") val albumId: String,
//    val breedOwnerId: String,
    @SerialName("url") val imageUrl: String?
)

// Json:
//[
// {
//    "id": "TGuAku7fM",
//    "url": "https://cdn2.thecatapi.com/images/TGuAku7fM.jpg",
//    "width": 1920,
//    "height": 1279
// }
//]


//@Entity
//data class Album(
//    @PrimaryKey val albumId: Int,
//    val breedOwnerId: String,
//    val imageUrl: String?,
//    val imageId: String?,
//)
