package com.example.catapult

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.catapult.navigation.AppNavigation
import com.example.catapult.ui.theme.CatalistTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatalistTheme {
                AppNavigation()
            }
        }
    }
}
