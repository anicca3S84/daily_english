package com.codework.daily_english.presentation.screen.grammar

import com.codework.daily_english.presentation.model.GrammarTip
import com.codework.daily_english.presentation.model.GrammarTopic

// 1. STATE
data class GrammarState(
    val selectedLevel: String = "All",
    val grammarTopics: List<GrammarTopic> = emptyList(),
    val dailyTip: GrammarTip? = null
)

// 2. EVENT
sealed interface GrammarEvent {
    data class OnTopicClick(val topicId: String) : GrammarEvent
    data class OnLevelFilterSelected(val level: String) : GrammarEvent
    data object OnSearchClick : GrammarEvent
    data object OnBackClick : GrammarEvent
    data object OnRetry : GrammarEvent
}

// 3. EFFECT
sealed interface GrammarEffect {
    data object NavigateBack : GrammarEffect
    data class NavigateToTopic(val id: String) : GrammarEffect
    data object OpenSearch : GrammarEffect
    data class ShowToast(val message: String) : GrammarEffect
}