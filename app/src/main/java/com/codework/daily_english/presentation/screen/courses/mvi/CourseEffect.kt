package com.codework.daily_english.presentation.screen.courses.mvi

sealed interface CourseEffect {
    data class NavigateToLesson(val lessonId: String) : CourseEffect
}