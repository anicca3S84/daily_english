package com.codework.dailyenglish.presentation.screen.user.mvi

sealed interface UserInfoEvent {
    data object OnLoadProfile : UserInfoEvent
    data class OnFullNameChanged(val newName: String) : UserInfoEvent
    data class OnAvatarChanged(val newUrl: String) : UserInfoEvent
    data object OnSaveProfile : UserInfoEvent
    data class OnAvatarSelected(val uri: android.net.Uri) : UserInfoEvent
}