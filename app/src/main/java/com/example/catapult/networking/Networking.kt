//package com.example.catapult.networking
//
//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import com.example.catapult.networking.serialization.AppJson
//
///*
// * Order of okhttp interceptors is important. If logging was first,
// * it would not log the custom header.
// */
//
//val okHttpClient = OkHttpClient.Builder()
//    .addInterceptor {
//        val updatedRequest = it.request().newBuilder()
//            .addHeader("CustomHeader", "CustomValue")
//            .addHeader("x-api-key", "live_YT0c8WFot9KksIOMViwgHNVOFCjqyvttSKEpv7rXOheCdPHwtV4PCZ8nmpBssbiR")
//            .build()
//        it.proceed(updatedRequest)
//    }
//    .addInterceptor(
//        HttpLoggingInterceptor().apply {
//            setLevel(HttpLoggingInterceptor.Level.BODY)
//        }
//    )
//    .build()
//
//
//val retrofit: Retrofit = Retrofit.Builder()
//    .baseUrl("https://api.thecatapi.com/v1/")
//    .client(okHttpClient)
//    .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
//    .build()
