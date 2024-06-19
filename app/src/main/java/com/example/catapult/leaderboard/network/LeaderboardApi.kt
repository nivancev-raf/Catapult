package com.example.catapult.leaderboard.network

import com.example.catapult.leaderboard.model.LeaderboardEntry
import com.example.catapult.leaderboard.model.LeaderboardPost
import com.example.catapult.leaderboard.model.LeaderboardResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardApi {
    @GET("leaderboard")
    suspend fun getLeaderboard(@Query("category") category: Int): List<LeaderboardEntry>

    @POST("leaderboard")
    suspend fun postResult(@Body result: LeaderboardPost): LeaderboardResponse
}
