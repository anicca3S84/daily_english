package com.codework.daily_english.presentation.screen.home.model

data class DailyStudyTime(
    val dayName: String, // Mon, Tue, Wed...
    val minutes: Int,    // 30, 45, 0...
    val isToday: Boolean = false // Để highlight ngày hiện tại
)