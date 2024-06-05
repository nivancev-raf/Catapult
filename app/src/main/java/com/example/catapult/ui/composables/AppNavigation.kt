package com.example.catapult.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.catapult.breeds.list.cats
import com.example.catapult.details.breedDetails


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost( //
        navController = navController,
        startDestination = "breeds"
    ) {
        cats(
            route = "breeds",
            onBreedClick = {
                navController.navigate(route = "breeds/$it")
            }
        )

        breedDetails(
            route = "breeds/{Id}",
            onClose = {
                navController.navigateUp()
            }
        )
    }



}