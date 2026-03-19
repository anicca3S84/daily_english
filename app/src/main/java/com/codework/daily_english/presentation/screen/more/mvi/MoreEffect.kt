package com.codework.daily_english.presentation.screen.more.mvi

sealed interface MoreEffect {
    data object NavigateToEditProfile : MoreEffect
    data object HandleLogout : MoreEffect
    data class NavigateToMenu(val label: String) : MoreEffect
    data class ShowToast(val message: String) : MoreEffect
}