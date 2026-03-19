package com.codework.daily_english.presentation.screen.lessons.sentence_builder.mvi

import com.codework.daily_english.presentation.screen.lessons.sentence_builder.model.SentenceBuilderQuestion
import com.codework.daily_english.presentation.screen.lessons.sentence_builder.model.WordChip

data class SentenceBuilderState(
    val questions: List<SentenceBuilderQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val progressCurrent: Int = 0,
    val progressTotal: Int = 0,
    val currentQuestion: SentenceBuilderQuestion? = null,
    val selectedWords: List<WordChip> = emptyList(),
    val availableWords: List<WordChip> = emptyList(),
    val isTheoryVisible: Boolean = true,
    val isCorrect: Boolean? = null,
    val isCompleted: Boolean = false
)