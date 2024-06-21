package com.example.catapult.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.catapult.users.UserProfile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.catapult.auth.UserProfileSerializer
import javax.inject.Singleton

@Module // obezbedjuje DI
@InstallIn(SingletonComponent::class)
object AuthStoreModule {

    @Provides // zbog builder pattern-a
    @Singleton
    fun provideAuthDataStore(
        @ApplicationContext context: Context, // globalni kontekst aplikacije
    ): DataStore<UserProfile> =
        DataStoreFactory.create(
            produceFile = { context.dataStoreFile("users.txt") },
            serializer = UserProfileSerializer,
        )

}