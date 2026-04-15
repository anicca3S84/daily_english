package com.codework.dailyenglish.presentation.screen.lessons.review.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface QuizReviewEffect: BaseEffect {
    data object NavigateBack : QuizReviewEffect
    data object NavigateToListening : QuizReviewEffect
    data object NavigateToGrammar : QuizReviewEffect
}