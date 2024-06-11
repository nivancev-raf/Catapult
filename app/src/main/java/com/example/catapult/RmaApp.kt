package com.example.catapult


import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RmaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize things if needed
    }
}