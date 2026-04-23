package com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface LessonEffect : BaseEffect {
    data object NavigateBack : LessonEffect
    data class NavigateToResult(val lessonId: String) : LessonEffect
    data class ShowQuitDialog(val currentProgress: Float) : LessonEffect
}