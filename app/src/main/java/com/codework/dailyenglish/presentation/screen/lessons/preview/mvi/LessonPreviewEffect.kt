package com.codework.dailyenglish.presentation.screen.lessons.preview.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface LessonPreviewEffect : BaseEffect {
    data class NavigateToLearningSession(val lessonId: String, val isReview: Boolean) : LessonPreviewEffect
    object CloseBottomSheet : LessonPreviewEffect
}