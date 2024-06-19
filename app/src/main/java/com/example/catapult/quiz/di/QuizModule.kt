package com.example.catapult.quiz.di

import com.example.catapult.db.AppDatabase
import com.example.catapult.quiz.model.QuizResultDao
import com.example.catapult.quiz.repository.QuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuizModule {

    @Provides
    fun provideQuizResultDao(database: AppDatabase): QuizResultDao {
        return database.quizResultDao()
    }

    @Provides
    @Singleton
    fun provideQuizRepository(quizResultDao: QuizResultDao): QuizRepository {
        return QuizRepository(quizResultDao)
    }
}
