package com.example.catapult.leaderboard.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.leaderboard.ui.LeaderboardViewModel
import com.example.catapult.leaderboard.ui.LeaderboardContract.LeaderboardUiState

fun NavGraphBuilder.leaderboard(
    route: String,
    onClose: () -> Unit,
) = composable(
    route = route
) {
    val leaderboardViewModel = hiltViewModel<LeaderboardViewModel>()
    val state = leaderboardViewModel.state.collectAsState()

    LeaderboardScreen(
        state = state.value,
        eventPublisher = {
            leaderboardViewModel.setEvent(it)
        },
        onClose = onClose,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    state: LeaderboardUiState,
    eventPublisher: (LeaderboardContract.LeaderboardUiEvent) -> Unit,
    onClose: () -> Unit,
) {

    val nicknameOccurrences = state.leaderboard.groupingBy { it.nickname }.eachCount()

    BackHandler(onBack = onClose)

    // Event se desava kada se ucita ekran
    LaunchedEffect(Unit) {
        eventPublisher(LeaderboardContract.LeaderboardUiEvent.LoadLeaderboard)
    }
    Log.d("LeaderboardScreen", "LeaderboardScreen: $state")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(state.leaderboard) { index, item -> // Use itemsIndexed to get the index
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${index + 1}. ${item.nickname}", // Display the rank with nickname
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Score: ${item.result}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = Color.Gray,
                                                fontSize = 14.sp
                                            )
                                        )
                                        Text(
                                            text = "Occurrences: ${nicknameOccurrences[item.nickname]}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = Color.Gray,
                                                fontSize = 14.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
