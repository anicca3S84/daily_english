package com.codework.dailyenglish.presentation.screen.translate.model

import com.codework.dailyenglish.presentation.utils.UiText

data class TranslationUi(
    val id: Long = 0,
    val sourceText: String,
    val targetText: String,
    val formattedTime: String,
    val error: UiText? = null,
)