package com.codework.daily_english.presentation.screen.more

sealed class MoreEvent {
    object OnEditProfile : MoreEvent()
    object OnLogout : MoreEvent()
    data class OnMenuClick(val label: String) : MoreEvent()
}