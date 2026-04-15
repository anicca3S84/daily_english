package com.codework.dailyenglish.presentation.screen.translate.mvi

sealed interface TranslateEvent {
    data class OnTextChange(val text: String) : TranslateEvent
    data object OnTranslateClick : TranslateEvent
    data object OnSwapLanguage : TranslateEvent
    data object OnClearText : TranslateEvent
    data object OnCameraClick : TranslateEvent
    data object OnImageClick : TranslateEvent
    data object OnMicClick : TranslateEvent
    data object OnSeeAllClick : TranslateEvent
}