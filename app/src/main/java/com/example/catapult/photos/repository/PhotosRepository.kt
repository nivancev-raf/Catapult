package com.example.catapult.photos.repository

import androidx.room.withTransaction
import com.example.catapult.db.AppDatabase
import com.example.catapult.photos.albums.model.AlbumUiModel
import com.example.catapult.photos.api.PhotosApi
import com.example.catapult.photos.db.Album
import com.example.catapult.photos.mappers.asAlbumDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PhotosRepository @Inject constructor(
    private val photosApi: PhotosApi,
    private val database: AppDatabase
) {

    // Fetching albums for a specific breed
    suspend fun fetchBreedAlbums(breedId: String) {
        val allAlbums = photosApi.getAlbums(breedId = breedId)
            .map { it.asAlbumDbModel(breedId = breedId) }
            .toMutableList()

        // Persisting albums in the database
        database.withTransaction {
            database.albumDao().upsertAllAlbums(data = allAlbums)
        }
    }

    fun observeBreedAlbums(breedId: String): Flow<List<Album>> {
        return database.albumDao().observeBreedAlbums(breedId = breedId)
    }

    suspend fun getAlbums(breedId: String): List<Album> {
        return database.albumDao().getAlbumsByBreedId(breedId = breedId)
    }

    suspend fun getRandomBreedOwnerId(): String {
        return database.albumDao().getRandomBreedOwnerId()
    }

    suspend fun getRandomAlbumByBreedOwnerId(breedOwnerId: String): Album {
        return database.albumDao().getRandomAlbumByBreedOwnerId(breedOwnerId)
    }

//    fun observeAlbumPhotos(albumId: String): Flow<List<Album>> {
//        return database.albumDao().observeAlbumPhotos(albumId = albumId)
//            .map { it.map { album -> album.asAlbumDbModel() } }
//    }

//    fun observeAlbumPhotos(albumId: String): Flow<List<Album>> {
//        return database.albumDao().observeAlbumPhotos(albumId = albumId)
//    }


//    suspend fun deleteAlbum(albumId: Int) {
//        photosApi.deleteAlbum(albumId = albumId)
//        database.albumDao().deleteAlbum(albumId = albumId)
//    }
}
