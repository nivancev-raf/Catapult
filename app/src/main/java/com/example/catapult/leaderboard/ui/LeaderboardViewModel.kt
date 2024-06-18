package com.example.catapult.leaderboard.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.leaderboard.repository.LeaderboardRepository
import com.example.catapult.leaderboard.ui.LeaderboardContract

import com.example.catapult.leaderboard.ui.LeaderboardContract.LeaderboardUiState
import com.example.catapult.leaderboard.ui.LeaderboardContract.LeaderboardUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardUiState())
    val state = _state.asStateFlow()

    private fun setState(reducer: LeaderboardUiState.() -> LeaderboardUiState) = _state.update(reducer)

    private val events = MutableSharedFlow<LeaderboardUiEvent>()
    fun setEvent(event: LeaderboardUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is LeaderboardUiEvent.LoadLeaderboard -> loadLeaderboard(1)
                }
            }
        }
    }

    private fun loadLeaderboard(category: Int) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            try {
                val leaderboard = repository.getLeaderboard(category)
                Log.d("LeaderboardViewModel", "loadLeaderboard: $leaderboard")
                setState { copy(isLoading = false, leaderboard = leaderboard) }
            } catch (error: Exception) {
                Log.d("LeaderboardViewModel", "loadLeaderboard: $error")
                setState { copy(isLoading = false, error = LeaderboardContract.LeaderboardError.ListUpdateFailed(error)) }
            }
        }
    }
}
