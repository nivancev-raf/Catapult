package com.example.catapult.users.profile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.catapult.users.UserProfile

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = uiState.name,
            onValueChange = { /* edit je u Edit profile */ },
            label = { Text("Name") },
            readOnly = true
        )
        TextField(
            value = uiState.nickname,
            onValueChange = { /* edit je u Edit profile */ },
            label = { Text("Nickname") },
            readOnly = true
        )
        TextField(
            value = uiState.email,
            onValueChange = { /* edit je u Edit profile*/ },
            label = { Text("Email") },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Navigate to edit screen or open dialog to edit
        }) {
            Text("Edit Profile")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Quiz Results:")
        uiState.quizResults.forEach { result ->
            Text("Score: ${result.score}, Date: ${result.date}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Best Score: ${uiState.bestScore}")
        Text("Best Position: ${uiState.bestPosition}")
    }
}
