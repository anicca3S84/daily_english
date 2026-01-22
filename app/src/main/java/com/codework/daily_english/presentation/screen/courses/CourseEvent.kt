package com.codework.daily_english.presentation.screen.courses

sealed interface CourseEvent {
    data class OnLevelSelected(val level: String) : CourseEvent
    data class OnNavigateToLesson(val lessonId: String): CourseEvent
}