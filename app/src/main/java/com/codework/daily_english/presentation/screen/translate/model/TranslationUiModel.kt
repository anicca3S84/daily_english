package com.codework.daily_english.presentation.screen.translate.model

import com.codework.daily_english.presentation.utils.UiText

data class TranslationUiModel(
    val id: String,
    val sourceText: String,
    val targetText: String,
    val formattedTime: String,
    val error: UiText? = null,
)