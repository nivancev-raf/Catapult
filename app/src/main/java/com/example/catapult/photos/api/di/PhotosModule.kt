package com.example.catapult.photos.api.di

import com.example.catapult.photos.api.PhotosApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhotosModule {

    @Provides
    @Singleton
    fun providePhotosApi(retrofit: Retrofit): PhotosApi = retrofit.create()
}
