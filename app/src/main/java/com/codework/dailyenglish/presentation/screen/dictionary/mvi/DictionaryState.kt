package com.codework.dailyenglish.presentation.screen.dictionary.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.dictionary.model.Word

data class DictionaryState(
    val query: String = "",
    val searchResults: BaseUiState<List<Word>> = BaseUiState(),

    val recentSearches: List<String> = listOf("Serendipity", "Ephemeral", "Petrichor"),
)