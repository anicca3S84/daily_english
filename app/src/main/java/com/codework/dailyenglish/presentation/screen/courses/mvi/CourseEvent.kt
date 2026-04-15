package com.codework.dailyenglish.presentation.screen.courses.mvi

sealed interface CourseEvent {
    data class OnCourseClick(val courseId: String, val courseCode: String, val isLocked: Boolean) : CourseEvent
    object OnRefreshData : CourseEvent
}