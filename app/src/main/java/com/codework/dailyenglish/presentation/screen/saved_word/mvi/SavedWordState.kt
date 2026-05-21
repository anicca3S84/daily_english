package com.codework.dailyenglish.presentation.screen.saved_word.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.dictionary.model.Word

data class SavedWordsState(
    val savedWords: BaseUiState<List<Word>> = BaseUiState()
)