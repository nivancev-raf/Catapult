package com.example.catapult.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.catapult.breeds.db.BreedData
import com.example.catapult.breeds.db.Converters
import com.example.catapult.breeds.list.model.BreedsDao
import com.example.catapult.photos.db.Album
import com.example.catapult.photos.db.AlbumDao
import com.example.catapult.quiz.db.QuizResultEntity
import com.example.catapult.quiz.model.QuizResultDao


@Database(
    entities = [
        BreedData::class,
        Album::class,
        QuizResultEntity::class
    ],
    version = 4,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breedsDao(): BreedsDao

    abstract fun albumDao() : AlbumDao

    abstract fun quizResultDao() : QuizResultDao
}
