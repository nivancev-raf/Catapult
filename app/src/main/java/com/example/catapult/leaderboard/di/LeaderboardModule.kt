package com.example.catapult.leaderboard.di

import com.example.catapult.breeds.api.BreedsApi
import com.example.catapult.leaderboard.network.LeaderboardApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LeaderboardModule {

    @Provides
    @Singleton
    fun provideLeaderboardApi(@LeaderboardRetrofit retrofit: Retrofit): LeaderboardApi = retrofit.create()
    // ovde smo dodali provideLeaderboardApi funkciju koja vraca instancu LeaderboardApi interfejsa
}
