package com.codework.dailyenglish.domain.translate.model

data class TranslateResult(
    val translatedText: String,
    val historyItem: Translation?
)