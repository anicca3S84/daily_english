package com.codework.daily_english.presentation.model

data class LessonItem(
    val id: String,
    val title: String,
    val progress: Float, // 0.0 - 1.0
    val topicIcon: String // Giả sử là tên icon hoặc url ảnh
)