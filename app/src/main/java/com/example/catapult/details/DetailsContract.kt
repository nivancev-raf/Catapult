package com.example.catapult.details

import com.example.catapult.details.model.DetailsUiModel
import com.example.catapult.details.model.PhotoUiModel

interface DetailsContract {
    data class DetailsUiState(
        val breedId: String,
        val data: DetailsUiModel? = null,
        val image: PhotoUiModel,
        val fetching: Boolean = false,
        val error: DetailsError? = null,
    )
    {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
        }
    }

}