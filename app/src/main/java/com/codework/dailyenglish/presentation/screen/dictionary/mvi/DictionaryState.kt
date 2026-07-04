package com.codework.dailyenglish.presentation.screen.dictionary.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.dictionary.model.RecentWord
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic

data class DictionaryState(
    val query: String = "",
    val searchResults: BaseUiState<List<Word>> = BaseUiState(),
    val topicsState: BaseUiState<List<VocabTopic>> = BaseUiState(),
    val recentSearches: List<RecentWord> = emptyList(),
)