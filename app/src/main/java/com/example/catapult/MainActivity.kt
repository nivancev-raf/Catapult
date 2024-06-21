package com.example.catapult

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.catapult.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import com.example.catapult.auth.AuthStore
import com.example.catapult.ui.theme.CatapultTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authStore: AuthStore // lateinit -> late initialization, dohvati mi ovo samo kad mi zatreba


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatapultTheme {
                AppNavigation(authStore)
            }
        }
    }
}
