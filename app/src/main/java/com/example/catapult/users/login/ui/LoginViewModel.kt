package com.example.catapult.users.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.catapult.auth.AuthStore
import com.example.catapult.users.UserProfile
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authStore: AuthStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginContract.LoginState())
    val uiState: StateFlow<LoginContract.LoginState> = _uiState.asStateFlow()

    private val events = MutableSharedFlow<LoginContract.LoginEvent>() // handle events that represent user interactions
    fun setEvent(event: LoginContract.LoginEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is LoginContract.LoginEvent.OnNameChange -> {
                        _uiState.value = _uiState.value.copy(
                            name = event.name,
                            isNameValid = event.name.isNotBlank()
                        )
                    }
                    is LoginContract.LoginEvent.OnNicknameChange -> {
                        _uiState.value = _uiState.value.copy(
                            nickname = event.nickname,
                            isNicknameValid = event.nickname.matches(Regex("^[a-zA-Z0-9_]*$"))
                        )
                    }
                    is LoginContract.LoginEvent.OnEmailChange -> {
                        _uiState.value = _uiState.value.copy(
                            email = event.email,
                            isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                        )
                    }
                    LoginContract.LoginEvent.OnCreateProfile -> {
                        val state = _uiState.value
                        if (state.isNameValid && state.isNicknameValid && state.isEmailValid) {
                            val newUserProfile = UserProfile(
                                name = state.name,
                                nickname = state.nickname,
                                email = state.email
                            )
                            authStore.updateAuthData(newUserProfile)
                            _uiState.value = _uiState.value.copy(isProfileCreated = true)
                        }
                    }
                }
            }
        }
    }

}
