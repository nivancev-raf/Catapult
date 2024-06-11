package com.example.catapult.photos.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Album(
    @PrimaryKey val albumId: String,
    val breedOwnerId: String,
    val imageUrl: String?,
)
