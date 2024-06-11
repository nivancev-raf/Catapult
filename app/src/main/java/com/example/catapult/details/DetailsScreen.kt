package com.example.catapult.details


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberImagePainter
import com.example.catapult.details.DetailsContract.DetailsUiState
import com.example.catapult.details.model.DetailsUiModel
import com.example.catapult.details.model.PhotoUiModel
import com.example.catapult.ui.composables.AppIconButton
import com.example.catapult.ui.composables.NoDataContent

fun NavGraphBuilder.breedDetails(
    route: String,
    arguments: List<NamedNavArgument>,
    onImageBreedClick: (String) -> Unit,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->

    val breedDetailViewModel: DetailsViewModel = hiltViewModel(navBackStackEntry)


//    val detailsViewModel = viewModel<DetailsViewModel>(
//        factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return DetailsViewModel(breedId = breedId) as T
//            }
//        }
//    )

    val state = breedDetailViewModel.state.collectAsState()


    DetailsScreen(
        state = state.value,
        onClose = onClose,
        onImageBreedClick = onImageBreedClick,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    state: DetailsUiState,
    onClose: () -> Unit,
    onImageBreedClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = state.data?.name ?: "Loading")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.fetching) {
                    // Loading...
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.error != null) {
                    // ukoliko postoji error
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (state.error) {
                            is DetailsUiState.DetailsError.DataUpdateFailed ->
                                "Failed to load. Error message: ${state.error.cause?.message}."
                        }
                        Text(text = errorMessage)
                    }
                } else if (state.data != null) {
                    DataColumn(
                        data = state.data,
                        image = state.image,
                        onImageBreedClick = onImageBreedClick,
                    )
                } else {
                    // nema podataka
                    NoDataContent(id = state.breedId)
                }
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DataColumn(
    data: DetailsUiModel,
    image: PhotoUiModel,
    onImageBreedClick: (String) -> Unit,
) {
    Column (
        modifier = Modifier.verticalScroll(rememberScrollState())
    ){

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
             Image(
                 painter = rememberImagePainter(image.url),
                 contentDescription = null,
                 modifier = Modifier.fillMaxSize()
             )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // add button fore more images

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = { onImageBreedClick(data.id) },
            ) {
                Icon(Icons.Filled.Info, contentDescription = "Info")
                Text("View more images of this breed")
            }
        }

        // opis
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = data.description
        )

        Spacer(modifier = Modifier.height(8.dp))

        // spisak zemalja porekla
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Origin: ${data.origin}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // sve osobine temperamenta
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Temperaments: ${data.temperament}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // životni vek
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Life Span: ${data.life_span}"
        )

        Spacer(modifier = Modifier.height(8.dp))


        // If the breed is rare
        if (data.rare > 0) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "This is a rare breed."
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // prosečna težina i/ili visina rase
        data.weight.let { weight ->
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Weight: ${weight.metric} (Metric), ${weight.imperial} (Imperial)"
            )
        }

        FlowRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
            data.temperament.split(",").map { it.trim() }.forEach { temperament ->
                AssistChip(onClick = { /*TODO*/ }, label = { Text(temperament) })
            }
        }

        CharacteristicBar(label = "Adaptability", level = data.adaptability)
        CharacteristicBar(label = "Affection Level", level = data.affection_level)
        CharacteristicBar(label = "Child friendly", level = data.child_friendly)
        CharacteristicBar(label = "Dog friendly", level = data.dog_friendly)
        CharacteristicBar(label = "Energy level", level = data.energy_level)

        val uriHandler = LocalUriHandler.current //

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            data.wikipedia_url?.let {
                Button(
                    onClick = { uriHandler.openUri(data.wikipedia_url) },
                ) {
                    Icon(Icons.Filled.Info, contentDescription = "Info")
                    Text("Open Wikipedia Page")
                }
            }
        }
    }
}

@Composable
fun CharacteristicBar(label: String, level: Int) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "$label: $level/5")
        LinearProgressIndicator(
            progress = level / 5f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

