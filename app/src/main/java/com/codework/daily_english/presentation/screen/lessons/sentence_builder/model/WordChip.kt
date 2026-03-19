package com.codework.daily_english.presentation.screen.lessons.sentence_builder.model

import java.util.UUID

data class WordChip(
    val id: String = UUID.randomUUID().toString(),
    val text: String
)