package com.codework.daily_english.presentation.screen.lessons.review.mvi

import com.codework.daily_english.core.base.BaseEffect

sealed interface QuizReviewEffect: BaseEffect {
    data object NavigateBack : QuizReviewEffect
    data object NavigateToListening : QuizReviewEffect
    data object NavigateToGrammar : QuizReviewEffect
}