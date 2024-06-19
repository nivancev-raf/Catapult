package com.example.catapult.users.profile.ui

interface ProfileContract {

    sealed class ProfileEvent {
        object LoadProfile : ProfileEvent()
        data class OnEditProfile(val name: String, val nickname: String, val email: String) : ProfileEvent()
    }

    data class ProfileState(
        val name: String = "",
        val nickname: String = "",
        val email: String = "",
        val quizResults: List<QuizResult> = emptyList(),
        val bestScore: Float = 0f,
        val bestPosition: Int = 0
    )

    data class QuizResult(
        val score: Float,
        val date: String // or Date type if needed
    )
}