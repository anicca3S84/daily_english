package com.codework.daily_english.presentation.screen.lessons.lesson.mvi

sealed interface LessonEvent {
    data class OnStepClick(val stepId: String) : LessonEvent
    data object OnRetry : LessonEvent
    data object OnStartPractice : LessonEvent
}