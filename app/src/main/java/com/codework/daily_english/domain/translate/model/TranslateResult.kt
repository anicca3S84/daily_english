package com.codework.daily_english.domain.translate.model

data class TranslateResult(
    val translatedText: String,
    val historyItem: Translation?
)