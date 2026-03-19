package com.codework.daily_english.presentation.screen.lessons.review.mvi

sealed interface QuizReviewEffect {
    data object NavigateBack : QuizReviewEffect
    data object NavigateToListening : QuizReviewEffect
    data object NavigateToGrammar : QuizReviewEffect
}