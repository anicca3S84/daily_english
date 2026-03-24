package com.codework.daily_english.presentation.screen.lessons.lesson.mvi

import com.codework.daily_english.core.base.BaseEffect

sealed interface LessonEffect: BaseEffect {
    data object NavigateToVocabulary : LessonEffect
    data object NavigateToListening : LessonEffect
    data object NavigateToSentenceBuilder : LessonEffect
    data object NavigateToSpeaking : LessonEffect
    data object NavigateToQuiz : LessonEffect
    data class ShowToast(val message: String) : LessonEffect
}