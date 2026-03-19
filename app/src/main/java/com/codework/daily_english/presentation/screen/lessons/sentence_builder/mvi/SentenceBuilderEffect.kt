package com.codework.daily_english.presentation.screen.lessons.sentence_builder.mvi

sealed interface SentenceBuilderEffect {
    data object NavigateBack : SentenceBuilderEffect
    data object NavigateToSpeaking : SentenceBuilderEffect
    data class ShowToast(val message: String) : SentenceBuilderEffect
}