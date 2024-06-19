package com.example.catapult

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.catapult.navigation.AppNavigation
import com.example.catapult.ui.theme.CatalistTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.catapult.auth.AuthStore
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var authStore: AuthStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatalistTheme {
                AppNavigation(authStore)
            }
        }
    }
}
