package com.codework.dailyenglish.presentation.screen.translate.mvi

import android.content.Context

sealed interface TranslateEvent {
    data class OnTextChange(val text: String) : TranslateEvent
    data object OnClearText : TranslateEvent
    data object OnSwapLanguage : TranslateEvent
    data object OnCameraClick : TranslateEvent
    data object OnImageClick : TranslateEvent
    data class OnMicClick(val context: Context) : TranslateEvent
    data object OnClearAllClick : TranslateEvent
    object OnCopySuccess : TranslateEvent
    object OnRefreshHistory : TranslateEvent
    data class OnHistoryItemClick(val source: String, val target: String) : TranslateEvent
    //  (TTS)
    data object OnPlaySourceAudio : TranslateEvent
    data object OnPlayTargetAudio : TranslateEvent
}