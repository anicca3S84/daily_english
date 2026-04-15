package com.codework.dailyenglish.domain.translate.model

data class Translation(
    val id: Long = 0,
    val sourceText: String,
    val targetText: String,
    val sourceLang: String,
    val targetLang: String,
    val timestamp: Long = System.currentTimeMillis()
)