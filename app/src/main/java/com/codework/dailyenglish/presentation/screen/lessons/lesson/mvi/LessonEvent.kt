package com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi

sealed interface LessonEvent {
    data class LoadLesson(val lessonId: String) : LessonEvent
    data object OnNextStep : LessonEvent
    data object OnQuitClicked : LessonEvent
    data object OnPreviousStep : LessonEvent
    data class OnOptionSelected(val optionId: String) : LessonEvent
    data object OnCheckAnswerClicked : LessonEvent
}