package com.example.catapult.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.breeds.api.model.BreedApiModel
import com.example.catapult.breeds.db.BreedData
import com.example.catapult.breeds.mappers.asBreedUiModel
import com.example.catapult.breeds.mappers.toDetailsUiModel
import com.example.catapult.breeds.repository.BreedsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import com.example.catapult.details.DetailsContract.DetailsUiState
import com.example.catapult.details.api.model.PhotoApiModel
import com.example.catapult.details.model.DetailsUiModel
import com.example.catapult.details.model.PhotoUiModel
import com.example.catapult.navigation.breeds_id
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.getAndUpdate
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val breedRepository: BreedsRepository,
) : ViewModel() {

    private val breedId: String = savedStateHandle.breeds_id


    private val _state = MutableStateFlow(DetailsUiState(breedId = breedId, image = PhotoUiModel("", "")))
    val state = _state.asStateFlow()
    private fun setState(reducer: DetailsUiState.() -> DetailsUiState) =
        _state.getAndUpdate(reducer)


    init {
        fetchDetailsById()
        Log.d("BreedId", breedId)
    }


    private fun fetchDetailsById() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val data = withContext(Dispatchers.IO) {
//                    breedRepository.fetchBreedById(breedId = breedId)
                    breedRepository.getBreedById(breedId = breedId)
                    // return is BreedData, so we need to map it to BreedApiModel
                }
                val apiModel = data.toBreedApiModel()

                setState { copy(data = apiModel.toDetailsUiModel()) }
                fetchBreedImage(data.reference_image_id?: "")
            } catch (error: IOException) {
                setState {
                    copy(error = DetailsUiState.DetailsError.DataUpdateFailed(cause = error))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

    private fun fetchBreedImage(breedImageId: String) {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val imageData = withContext(Dispatchers.IO) {
                    breedRepository.fetchBreedImage(breedImageId = breedImageId)
                }
                // Assuming you want to update only the image part of the state
                setState { copy(image = imageData.asBreedImageUiModel()) }
            } catch (error: IOException) {
                setState {
                    copy(error = DetailsUiState.DetailsError.DataUpdateFailed(cause = error))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

    // mappe

    // mapper
    private fun PhotoApiModel.asBreedImageUiModel() = PhotoUiModel(
        photoId = this.id,
        url = this.url,
//        width = this.width,
//        height = this.height
    )

    private fun BreedData.toBreedApiModel() = BreedApiModel(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = this.description,
        temperament = this.temperament,
        origin = this.origin,
        life_span = this.life_span,
        reference_image_id = this.reference_image_id,
        wikipedia_url = this.wikipedia_url,
        adaptability = this.adaptability,
        affection_level = this.affection_level,
        child_friendly = this.child_friendly,
        dog_friendly = this.dog_friendly,
        energy_level = this.energy_level,
        rare = this.rare,
        weight = this.weight
    )
}
