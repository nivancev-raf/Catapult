package com.example.catapult.leaderboard.repository

import com.example.catapult.leaderboard.model.LeaderboardEntry
import com.example.catapult.leaderboard.network.LeaderboardApi
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi
) {
    suspend fun getLeaderboard(category: Int): List<LeaderboardEntry> {
        return leaderboardApi.getLeaderboard(category)
    }
}
