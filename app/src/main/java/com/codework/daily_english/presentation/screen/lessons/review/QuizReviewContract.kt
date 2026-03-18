package com.codework.daily_english.presentation.screen.lessons.review

import com.codework.daily_english.presentation.model.QuizType

// 1. STATE
data class QuizReviewState(
    val id: String = "",
    val sentence: String = "",
    val translatedSentence: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerRevealed: Boolean = false,
    val progressCurrent: Int = 0,
    val progressTotal: Int = 1,
    val type: QuizType = QuizType.VOCABULARY
)

// 2. EVENT
sealed interface QuizReviewEvent {
    data class OnSelectOption(val index: Int) : QuizReviewEvent
    data object OnNextQuestion : QuizReviewEvent
    data object OnGoBack : QuizReviewEvent
    data object OnNavigateToListeningScreen : QuizReviewEvent
    data object OnNavigateToGrammarScreen : QuizReviewEvent
}

// 3. EFFECT
sealed interface QuizReviewEffect {
    data object NavigateBack : QuizReviewEffect
    data object NavigateToListening : QuizReviewEffect
    data object NavigateToGrammar : QuizReviewEffect
}