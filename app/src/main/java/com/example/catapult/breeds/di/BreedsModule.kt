package com.example.catapult.breeds.di

import com.example.catapult.breeds.api.BreedsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsersModule {

    @Provides
    @Singleton
    fun provideUsersApi(retrofit: Retrofit): BreedsApi = retrofit.create()
    // ovde smo dodali provideUsersApi funkciju koja vraca instancu UsersApi interfejsa
}