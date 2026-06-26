package com.codework.dailyenglish.presentation.screen.flashcard.model

import com.codework.dailyenglish.domain.lesson.model.StepData

data class FlashcardUiModel(
    val steps: List<StepData.Vocab>,
    val currentIndex: Int,
    val currentStep: Int,
    val totalSteps: Int,
    val progressPercent: Float,
    val progressText: String
)