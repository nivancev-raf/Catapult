package com.example.catapult.breeds.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.breeds.list.BreedListContract.BreedListState
import com.example.catapult.breeds.list.BreedListContract.BreedListUiEvent


@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.cats(
    route: String,
    onBreedClick: (String) -> Unit,
) = composable(
    route = route
) {
    val breedListViewModel = hiltViewModel<BreedListViewModel>() // funkcija viewModel je deo compose-a
    val state = breedListViewModel.state.collectAsState() // vraca poslednji state koji je emitovan

    BreedListScreen(
        state = state.value,
        eventPublisher = {
            breedListViewModel.setEvent(it)
        },
        onBreedClick = onBreedClick,
    )
}


@ExperimentalMaterial3Api
@Composable
fun BreedListScreen(
    state: BreedListState,
    eventPublisher: (uiEvent: BreedListUiEvent) -> Unit,
    onBreedClick: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            MediumTopAppBar(title = { Text(text = "Breeds") })
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
                    singleLine = true,  // sprecava prelazak u novi red (zatvara search na enter)
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
//                        contentPadding = paddingValues,
                    ) {

                        items(
                            items = breedsToShow,
//                            key = { it.id },
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