package com.example.catapult.breeds.repository

import com.example.catapult.breeds.api.BreedsApi
import com.example.catapult.breeds.api.model.BreedApiModel
import com.example.catapult.details.api.model.PhotoApiModel
import com.example.catapult.networking.retrofit
import java.io.IOException



// The object keyword is used to define a singleton class in Kotlin. A singleton is a class that can have only one instance.
object BreedsRepository {

    private val breedsApi: BreedsApi = retrofit.create(BreedsApi::class.java)

//    The use of the suspend keyword means that this function can be called from a coroutine
//    and will suspend execution until the network call completes, without blocking the thread on which it's running.
//

    suspend fun fetchAllBreeds(): List<BreedApiModel> {
        return breedsApi.getAllBreeds()
    }

    suspend fun fetchBreedById(breedId: String) : BreedApiModel {
        try {
            return breedsApi.getBreed(breedId = breedId)
        } catch (error: IOException) {
            throw error
        }
    }

    suspend fun fetchBreedImage(breedImageId: String) : PhotoApiModel {
        try {
            return breedsApi.getBreedImage(breedImageId = breedImageId)
        } catch (error: IOException) {
            throw error
        }
    }


}