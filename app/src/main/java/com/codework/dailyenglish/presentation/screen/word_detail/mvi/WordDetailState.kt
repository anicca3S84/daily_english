package com.codework.dailyenglish.presentation.screen.word_detail.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.dictionary.model.Word

data class WordDetailState(
    val wordState: BaseUiState<Word> = BaseUiState()
)