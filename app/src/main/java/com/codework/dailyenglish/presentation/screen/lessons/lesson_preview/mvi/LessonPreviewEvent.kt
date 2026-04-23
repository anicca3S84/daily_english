package com.codework.dailyenglish.presentation.screen.lessons.lesson_preview.mvi

sealed interface LessonPreviewEvent {
    data class OnLoadPreview(val lessonId: String) : LessonPreviewEvent
    data class OnStartLearningClick(val lessonId: String) : LessonPreviewEvent
    object OnCloseClick : LessonPreviewEvent
    object OnRefresh : LessonPreviewEvent
}