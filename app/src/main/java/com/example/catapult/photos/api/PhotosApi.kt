package com.example.catapult.photos.api

import com.example.catapult.details.api.model.PhotoApiModel
import com.example.catapult.photos.api.model.AlbumApiModel
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotosApi {


    @GET("images/search")
    suspend fun getAlbums(
        @Query("breed_ids") breedId: String,
    ): List<AlbumApiModel>

}
