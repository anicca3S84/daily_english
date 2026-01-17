package com.codework.daily_english.presentation.model

data class TopicItem(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val totalLessons: Int, // Số lượng bài nhỏ bên trong
    val progress: Float,   // 0.0 - 1.0
    val isLocked: Boolean = false // Khóa nếu chưa đủ cấp độ
)