package com.example.catapult.quiz.ui

import android.app.AlertDialog
import com.example.catapult.quiz.model.AnswerOption
import com.example.catapult.quiz.model.Question
import com.example.catapult.users.profile.ui.ProfileContract

interface QuizContract {

    data class QuizUiState(
        val questions: List<Question> = emptyList(),
        val currentQuestionIndex: Int = 0,
        val score: Float = 0f, // broj tacnih odgovora
        val updating: Boolean = false,
        val error: Exception? = null,
        val selectedOption: AnswerOption? = null,
        val isOptionCorrect: Boolean? = null,
        val timeRemaining: Long = 300000L, // max vreme trajanja kviza (300 sec)
        val ubp: Float = 0f, // ukupan broj poena
        val showExitDialog: Boolean = false,
        val nickname: String = "" // Add nickname here
    )

    sealed class QuizEvents {
        object StopQuiz : QuizEvents()
        object ContinueQuiz : QuizEvents()
//        object NextQuestion : QuizEvent()
//        object FinishQuiz : QuizEvent()
//        object PublishScore : QuizEvent()

//        data class OptionSelected(val option: AnswerOption) : QuizEvent()
//        data class TimerTick(val timeRemaining: Long) : QuizEvent()
    }
}