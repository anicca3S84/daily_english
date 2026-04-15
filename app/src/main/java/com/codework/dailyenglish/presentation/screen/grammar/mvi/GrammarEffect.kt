package com.codework.dailyenglish.presentation.screen.grammar.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface GrammarEffect: BaseEffect {
    data object NavigateBack : GrammarEffect
    data class NavigateToTopic(val id: String) : GrammarEffect
    data object OpenSearch : GrammarEffect
    data class ShowToast(val message: String) : GrammarEffect
}