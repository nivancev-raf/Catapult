package com.example.catapult.users.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.auth.AuthStore
import com.example.catapult.quiz.repository.QuizRepository
import com.example.catapult.users.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authStore: AuthStore,
    private val quizRepository: QuizRepository // Assuming you have a repository for quizzes
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileContract.ProfileState())
    val uiState: StateFlow<ProfileContract.ProfileState> = _uiState.asStateFlow()

    private val events = MutableSharedFlow<ProfileContract.ProfileEvent>() // handle events that represent user interactions
    fun setEvent(event: ProfileContract.ProfileEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        setEvent(ProfileContract.ProfileEvent.LoadProfile) // Load profile initially
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    ProfileContract.ProfileEvent.LoadProfile -> {
                        loadUserProfile()
                    }
                    is ProfileContract.ProfileEvent.OnEditProfile -> {
                        updateUserProfile(event.name, event.nickname, event.email)
                    }
                }
            }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val userProfile = authStore.authData.first()
            val quizResultsFlow = quizRepository.getAllQuizResults(userProfile.nickname)
            val bestScore = quizRepository.getBestScore(userProfile.nickname) ?: 0f
            val bestPosition = quizRepository.getBestPosition(userProfile.nickname)

            quizResultsFlow.collect { quizResults ->
                _uiState.value = _uiState.value.copy(
                    name = userProfile.name,
                    nickname = userProfile.nickname,
                    email = userProfile.email,
                    quizResults = quizResults.map { ProfileContract.QuizResult(it.score, it.date) },
                    bestScore = bestScore,
                    bestPosition = bestPosition
                )
            }
        }
    }

    private fun updateUserProfile(name: String, nickname: String, email: String) {
        viewModelScope.launch {
            val updatedProfile = UserProfile(
                name = name,
                nickname = nickname,
                email = email
            )
            authStore.updateAuthData(updatedProfile)
            _uiState.value = _uiState.value.copy(
                name = name,
                nickname = nickname,
                email = email
            )
        }
    }
}
