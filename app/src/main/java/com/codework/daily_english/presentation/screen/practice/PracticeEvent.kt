package com.codework.daily_english.presentation.screen.practice

// PracticeEvent.kt
sealed class PracticeEvent {
    object LoadData : PracticeEvent()
    object OnRetry : PracticeEvent()
    data class OnCategoryClick(val id: String) : PracticeEvent()
}

// PracticeState.kt (Dùng chung với BaseScreen của bạn)
// PracticeData là class chứa dữ liệu thật (đã định nghĩa ở tin nhắn trước)