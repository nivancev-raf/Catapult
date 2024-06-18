package com.example.catapult.networking.di

import com.example.catapult.leaderboard.di.LeaderboardBaseUrl
import com.example.catapult.leaderboard.di.LeaderboardRetrofit
import com.example.catapult.networking.serialization.AppJson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            /*
             * Order of okhttp interceptors is important.
             * If logging was first it would not log the custom header.
             */
            .addInterceptor {
                val updatedRequest = it.request().newBuilder()
                    .addHeader("CustomHeader", "CustomValue")
                    .build()
                it.proceed(updatedRequest)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()
    }

    @Singleton
    @Provides
    @DefaultRetrofit
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
        @BaseUrl apiUrl: String,
    ) : Retrofit {
        return Retrofit.Builder()
//            .baseUrl("https://api.thecatapi.com/v1/")
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Singleton
    @Provides
    @LeaderboardRetrofit
    fun provideLeaderboardRetrofitClient(
        okHttpClient: OkHttpClient,
        @LeaderboardBaseUrl leaderboardApiUrl: String,
    ): Retrofit {
        return Retrofit.Builder()
            // .baseUrl("https://rma.finlab.rs")
            .baseUrl(leaderboardApiUrl)
            .client(okHttpClient)
            .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
