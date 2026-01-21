package com.codework.daily_english.presentation.screen.courses

sealed interface CourseEvent {
    // Người dùng bấm đổi Tab Level
    data class OnLevelSelected(val level: String) : CourseEvent
}