package com.example.catapult.networking.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object ProdNetworkingModule {

    @BaseUrl
    @Provides // This annotation is used to tell Dagger how to provide instances of a type.
    fun provideProdUrl(): String = "https://api.thecatapi.com/v1/"
}