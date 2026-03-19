package com.codework.daily_english.presentation.screen.translate.model

data class TranslationUiModel(
    val id: String,
    val sourceText: String,
    val targetText: String,
    val formattedTime: String // Biến thời gian thành chuỗi đọc được
)