package com.codework.daily_english.presentation.screen.courses.mvi

import com.codework.daily_english.core.base.BaseEffect

sealed interface CourseEffect: BaseEffect {
    data class NavigateToLesson(val lessonId: String) : CourseEffect
}