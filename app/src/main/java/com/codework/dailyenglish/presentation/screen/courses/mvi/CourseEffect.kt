package com.codework.dailyenglish.presentation.screen.courses.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface CourseEffect: BaseEffect {
    data class NavigateToCourseDetails(
        val courseId: String,
        val courseCode: String
    ) : CourseEffect
}