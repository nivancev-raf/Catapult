package com.example.catapult.users.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.auth.AuthStore
import com.example.catapult.users.UserProfile
import com.example.catapult.users.profile.editprofile.EditProfileContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authStore: AuthStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileContract.EditProfileState())
    val uiState: StateFlow<EditProfileContract.EditProfileState> = _uiState.asStateFlow()

    private val events = MutableSharedFlow<EditProfileContract.EditProfileEvent>() // handle events that represent user interactions
    fun setEvent(event: EditProfileContract.EditProfileEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        setEvent(EditProfileContract.EditProfileEvent.LoadProfile) // Load profile initially
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    EditProfileContract.EditProfileEvent.LoadProfile -> {
                        loadUserProfile()
                    }
                    is EditProfileContract.EditProfileEvent.OnNameChange -> {
                        val isValid = event.name.isNotBlank()
                        _uiState.update { it.copy(name = event.name, isNameValid = isValid, isSaveEnabled = isValidForm(it.copy(name = event.name, isNameValid = isValid))) }
                    }
                    is EditProfileContract.EditProfileEvent.OnNicknameChange -> {
                        val isValid = event.nickname.matches(Regex("^[a-zA-Z0-9_]*$"))
                        _uiState.update { it.copy(nickname = event.nickname, isNicknameValid = isValid, isSaveEnabled = isValidForm(it.copy(nickname = event.nickname, isNicknameValid = isValid))) }
                    }
                    is EditProfileContract.EditProfileEvent.OnEmailChange -> {
                        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(event.email).matches()
                        _uiState.update { it.copy(email = event.email, isEmailValid = isValid, isSaveEnabled = isValidForm(it.copy(email = event.email, isEmailValid = isValid))) }
                    }
                    EditProfileContract.EditProfileEvent.OnSave -> {
                        saveProfile()
                    }
                }
            }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val userProfile = authStore.authData.first()
            _uiState.update {
                it.copy(
                    name = userProfile.name,
                    nickname = userProfile.nickname,
                    email = userProfile.email,
                    isNameValid = userProfile.name.isNotBlank(),
                    isNicknameValid = userProfile.nickname.matches(Regex("^[a-zA-Z0-9_]*$")),
                    isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(userProfile.email).matches(),
                    isSaveEnabled = userProfile.name.isNotBlank() && userProfile.nickname.matches(Regex("^[a-zA-Z0-9_]*$")) && android.util.Patterns.EMAIL_ADDRESS.matcher(userProfile.email).matches()
                )
            }
        }
    }

    private fun isValidForm(state: EditProfileContract.EditProfileState): Boolean {
        return state.isNameValid && state.isNicknameValid && state.isEmailValid
    }

    private fun saveProfile() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.isSaveEnabled) {
                val newProfile = UserProfile(
                    name = state.name,
                    nickname = state.nickname,
                    email = state.email
                )
                authStore.updateAuthData(newProfile)
                // Optionally, trigger a load profile event to refresh the UI with updated data
                setEvent(EditProfileContract.EditProfileEvent.LoadProfile)
            }
        }
    }
}