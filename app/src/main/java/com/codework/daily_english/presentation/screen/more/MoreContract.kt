package com.codework.daily_english.presentation.screen.more

// 1. STATE (Đổi MoreData thành MoreState)
data class MoreState(
    val userName: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val streak: Int = 0,
    val totalXp: Int = 0,
    val wordsLearned: Int = 0,
    val appVersion: String = "1.0.0"
)

// 2. EVENT
sealed interface MoreEvent {
    data object OnEditProfile : MoreEvent
    data object OnLogout : MoreEvent
    data class OnMenuClick(val label: String) : MoreEvent
    data object OnRetry : MoreEvent // Dành cho nút thử lại khi lỗi mạng
}

// 3. EFFECT
sealed interface MoreEffect {
    data object NavigateToEditProfile : MoreEffect
    data object HandleLogout : MoreEffect
    data class NavigateToMenu(val label: String) : MoreEffect
    data class ShowToast(val message: String) : MoreEffect
}