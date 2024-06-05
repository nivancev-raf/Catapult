package com.example.catapult.breeds.list

import com.example.catapult.breeds.list.model.BreedUiModel

interface BreedListContract {

    data class BreedListState(
        val loading: Boolean = false,
        val query: String = "",
        val isSearchMode: Boolean = false,
        val breeds: List<BreedUiModel> = emptyList(),
        val filteredBreeds: List<BreedUiModel> = emptyList(),
        val error: ListError? = null

    )

    sealed class BreedListUiEvent { // zapecacena klasa
        data class SearchQueryChanged(val query: String) : BreedListUiEvent()
        data object ClearSearch : BreedListUiEvent()
    }

    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}