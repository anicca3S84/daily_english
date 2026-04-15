package com.codework.dailyenglish.presentation.screen.grammar.mvi

import com.codework.dailyenglish.presentation.screen.grammar.model.GrammarTip
import com.codework.dailyenglish.presentation.screen.grammar.model.GrammarTopic

data class GrammarState(
    val selectedLevel: String = "All",
    val grammarTopics: List<GrammarTopic> = emptyList(),
    val dailyTip: GrammarTip? = null
)