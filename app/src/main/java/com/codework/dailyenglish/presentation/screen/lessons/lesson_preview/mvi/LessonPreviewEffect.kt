package com.codework.dailyenglish.presentation.screen.lessons.lesson_preview.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface LessonPreviewEffect : BaseEffect {
    data class NavigateToLearningSession(val lessonId: String) : LessonPreviewEffect
    object CloseBottomSheet : LessonPreviewEffect
}