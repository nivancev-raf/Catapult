package com.example.catapult.photos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

//@Dao
//interface AlbumDao {
//
//    @Upsert
//    fun upsertAllAlbums(data: List<Album>)
//
//    @Upsert
//    fun upsertAllPhotos(data: List<Photo>)
//
//    @Query("SELECT * FROM Album WHERE Album.breedOwnerId = :breedId") // selektujemo sve albume za odredjeni breed
//    fun observeBreedAlbums(breedId: String): Flow<List<Album>>
//
//    @Query("SELECT * FROM Photo WHERE Photo.albumId = :albumId") // selektujemo sve slike za odredjeni album
//    fun observeAlbumPhotos(albumId: Int): Flow<List<Photo>>
//}

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllAlbums(data: List<Album>)

    @Query("SELECT * FROM Album WHERE breedOwnerId = :breedId")
    fun observeBreedAlbums(breedId: String): Flow<List<Album>>

    // get all photos with same breedOwnerId
    @Query("SELECT * FROM Album WHERE breedOwnerId = :breedId")
    suspend fun getAlbumsByBreedId(breedId: String): List<Album>


    @Query("SELECT * FROM Album WHERE albumId = :albumId")
    fun observeAlbumPhotos(albumId: String): Flow<List<Album>>

//    @Query("DELETE FROM Album WHERE albumId = :albumId")
//    suspend fun deleteAlbum(albumId: Int)
}