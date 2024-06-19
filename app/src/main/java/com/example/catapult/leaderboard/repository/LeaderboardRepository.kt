package com.example.catapult.leaderboard.repository

import com.example.catapult.leaderboard.model.LeaderboardEntry
import com.example.catapult.leaderboard.model.LeaderboardPost
import com.example.catapult.leaderboard.model.LeaderboardResponse
import com.example.catapult.leaderboard.network.LeaderboardApi
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi
) {
    suspend fun getLeaderboard(category: Int): List<LeaderboardEntry> {
        return leaderboardApi.getLeaderboard(category)
    }

    suspend fun postResult(result: LeaderboardPost): LeaderboardResponse {
        return leaderboardApi.postResult(result)
    }

    suspend fun getBestPositionForUser(nickname: String): Int {
        val leaderboard = leaderboardApi.getLeaderboard(1)
        val userPosition = leaderboard.indexOfFirst { it.nickname == nickname }
        return if (userPosition == -1) -1 else userPosition + 1 // Return 1-based index
    }
}
