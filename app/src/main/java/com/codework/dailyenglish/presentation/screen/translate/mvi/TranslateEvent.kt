package com.codework.dailyenglish.presentation.screen.translate.mvi

sealed interface TranslateEvent {
    data class OnTextChange(val text: String) : TranslateEvent
    data object OnClearText : TranslateEvent
    data object OnSwapLanguage : TranslateEvent
    data object OnCameraClick : TranslateEvent
    data object OnImageClick : TranslateEvent
    data object OnMicClick : TranslateEvent
    data object OnSeeAllClick : TranslateEvent
    // Sự kiện phục vụ tính năng "Nghe" (TTS)
    data object OnPlaySourceAudio : TranslateEvent
    data object OnPlayTargetAudio : TranslateEvent
}