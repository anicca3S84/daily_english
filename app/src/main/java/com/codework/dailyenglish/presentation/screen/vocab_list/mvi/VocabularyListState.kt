package com.codework.dailyenglish.presentation.screen.vocab_list.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.dictionary.model.Word

data class VocabularyListState(
    val title: String = "",
    val isSavedMode: Boolean = true,
    val vocabWords: BaseUiState<List<Word>> = BaseUiState(),
    val selectedWordIds: Set<String> = emptySet()
)