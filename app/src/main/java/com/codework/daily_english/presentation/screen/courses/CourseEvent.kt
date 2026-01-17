package com.codework.daily_english.presentation.screen.courses

sealed interface CourseEvent {
    // Người dùng bấm đổi Tab Level
    data class OnLevelSelected(val level: String) : CourseEvent

    // Người dùng bấm vào một chủ đề để vào học
    data class OnTopicClick(val topicId: String) : CourseEvent
}