package com.example.catapult.users.profile.editprofile


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.catapult.users.profile.ui.EditProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
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
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                TextField(
                    value = uiState.name,
                    onValueChange = { viewModel.setEvent(EditProfileContract.EditProfileEvent.OnNameChange(it)) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.isNameValid
                )
                if (!uiState.isNameValid) {
                    Text("Name cannot be empty", color = Color.Red)
                }
                TextField(
                    value = uiState.nickname,
                    onValueChange = { viewModel.setEvent(EditProfileContract.EditProfileEvent.OnNicknameChange(it)) },
                    label = { Text("Nickname") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.isNicknameValid
                )
                if (!uiState.isNicknameValid) {
                    Text("Nickname can only contain letters, numbers, and underscores", color = Color.Red)
                }
                TextField(
                    value = uiState.email,
                    onValueChange = { viewModel.setEvent(EditProfileContract.EditProfileEvent.OnEmailChange(it)) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !uiState.isEmailValid
                )
                if (!uiState.isEmailValid) {
                    Text("Invalid email address", color = Color.Red)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.setEvent(EditProfileContract.EditProfileEvent.OnSave)
                        // navigate back to breeds screen
                        navController.navigate("breeds")

                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.isSaveEnabled
                ) {
                    Text("Save")
                }
            }
        }
    )
}