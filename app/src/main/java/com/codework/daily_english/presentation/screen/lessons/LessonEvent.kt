package com.codework.daily_english.presentation.screen.lessons

sealed class LessonEvent {
    // Click vào một bước cụ thể
    data class OnStepClick(val stepId: String) : LessonEvent()

    // Click nút Back
    object OnBackClick : LessonEvent()

    // Click nút thử lại khi lỗi mạng
    object OnRetry : LessonEvent()

    // Nếu bạn muốn giữ tính năng Start Practice tổng hợp
    object OnStartPractice : LessonEvent()
}