package com.example.catapult.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardEntry(
    val nickname: String,
    val result: Float,
    val category: Int,
    val createdAt: Long,
)
