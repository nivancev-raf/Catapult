package com.example.catapult.breeds.repository

import com.example.catapult.breeds.api.BreedsApi
import com.example.catapult.breeds.api.model.BreedApiModel
import com.example.catapult.breeds.mappers.asBreedDbModel
import com.example.catapult.db.AppDatabase
import com.example.catapult.details.api.model.PhotoApiModel
import java.io.IOException
import javax.inject.Inject


// The object keyword is used to define a singleton class in Kotlin. A singleton is a class that can have only one instance.
class BreedsRepository @Inject constructor(
    private val breedsApi: BreedsApi,
    private val database: AppDatabase
    ) {

//    private val breedsApi: BreedsApi = retrofit.create(BreedsApi::class.java)

//    The use of the suspend keyword means that this function can be called from a coroutine
//    and will suspend execution until the network call completes, without blocking the thread on which it's running.
//

    suspend fun fetchAllBreeds() {
        val breeds = breedsApi.getAllBreeds()
        database.breedsDao().insertAll(list = breeds.map { it.asBreedDbModel() })
    }

    fun observeAllBreeds() = database.breedsDao().observeAllBreeds()

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