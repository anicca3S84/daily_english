package com.codework.daily_english.presentation.screen.lessons

data class LessonData(
    val lessonId: String,
    val title: String,       // Ví dụ: "At the Coffee Shop"
    val description: String, // Mô tả ngắn
    val imageUrl: String,    // Ảnh bìa
    val totalProgress: Float, // 0.0 -> 1.0 (Để vẽ thanh tiến độ tổng)
    val steps: List<LessonStep>, // DANH SÁCH CÁC BƯỚC (Quan trọng nhất)
    val level: String
)