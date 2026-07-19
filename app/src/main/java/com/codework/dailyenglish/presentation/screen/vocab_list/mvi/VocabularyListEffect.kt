package com.codework.dailyenglish.presentation.screen.vocab_list.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface VocabularyListEffect : BaseEffect {
    object NavigateToFlashCard : VocabularyListEffect
    data class NavigateToWordDetail(val wordId: String) : VocabularyListEffect

}