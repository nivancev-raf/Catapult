package com.example.catapult.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catapult.breeds.db.BreedData
import com.example.catapult.breeds.list.model.BreedsDao
import com.example.catapult.photos.db.Album
import com.example.catapult.photos.db.AlbumDao


@Database(
    entities = [
        BreedData::class,
        Album::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breedsDao(): BreedsDao

    abstract fun albumDao() : AlbumDao
}
