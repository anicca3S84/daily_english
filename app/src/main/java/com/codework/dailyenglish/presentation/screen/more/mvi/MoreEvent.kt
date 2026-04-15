package com.codework.dailyenglish.presentation.screen.more.mvi

sealed interface MoreEvent {
    data object OnEditProfile : MoreEvent
    data object OnLogout : MoreEvent
    data class OnMenuClick(val label: String) : MoreEvent
    data object OnRetry : MoreEvent // Dành cho nút thử lại khi lỗi mạng
}