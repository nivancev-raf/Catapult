package com.example.catapult.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.breeds.list.cats
import com.example.catapult.details.breedDetails
import com.example.catapult.photos.albums.grid.breedAlbumsGrid
import com.example.catapult.photos.gallery.photoGallery
import com.example.catapult.quiz.ui.ResultScreen
import com.example.catapult.quiz.ui.quiz


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
            },
            onProfileClick = {
                // Implement the action when profile is clicked
                // For example, navigate to the profile screen
                navController.navigate(route = "profile")
            },
            onBreedsClick = {
                // Implement the action when breeds is clicked
                // For example, navigate to the breeds list screen
                navController.navigate(route = "breeds")
            },
            onQuizClick = {
                // Implement the action when quiz is clicked
                // For example, navigate to the quiz screen
                navController.navigate(route = "quiz")
            }
        )

        breedDetails(
            route = "breeds/{Id}",
            arguments = listOf(
                navArgument(name = "Id") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onClose = {
                navController.navigateUp()
            },
            onImageBreedClick = {
                navController.navigate(route = "breeds/grid/$it")
            }
        )

        breedAlbumsGrid(
            route = "breeds/grid/{Id}",
            arguments = listOf(
                navArgument(name = "Id") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onAlbumClick = {
                navController.navigate(route = "albums/$it")
            },
            onClose = {
                navController.navigateUp()
            }
        )

        photoGallery(
            route = "albums/{Id}",
            arguments = listOf(
                navArgument(name = "Id") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onClose = {
                navController.navigateUp()
            }
        )

        quiz(
            route = "quiz",
            arguments = listOf(),
            onQuizCompleted = { navController.navigate("result") },
            onClose = { navController.navigateUp() },
            onPublishScore = {
                // Handle publish score
                // For example, navigate to leaderboard or show a toast
            }
        )

        composable("result") {
            ResultScreen(
                score = 0f,  // Replace with actual score from view model
                onFinish = { navController.navigate("breeds") },
                onPublish = { /* Handle publish action */ }
            )
        }


    }

}

inline val SavedStateHandle.breeds_id: String
    get() = checkNotNull(get<String>("Id")) { "breed Id is mandatory" }

inline val SavedStateHandle.albumId: String
    get() = checkNotNull(get<String>("albumId")) { "albumId is mandatory" }


//

//inline val SavedStateHandle.breeds_id: String
//    get() = checkNotNull(get("Id")) { "Id is mandatory" }
//
//inline val SavedStateHandle.albumId: Int
//    get() = checkNotNull(get("albumId")) { "albumId is mandatory" }