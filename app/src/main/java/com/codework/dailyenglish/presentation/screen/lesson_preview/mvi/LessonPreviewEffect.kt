package com.codework.dailyenglish.presentation.screen.lesson_preview.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface LessonPreviewEffect : BaseEffect {
    data class NavigateToLearningSession(val lessonId: String) : LessonPreviewEffect
    object CloseBottomSheet : LessonPreviewEffect
}