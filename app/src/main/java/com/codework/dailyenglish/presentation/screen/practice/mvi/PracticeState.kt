package com.codework.dailyenglish.presentation.screen.practice.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic
import com.codework.dailyenglish.presentation.screen.practice.model.PracticeCategory

data class PracticeState(
    val categories: List<PracticeCategory> = emptyList(),
    val isPremium: Boolean = false,
    val topicsState: BaseUiState<List<VocabTopic>> = BaseUiState(),
)