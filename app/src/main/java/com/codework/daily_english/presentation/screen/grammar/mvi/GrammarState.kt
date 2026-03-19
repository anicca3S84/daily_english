package com.codework.daily_english.presentation.screen.grammar.mvi

import com.codework.daily_english.presentation.screen.grammar.model.GrammarTip
import com.codework.daily_english.presentation.screen.grammar.model.GrammarTopic

data class GrammarState(
    val selectedLevel: String = "All",
    val grammarTopics: List<GrammarTopic> = emptyList(),
    val dailyTip: GrammarTip? = null
)