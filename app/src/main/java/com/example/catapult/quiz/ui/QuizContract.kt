package com.example.catapult.quiz.ui

import com.example.catapult.quiz.model.AnswerOption
import com.example.catapult.quiz.model.Question

interface QuizContract {

    data class QuizUiState(
        val questions: List<Question> = emptyList(),
        val currentQuestionIndex: Int = 0,
        val score: Float = 0f,
        val updating: Boolean = false,
        val error: Exception? = null,
        val selectedOption: AnswerOption? = null,
        val isOptionCorrect: Boolean? = null,
        val timeRemaining: Long = 300000L // 5 minutes in milliseconds
    )
}