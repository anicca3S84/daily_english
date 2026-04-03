package com.codework.daily_english.presentation.screen.course_details.mvi

sealed interface CourseDetailEvent {
    data class OnLessonClick(val lessonId: String, val isLocked: Boolean) : CourseDetailEvent
    data object OnRefresh : CourseDetailEvent
}