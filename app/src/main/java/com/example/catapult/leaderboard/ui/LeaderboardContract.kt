package com.example.catapult.leaderboard.ui

import com.example.catapult.leaderboard.model.LeaderboardEntry
import com.example.catapult.leaderboard.model.LeaderboardPost

interface LeaderboardContract {
    data class LeaderboardUiState(
        val isLoading: Boolean = false,
        val leaderboard: List<LeaderboardEntry> = emptyList(),
        val error: LeaderboardError.ListUpdateFailed? = null
    )

    sealed class LeaderboardUiEvent {
        object LoadLeaderboard : LeaderboardUiEvent()
        data class PostResult(val result: LeaderboardPost) : LeaderboardUiEvent()
    }

    sealed class LeaderboardError {
        data class ListUpdateFailed(val cause: Throwable?) : LeaderboardError()
    }
}
