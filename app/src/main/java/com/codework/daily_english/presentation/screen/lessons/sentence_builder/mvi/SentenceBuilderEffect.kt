package com.codework.daily_english.presentation.screen.lessons.sentence_builder.mvi

import com.codework.daily_english.core.base.BaseEffect

sealed interface SentenceBuilderEffect: BaseEffect {
    data object NavigateBack : SentenceBuilderEffect
    data object NavigateToSpeaking : SentenceBuilderEffect
    data class ShowToast(val message: String) : SentenceBuilderEffect
}