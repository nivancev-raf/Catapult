package com.example.catapult.breeds.di

import com.example.catapult.breeds.api.BreedsApi
import com.example.catapult.networking.di.DefaultRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BreedsModule {

    @Provides
    @Singleton
    fun provideBreedsApi(@DefaultRetrofit retrofit: Retrofit): BreedsApi = retrofit.create()
    // ovde smo dodali provideBreedsApi funkciju koja vraca instancu BreedApi interfejsa
}