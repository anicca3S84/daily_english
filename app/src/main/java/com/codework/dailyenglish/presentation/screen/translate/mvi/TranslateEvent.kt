package com.codework.dailyenglish.presentation.screen.translate.mvi

sealed interface TranslateEvent {
    data class OnTextChange(val text: String) : TranslateEvent
    data object OnClearText : TranslateEvent
    data object OnSwapLanguage : TranslateEvent
    data object OnCameraClick : TranslateEvent
    data object OnImageClick : TranslateEvent
    data object OnMicClick : TranslateEvent
    data object OnClearAllClick : TranslateEvent
    object OnCopySuccess : TranslateEvent
    data class OnHistoryItemClick(val source: String, val target: String) : TranslateEvent
    //  (TTS)
    data object OnPlaySourceAudio : TranslateEvent
    data object OnPlayTargetAudio : TranslateEvent
}