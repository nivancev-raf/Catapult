package com.example.catapult.users.profile.editprofile

object EditProfileContract {

    data class EditProfileState(
        val name: String = "",
        val nickname: String = "",
        val email: String = "",
        val isNameValid: Boolean = true,
        val isNicknameValid: Boolean = true,
        val isEmailValid: Boolean = true,
        val isSaveEnabled: Boolean = false
    )

    sealed class EditProfileEvent {
        object LoadProfile : EditProfileEvent()
        data class OnNameChange(val name: String) : EditProfileEvent()
        data class OnNicknameChange(val nickname: String) : EditProfileEvent()
        data class OnEmailChange(val email: String) : EditProfileEvent()
        object OnSave : EditProfileEvent()
    }
}
