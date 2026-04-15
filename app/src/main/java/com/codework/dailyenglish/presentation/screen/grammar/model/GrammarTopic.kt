package com.codework.dailyenglish.presentation.screen.grammar.model

data class GrammarTopic(
    val id: String,
    val title: String,
    val description: String,
    val level: String,
    val progress: Float,
    val isLocked: Boolean = false,
    val iconUrl: String = ""
)