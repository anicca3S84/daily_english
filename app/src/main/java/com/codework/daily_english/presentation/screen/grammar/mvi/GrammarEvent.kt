package com.codework.daily_english.presentation.screen.grammar.mvi

sealed interface GrammarEvent {
    data class OnTopicClick(val topicId: String) : GrammarEvent
    data class OnLevelFilterSelected(val level: String) : GrammarEvent
    data object OnSearchClick : GrammarEvent
    data object OnBackClick : GrammarEvent
    data object OnRetry : GrammarEvent
}
