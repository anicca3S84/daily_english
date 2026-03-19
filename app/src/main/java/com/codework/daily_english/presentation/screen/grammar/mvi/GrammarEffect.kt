package com.codework.daily_english.presentation.screen.grammar.mvi

sealed interface GrammarEffect {
    data object NavigateBack : GrammarEffect
    data class NavigateToTopic(val id: String) : GrammarEffect
    data object OpenSearch : GrammarEffect
    data class ShowToast(val message: String) : GrammarEffect
}