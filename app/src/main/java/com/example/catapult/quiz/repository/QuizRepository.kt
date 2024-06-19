package com.example.catapult.quiz.repository

import com.example.catapult.quiz.db.QuizResultEntity
import com.example.catapult.quiz.model.QuizResultDao
import com.example.catapult.users.profile.ui.ProfileContract.QuizResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizResultDao: QuizResultDao
) {
    suspend fun insertQuizResult(result: QuizResultEntity) {
        quizResultDao.insertQuizResult(result)
    }

    fun getAllQuizResults(nickname: String): Flow<List<QuizResult>> {
        return quizResultDao.getQuizResultsForUser(nickname).map { entities ->
            entities.map { entity ->
                QuizResult(entity.score, entity.date.toString())
            }
        }
    }

    suspend fun getBestScore(userId: String): Float? {
        return quizResultDao.getBestScoreForUser(userId)
    }

    suspend fun getBestPosition(userId: String): Int {
        return quizResultDao.getBestPositionForUser(userId) + 1 // Adding 1 because position is zero-indexed
    }
}

