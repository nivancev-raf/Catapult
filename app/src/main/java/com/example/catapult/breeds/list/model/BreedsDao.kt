package com.example.catapult.breeds.list.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.catapult.breeds.db.BreedData
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedsDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    // ABORT - When a conflict occurs, the existing rows are kept and the new rows are ignored
    suspend fun insert(breedData: BreedData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // REPLACE - When a conflict occurs, the existing rows are deleted and the new rows are inserted
    suspend fun insertAll(list: List<BreedData>)

    @Query("SELECT * FROM BreedData")
    suspend fun getAll(): List<BreedData>

    @Query("SELECT * FROM BreedData")
    fun observeAllBreeds(): Flow<List<BreedData>> // flow je deo korutina i sluzi da se podaci emituju u realnom vremenu


    // getBreed by id
    @Transaction
    @Query("SELECT * FROM BreedData WHERE id = :breedId")
    suspend fun getBreed(breedId: Int): BreedData

}