package com.codework.daily_english.presentation.screen.lessons

sealed class LessonEvent {
    data class OnStepClick(val stepId: String) : LessonEvent()
    object OnRetry : LessonEvent()
    object OnStartPractice : LessonEvent()
}