package com.example.catapult.leaderboard.di

import javax.inject.Qualifier

@Qualifier // kvalifikator koji se koristi za razlikovanje zavisnosti koje imaju isti tip
@Retention(AnnotationRetention.BINARY)
annotation class LeaderboardBaseUrl()
