package com.example.catapult.quiz.model

import android.provider.ContactsContract.CommonDataKinds.Nickname
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.quiz.db.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResultEntity)

    @Query("SELECT * FROM quiz_results WHERE nickname = :nickname ORDER BY date DESC")
    fun getQuizResultsForUser(nickname: String): Flow<List<QuizResultEntity>>

    @Query("SELECT MAX(score) FROM quiz_results WHERE nickname = :nickname")
    suspend fun getBestScoreForUser(nickname: String): Float?

}
