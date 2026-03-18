package com.codework.daily_english.presentation.screen.lessons.sentence_builder

import java.util.UUID

// --- DATA MODELS ---
data class WordChip(
    val id: String = UUID.randomUUID().toString(),
    val text: String
)

data class SentenceBuilderQuestion(
    val id: String = UUID.randomUUID().toString(),
    val vietnamese: String,
    val correctSentence: List<String> = emptyList(),
    val grammarTitle: String = "",
    val grammarContent: String = "",
    val grammarExample: String = ""
)

// 1. STATE (Gom SentenceBuilderData vào đây)
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

// 2. EVENT
sealed interface SentenceBuilderEvent {
    data object OnGoBack : SentenceBuilderEvent
    data object OnToggleTheory : SentenceBuilderEvent
    data object OnCheckAnswer : SentenceBuilderEvent
    data object OnNextQuestion : SentenceBuilderEvent
    data class OnSelectWord(val wordChip: WordChip) : SentenceBuilderEvent
    data class OnDeselectWord(val wordChip: WordChip) : SentenceBuilderEvent
    data object OnRetry : SentenceBuilderEvent
}

// 3. EFFECT
sealed interface SentenceBuilderEffect {
    data object NavigateBack : SentenceBuilderEffect
    data object NavigateToSpeaking : SentenceBuilderEffect
    data class ShowToast(val message: String) : SentenceBuilderEffect
}