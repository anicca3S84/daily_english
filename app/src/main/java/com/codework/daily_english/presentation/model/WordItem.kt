package com.codework.daily_english.presentation.model

import java.util.UUID

data class WordItem(
    val id: String = UUID.randomUUID().toString(),
    val word: String,
    val phoneticUk: String, // Đổi từ phonetic thành phoneticUk
    val phoneticUs: String, // Thêm phoneticUs
    val type: String,
    val definition: String,
    val example: String,
    val isSaved: Boolean = false // Thêm trạng thái đã lưu hay chưa
)