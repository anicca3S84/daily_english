package com.codework.dailyenglish.presentation.screen.saved_word.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface SavedWordsEffect : BaseEffect {
    object NavigateToFlashCard : SavedWordsEffect
}