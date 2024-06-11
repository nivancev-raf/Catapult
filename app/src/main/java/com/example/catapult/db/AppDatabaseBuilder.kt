package com.example.catapult.db

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppDatabaseBuilder @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun build(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "catapult.db",
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
// Path: app/src/main/java/com/example/catapult/db/AppDatabase.kt