package com.example.catapult.photos.repository

import androidx.room.withTransaction
import com.example.catapult.db.AppDatabase
import com.example.catapult.photos.albums.model.AlbumUiModel
import com.example.catapult.photos.api.PhotosApi
import com.example.catapult.photos.db.Album
import com.example.catapult.photos.mappers.asAlbumDbModel
import com.example.catapult.photos.mappers.asAlbumUiModel
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

        // Updating coverUrl for each album
//        allAlbums.forEachIndexed { index, album ->
//            val albumPhotos = photosApi.getAlbumPhotos(albumId = album.albumId)
//            album.imageUrl = albumPhotos.firstOrNull()?.url
//            allAlbums[index] = album
//        }

        // Persisting albums in the database
        database.withTransaction {
            database.albumDao().upsertAllAlbums(data = allAlbums)
        }
    }

    fun observeBreedAlbums(breedId: String): Flow<List<Album>> {
        return database.albumDao().observeBreedAlbums(breedId = breedId)
    }

//    fun observeAlbumPhotos(albumId: String): Flow<List<Album>> {
//        return database.albumDao().observeAlbumPhotos(albumId = albumId)
//            .map { it.map { album -> album.asAlbumDbModel() } }
//    }


//    suspend fun deleteAlbum(albumId: Int) {
//        photosApi.deleteAlbum(albumId = albumId)
//        database.albumDao().deleteAlbum(albumId = albumId)
//    }
}
