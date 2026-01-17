package com.codework.daily_english.presentation.model

data class LessonItem(
    val id: String,
    val title: String,
    val imageUrl: String,      // Ảnh bìa
    val lessonNum: Int,        // Ví dụ: 3
    val level: String,         // Ví dụ: "A1-B1"
    val progress: Float        // 0.0 đến 1.0 (Ví dụ: 0.6f)
)