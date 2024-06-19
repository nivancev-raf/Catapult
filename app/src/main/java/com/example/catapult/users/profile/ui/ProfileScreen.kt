package com.example.catapult.users.profile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.catapult.users.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                UserProfileSection(uiState)
                Spacer(modifier = Modifier.height(16.dp))
                EditProfileButton(navController)
                Spacer(modifier = Modifier.height(16.dp))
                QuizResultsSection(uiState.quizResults)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Best Score: ${uiState.bestScore}")
                Text("Best Position: ${uiState.bestPosition}")
            }
        }
    )
}

@Composable
fun UserProfileSection(uiState: ProfileContract.ProfileState) {
    TextField(
        value = uiState.name,
        onValueChange = { /* edit je u Edit profile */ },
        label = { Text("Name") },
        readOnly = true,
        modifier = Modifier.fillMaxWidth()
    )
    TextField(
        value = uiState.nickname,
        onValueChange = { /* edit je u Edit profile */ },
        label = { Text("Nickname") },
        readOnly = true,
        modifier = Modifier.fillMaxWidth()
    )
    TextField(
        value = uiState.email,
        onValueChange = { /* edit je u Edit profile*/ },
        label = { Text("Email") },
        readOnly = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun EditProfileButton(navController: NavController) {
    Button(
        onClick = {
            // Navigate to edit screen or open dialog to edit profile
            navController.navigate("editProfile")
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Edit Profile")
    }
}

@Composable
fun QuizResultsSection(quizResults: List<ProfileContract.QuizResult>) {
    Column {
        Text("Quiz Results:", style = MaterialTheme.typography.headlineSmall)
        quizResults.forEach { result ->
            QuizResultItem(result)
            Divider()
        }
    }
}

@Composable
fun QuizResultItem(result: ProfileContract.QuizResult) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("Score: ${result.score}")
        Text("Date: ${result.date}")
    }
}
