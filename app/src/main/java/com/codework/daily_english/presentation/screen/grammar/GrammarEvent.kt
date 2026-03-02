package com.codework.daily_english.presentation.screen.grammar

sealed interface GrammarEvent {
    data class OnTopicClick(val topicId: String) : GrammarEvent
    data class OnLevelFilterSelected(val level: String) : GrammarEvent
    data object OnBackClick : GrammarEvent
    data object OnSearchClick : GrammarEvent
}