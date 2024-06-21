package com.example.catapult.users.login.ui

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
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

//    LaunchedEffect(uiState.isProfileCreated) {
//        if (uiState.isProfileCreated) {
//            navController.navigate("breeds")
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = uiState.name,
            onValueChange = { viewModel.setEvent(LoginContract.LoginEvent.OnNameChange(it)) },
            label = { Text("Name") },
            isError = !uiState.isNameValid
        )
        if (!uiState.isNameValid) {
            Text(text = "Name cannot be empty", color = MaterialTheme.colorScheme.error)
        }

        TextField(
            value = uiState.nickname,
            onValueChange = { viewModel.setEvent(LoginContract.LoginEvent.OnNicknameChange(it)) },
            label = { Text("Nickname") },
            isError = !uiState.isNicknameValid
        )
        if (!uiState.isNicknameValid) {
            Text(text = "Nickname can only contain letters, numbers, and underscores", color = MaterialTheme.colorScheme.error)
        }

        TextField(
            value = uiState.email,
            onValueChange = { viewModel.setEvent(LoginContract.LoginEvent.OnEmailChange(it)) },
            label = { Text("Email") },
            isError = !uiState.isEmailValid
        )
        if (!uiState.isEmailValid) {
            Text(text = "Enter a valid email address", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.setEvent(LoginContract.LoginEvent.OnCreateProfile) },
            enabled = uiState.isNameValid && uiState.isNicknameValid && uiState.isEmailValid
        ) {
            Text("Create Profile")
        }
    }
}
