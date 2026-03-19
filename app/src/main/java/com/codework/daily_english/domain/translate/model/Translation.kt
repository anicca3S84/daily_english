package com.codework.daily_english.domain.translate.model

data class Translation(
    val id: String = "",
    val sourceText: String,
    val targetText: String,
    val sourceLang: String,
    val targetLang: String,
    val timestamp: Long = System.currentTimeMillis()
)