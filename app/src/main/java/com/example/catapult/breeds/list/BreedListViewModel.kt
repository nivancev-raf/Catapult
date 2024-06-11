package com.example.catapult.breeds.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.breeds.api.model.BreedApiModel
import com.example.catapult.breeds.repository.BreedsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.catapult.breeds.list.BreedListContract.BreedListState
import com.example.catapult.breeds.list.BreedListContract.BreedListUiEvent
import com.example.catapult.breeds.list.model.BreedUiModel
import com.example.catapult.breeds.mappers.asBreedUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val repository: BreedsRepository,
) : ViewModel() { // nasledjujemo ViewModel

    private val _state = MutableStateFlow(BreedListState()) // holds the current state of the UI
    // _state - mutable state
    // state - read-only state, iz drugih klasa mozemo da citamo state, ali ne mozemo da ga menjamo
    val state = _state.asStateFlow()

    private fun setState(reducer: BreedListState.() -> BreedListState) = _state.update(reducer)
    // reducer - funkcija koja uzima trenutni state i vraca novi state

    private val events = MutableSharedFlow<BreedListUiEvent>() // handle events that represent user interactions
    fun setEvent(event: BreedListUiEvent) = viewModelScope.launch { events.emit(event) }



     // MutableSharedFlow - flow koji moze da emituje vise vrednosti
    init {
        observeEvents()
        fetchAllBreeds()
        observeBreeds()
    }

    // collect - funkcija koja se koristi za prikupljanje vrednosti iz flow-a
    // flow je koncept koji se koristi za emitovanje (vise) dogadjaja asinhrono (kao stream u javi)
    private fun observeEvents() {
        viewModelScope.launch {       // izvrsava se na main threadu po defaultu
            events.collect {
                when (it) {
                    is BreedListUiEvent.ClearSearch -> setState { copy(query = "") }
                    is BreedListUiEvent.SearchQueryChanged -> {
                        filterBreedList(query = it.query)
                    }

                }
            }
        }
    }

    private fun observeBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            repository.observeAllBreeds()
                .distinctUntilChanged() // vidljivo samo ako se vrednost promeni
                .collect {
                    setState {
                        copy(
                            loading = false,
                            breeds = it.map { it.asBreedUiModel() }
                        )
                    }
                }
        }
    }


    private fun filterBreedList(query: String) {
        val filteredBreeds = _state.value.breeds.filter { breed ->
            breed.name.startsWith(query, ignoreCase = true)
        }
        setState { copy(filteredBreeds = filteredBreeds, query = query) }

    }

    private fun fetchAllBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchAllBreeds()
                }
            } catch (error: Exception) {
                // TODO Handle error
                setState { copy(error = BreedListContract.ListError.ListUpdateFailed(error))}
                Log.e("BreedListViewModel", "Error", error)
            } finally {
                setState { copy(loading = false) }
            }
        }
    }


    // mapper
//    private fun BreedApiModel.asBreedUiModel() = BreedUiModel(
//        id = this.id,
//        name = this.name,
//        alternativeNames = this.alt_names,
//        description = this.description,
//        temperaments = this.temperament,
//    )
}
