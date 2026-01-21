package com.codework.daily_english.presentation.screen.translate

data class TranslateData(
    val sourceText: String = "",
    val targetText: String = "",
    val sourceLang: String = "English",
    val targetLang: String = "Vietnamese",
    val isTranslating: Boolean = false
)