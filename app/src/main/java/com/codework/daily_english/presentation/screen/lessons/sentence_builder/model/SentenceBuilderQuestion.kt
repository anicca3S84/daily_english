package com.codework.daily_english.presentation.screen.lessons.sentence_builder.model

import java.util.UUID

data class SentenceBuilderQuestion(
    val id: String = UUID.randomUUID().toString(),
    val vietnamese: String,
    val correctSentence: List<String> = emptyList(),
    val grammarTitle: String = "",
    val grammarContent: String = "",
    val grammarExample: String = ""
)