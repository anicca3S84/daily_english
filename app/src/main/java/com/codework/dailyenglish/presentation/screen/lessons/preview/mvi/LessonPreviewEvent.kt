package com.codework.dailyenglish.presentation.screen.lessons.preview.mvi

sealed interface LessonPreviewEvent {
    data class OnLoadPreview(val lessonId: String) : LessonPreviewEvent
    data class OnStartLearningClick(val lessonId: String) : LessonPreviewEvent
    object OnCloseClick : LessonPreviewEvent
    object OnRefresh : LessonPreviewEvent
    data class OnReviewClick(val lessonId: String) : LessonPreviewEvent
    data class OnRestartClick(val lessonId: String) : LessonPreviewEvent
}