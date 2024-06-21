package com.example.catapult.breeds.list.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.catapult.breeds.db.BreedData
import kotlinx.coroutines.flow.Flow

@Dao // Data Access Object
interface BreedsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // REPLACE - When a conflict occurs, the existing rows are deleted and the new rows are inserted
    suspend fun insertAll(list: List<BreedData>)


    @Query("SELECT * FROM BreedData")
    fun observeAllBreeds(): Flow<List<BreedData>> // flow je deo korutina i sluzi da se podaci emituju u realnom vremenu


    // get 3 random breeds for false answers in quiz
    @Query("SELECT * FROM BreedData WHERE id != :breedId ORDER BY RANDOM() LIMIT 3")
    suspend fun getRandomBreedsExcluding(breedId: String): List<BreedData>

    // getBreed by id
    @Query("SELECT * FROM BreedData WHERE id = :breedId")
    suspend fun getBreed(breedId: String): BreedData

    // get 3 random temperaments for false answers in quiz
    @Query("SELECT DISTINCT temperament FROM BreedData WHERE temperament NOT LIKE '%' || :temperament || '%' ORDER BY RANDOM() LIMIT 2") // [0,1,2] -> 3 elementa
    suspend fun getRandomTemperamentsExcluding(temperament: String): List<String>

    /*
    * objasnjenje za query:
    * SELECT DISTINCT temperament FROM BreedData WHERE temperament NOT LIKE '%' || :temperament || '%' ORDER BY RANDOM() LIMIT 2
    *
    * DISTINCT -> vraca samo unique vrednosti
    * temperament -> kolona iz koje uzimamo podatke
    * || -> operator za konkatenaciju stringova
    * dakle selektuje sve unique vrednosti iz kolone temperament gde vrednost nije jednaka vrednosti koja je prosledjena kao argument
    *
    * */


    // vraca random temperament koji nije u listi prosledjenih temperamenata
    @Query("SELECT DISTINCT temperament FROM BreedData WHERE temperament NOT IN (:temperaments) ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomTemperamentExcluding(temperaments: List<String>): String?
}