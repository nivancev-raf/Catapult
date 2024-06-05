package com.example.catapult.details.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PhotoApiModel(
    val id: String,
    val url: String
//    val breeds: List<BreedApiModel>,
//    val width: Int,
//    val height: Int
)
