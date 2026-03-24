package com.codework.daily_english.presentation.screen.translate.mvi

import com.codework.daily_english.core.base.BaseUiState
import com.codework.daily_english.presentation.screen.translate.model.TranslationUiModel

data class TranslateState(
    val historyState: BaseUiState<List<TranslationUiModel>> = BaseUiState(),
    val sourceText: String = "",
    val targetText: String = "",
    val isTranslating: Boolean = false,
    val sourceLang: String = "en",
    val targetLang: String = "vi"
)