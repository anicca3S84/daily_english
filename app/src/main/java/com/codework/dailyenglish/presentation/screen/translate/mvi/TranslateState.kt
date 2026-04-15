package com.codework.dailyenglish.presentation.screen.translate.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.presentation.screen.translate.model.TranslationUi

data class TranslateState(
    val historyState: BaseUiState<List<TranslationUi>> = BaseUiState(),
    val sourceText: String = "",
    val targetText: String = "",
    val isTranslating: Boolean = false,
    val sourceLang: String = "en",
    val targetLang: String = "vi"
)