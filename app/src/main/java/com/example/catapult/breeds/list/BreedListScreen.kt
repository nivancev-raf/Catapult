package com.example.catapult.breeds.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.breeds.list.BreedListContract.BreedListState
import com.example.catapult.breeds.list.BreedListContract.BreedListUiEvent
import com.example.catapult.drawer.BreedListDrawer
import kotlinx.coroutines.launch


fun NavGraphBuilder.cats(
    route: String,
    onBreedClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onBreedsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
) = composable(
    route = route
) {
    val breedListViewModel = hiltViewModel<BreedListViewModel>()
    val state = breedListViewModel.state.collectAsState()

    BreedListScreen(
        state = state.value,
        eventPublisher = {
            breedListViewModel.setEvent(it)
        },
        onBreedClick = onBreedClick,
        onProfileClick = onProfileClick,
        onBreedsClick = onBreedsClick,
        onQuizClick = onQuizClick,
        onLeaderboardClick = onLeaderboardClick,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedListScreen(
    state: BreedListState,
    eventPublisher: (uiEvent: BreedListUiEvent) -> Unit,
    onBreedClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onBreedsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: () -> Unit,

) {
    val uiScope = rememberCoroutineScope() // uiScope je CoroutineScope koji je vezan za UI thread
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed) // stanje drawera - open ili closed (po defaultu je closed)

    // fokus kursora za search polje
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // na dugme back zatvori drawer
    BackHandler(enabled = drawerState.isOpen) {
        uiScope.launch { drawerState.close() }
    }

    ModalNavigationDrawer( //
        modifier = Modifier,
        drawerState = drawerState,
        drawerContent = {
            BreedListDrawer(
                onProfileClick = {
                    uiScope.launch {
                        drawerState.close()
                    }
                    onProfileClick()
                },
                onBreedsClick = {
                    uiScope.launch { drawerState.close() }
                    onBreedsClick()
                },
                onQuizClick = {
                    uiScope.launch { drawerState.close() }
                    onQuizClick()
                },
                onLeaderboardClick = {
                    uiScope.launch { drawerState.close() }
                    onLeaderboardClick()
                }

            )
        },
        // sadržaj breed list ekrana
        content = {
            Scaffold(
                topBar = {
                    MediumTopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Breeds", textAlign = TextAlign.Center)
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                uiScope.launch { drawerState.open() } // trigerujemo rekompoziciju ModalNavigationDrawer-a
                                // ovo se izvrsava na main threadu jer update-ujemo UI (za update UI moramo biti na main threadu)
                            }) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        // Search TextField
                        OutlinedTextField(
                            value = state.query,
                            onValueChange = {
                                eventPublisher(BreedListUiEvent.SearchQueryChanged(it))
                            },
                            label = { Text("Search") },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search"
                                )
                            },
                            trailingIcon = {
                                if (state.query.isNotEmpty()) {
                                    IconButton(onClick = {
                                        eventPublisher(BreedListUiEvent.ClearSearch)
                                        // Hide the keyboard
                                        keyboardController?.hide()
                                        // Clear focus
                                        focusManager.clearFocus()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Clear Search"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )

                        if (state.error != null) {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                val errorMessage = when (state.error) {
                                    is BreedListContract.ListError.ListUpdateFailed ->
                                        "Failed to load. Please try again later. Error message: ${state.error.cause?.message}."
                                }
                                Text(text = errorMessage)
                            }
                        } else if (state.loading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            val breedsToShow = if (state.query.isNotEmpty()) state.filteredBreeds else state.breeds
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                items(
                                    items = breedsToShow,
                                ) { breed ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp)
                                            .padding(bottom = 16.dp)
                                            .clickable { onBreedClick(breed.id) },
                                    ) {
                                        val temperaments = breed.temperaments.split(",").map { it.trim() }.take(3)

                                        Column {
                                            Text(
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp)
                                                    .padding(vertical = 16.dp),
                                                text = buildString {
                                                    append(breed.name)
                                                    if (!breed.alternativeNames.isNullOrEmpty()) {
                                                        append("\nAlternatives: ${breed.alternativeNames}")
                                                    }
                                                    append("\nDescription: ${breed.description.take(250)}")
                                                },
                                                style = MaterialTheme.typography.headlineSmall,
                                            )
                                            if (temperaments.isNotEmpty()) {
                                                Row(
                                                    modifier = Modifier
                                                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    temperaments.forEach { temperament ->
                                                        AssistChip(onClick = { /*TODO*/ }, label = { Text(text = temperament) })
                                                    }
                                                }
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
    )
}
