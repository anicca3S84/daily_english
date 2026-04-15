package com.codework.dailyenglish.presentation.screen.lessons.review.mvi

sealed interface QuizReviewEvent {
    data class OnSelectOption(val index: Int) : QuizReviewEvent
    data object OnNextQuestion : QuizReviewEvent
    data object OnGoBack : QuizReviewEvent
    data object OnNavigateToListeningScreen : QuizReviewEvent
    data object OnNavigateToGrammarScreen : QuizReviewEvent
}