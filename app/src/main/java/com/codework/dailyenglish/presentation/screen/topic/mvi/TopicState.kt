package com.codework.dailyenglish.presentation.screen.topic.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic

data class TopicState(
    val topics: BaseUiState<List<VocabTopic>> = BaseUiState(),
    val isDictionary: Boolean = false,
    val searchQuery: String = ""
)