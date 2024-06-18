package com.example.catapult.leaderboard.ui

import com.example.catapult.leaderboard.model.LeaderboardEntry

interface LeaderboardContract {
    data class LeaderboardUiState(
        val isLoading: Boolean = false,
        val leaderboard: List<LeaderboardEntry> = emptyList(),
        val error: LeaderboardError.ListUpdateFailed? = null
    )

    sealed class LeaderboardUiEvent {
        object LoadLeaderboard : LeaderboardUiEvent()
    }

    sealed class LeaderboardError {
        data class ListUpdateFailed(val cause: Throwable?) : LeaderboardError()
    }
}
