package com.example.catapult.users

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val name: String,
    val nickname: String,
    val email: String
)
