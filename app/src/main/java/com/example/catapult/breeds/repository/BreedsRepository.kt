package com.example.catapult.breeds.repository

import com.example.catapult.breeds.api.BreedsApi
import com.example.catapult.breeds.api.model.BreedApiModel
import com.example.catapult.breeds.db.BreedData
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

        val breedsWithImages = breeds.map { breed ->
            val imageUrl = getBreedImage(breed.reference_image_id)?.url
            breed.copy(url = imageUrl ?: "")
        }

//        database.breedsDao().insertAll(list = breeds.map { it.asBreedDbModel() })
        database.breedsDao().insertAll(breedsWithImages.map { it.asBreedDbModel() })
    }

    suspend fun getBreedImage(imageId: String?): PhotoApiModel? {
        return imageId?.let { breedsApi.getBreedImage(it) }
    }

    fun observeAllBreeds() = database.breedsDao().observeAllBreeds()


    // get breed by id, return is BreedData
    suspend fun getBreedById(breedId: String) : BreedData {
        return database.breedsDao().getBreed(breedId = breedId)
    }

    suspend fun fetchBreedImage(breedImageId: String) : PhotoApiModel {
        try {
            return breedsApi.getBreedImage(breedImageId = breedImageId)
        } catch (error: IOException) {
            throw error
        }
    }

    suspend fun getRandomBreedsExcluding(breedId: String): List<BreedData> {
        return database.breedsDao().getRandomBreedsExcluding(breedId)
    }

    // getRandomTemperamentsExcluding
    suspend fun getRandomTemperamentsExcluding(breedId: String): List<String> {
        return database.breedsDao().getRandomTemperamentsExcluding(breedId)
    }

    // In your repository or ViewModel
    suspend fun getOneRandomTemperamentExcluding(excludedTemperaments: List<String>): String? {
        return database.breedsDao().getRandomTemperamentExcluding(excludedTemperaments)
    }

    //




}