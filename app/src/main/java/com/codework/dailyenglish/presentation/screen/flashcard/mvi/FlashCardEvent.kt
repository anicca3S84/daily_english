package com.codework.dailyenglish.presentation.screen.flashcard.mvi

import com.codework.dailyenglish.domain.lesson.model.StepData

sealed interface FlashcardEvent {
    object OnCardClick : FlashcardEvent
    object OnToggleSaveClick : FlashcardEvent
    object OnKnowItClick : FlashcardEvent
    object OnStillLearningClick : FlashcardEvent
    object OnPlayAudio : FlashcardEvent
    object OnReviewForgotWords : FlashcardEvent
    object OnForceFinish : FlashcardEvent
}