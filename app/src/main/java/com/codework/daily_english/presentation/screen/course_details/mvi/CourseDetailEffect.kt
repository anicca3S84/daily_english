package com.codework.daily_english.presentation.screen.course_details.mvi

import com.codework.daily_english.core.base.BaseEffect

sealed interface CourseDetailEffect : BaseEffect {
    data class NavigateToLearning(val lessonId: String) : CourseDetailEffect
}