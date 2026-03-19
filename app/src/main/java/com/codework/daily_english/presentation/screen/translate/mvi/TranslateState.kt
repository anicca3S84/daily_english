package com.codework.daily_english.presentation.screen.translate.mvi

import com.codework.daily_english.presentation.screen.translate.model.TranslationUiModel

data class TranslateState(
    val sourceText: String = "",
    val targetText: String = "",
    val sourceLang: String = "English",
    val targetLang: String = "Vietnamese",
    val isTranslating: Boolean = false,
    val error: String? = null,

    val history: List<TranslationUiModel> = emptyList()
)