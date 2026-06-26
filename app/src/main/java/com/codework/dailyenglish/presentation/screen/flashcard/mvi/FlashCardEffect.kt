package com.codework.dailyenglish.presentation.screen.flashcard.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface FlashcardEffect : BaseEffect {
    object NavigateBack : FlashcardEffect
}