package com.codework.daily_english.presentation.screen.lessons.review


sealed interface QuizReviewEvent {
    data class OnSelectOption(val index: Int) : QuizReviewEvent
    data object OnNextQuestion : QuizReviewEvent
    data object OnGoBack : QuizReviewEvent
    data object OnNavigateToListeningScreen : QuizReviewEvent
    data object OnNavigateToGrammarScreen : QuizReviewEvent
}