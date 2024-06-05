package rs.edu.raf.rma6.photos.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.breeds.api.model.BreedApiModel
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
import kotlinx.coroutines.flow.getAndUpdate

class DetailsViewModel(
    private val breedId: String,
    private val breedRepository: BreedsRepository = BreedsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState(breedId = breedId, image = PhotoUiModel("", "")))
    val state = _state.asStateFlow()
    private fun setState(reducer: DetailsUiState.() -> DetailsUiState) =
        _state.getAndUpdate(reducer)


    init {
        fetchDetailsById()
    }


    private fun fetchDetailsById() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val data = withContext(Dispatchers.IO) {
                    breedRepository.fetchBreedById(breedId = breedId)
                }
                setState { copy(data = data.asBreedUiModel()) }
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

    // mapper
    private fun BreedApiModel.asBreedUiModel() = DetailsUiModel(
        id = this.id,
        name = this.name,
        reference_image_id = this.reference_image_id, // Assuming that Image in API model and UI model are the same type
        description = this.description,
        origin = this.origin,
        temperament = this.temperament,
        life_span = this.life_span,
        weight = this.weight, // Assuming that Weight in API model and UI model are the same type
        adaptability = this.adaptability,
        affection_level = this.affection_level,
        child_friendly = this.child_friendly,
        dog_friendly = this.dog_friendly,
        energy_level = this.energy_level,
        wikipedia_url = this.wikipedia_url,
        rare = this.rare // You might need to adjust the type or the way you map this field
    )

    // mapper
    private fun PhotoApiModel.asBreedImageUiModel() = PhotoUiModel(
        id = this.id,
        url = this.url,
//        width = this.width,
//        height = this.height
    )
}
