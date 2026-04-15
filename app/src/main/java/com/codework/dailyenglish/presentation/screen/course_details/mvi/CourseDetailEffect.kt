package com.codework.dailyenglish.presentation.screen.course_details.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface CourseDetailEffect : BaseEffect {
    data class ShowLessonPreview(val lessonId: String) : CourseDetailEffect
}