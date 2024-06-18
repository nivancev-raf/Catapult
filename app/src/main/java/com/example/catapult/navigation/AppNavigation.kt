package com.example.catapult.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.breeds.list.cats
import com.example.catapult.details.breedDetails
import com.example.catapult.leaderboard.ui.leaderboard
import com.example.catapult.photos.albums.grid.breedAlbumsGrid
import com.example.catapult.photos.gallery.photoGallery
import com.example.catapult.quiz.ui.ResultScreen
import com.example.catapult.quiz.ui.quiz
import com.example.catapult.users.UserProfile
import com.example.catapult.users.login.ui.LoginScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import rs.edu.raf.rma.auth.AuthStore


@Composable
fun AppNavigation(authStore: AuthStore) {
    val navController = rememberNavController()
//    var startDestination by remember { mutableStateOf("login") }
    var startDestination by remember { mutableStateOf<String?>(null) } // Initially null
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            val userProfile = authStore.authData.first()
            Log.d("DATASTORE", "AuthData = $userProfile")
            startDestination = if (userProfile.name.isEmpty() && userProfile.nickname.isEmpty() && userProfile.email.isEmpty()) "login" else "breeds"
            // log startDestination
            Log.d("DATASTORE", "startDestination = $startDestination")
        }
    }


    if (startDestination == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }else{


    NavHost( //
        navController = navController,
        startDestination = startDestination!! // startDestination!!, !! -> means that startDestination is not null
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
            },
            onLeaderboardClick = {
                navController.navigate(route = "leaderboard")
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
            onClose = { navController.navigate("breeds") },
            onPublishScore = {
                // Handle publish score
                // For example, navigate to leaderboard or show a toast
            }
        )

        composable("result") {
            ResultScreen(
                ubp = 0f,  // Replace with actual score from view model
                onFinish = { navController.navigate("breeds") },
                onPublish = { /* Handle publish action */ }
            )
        }

        leaderboard(
            route = "leaderboard",
            onClose = { navController.navigate("breeds") }
        )

        composable("login") {
            LoginScreen(navController)
        }

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