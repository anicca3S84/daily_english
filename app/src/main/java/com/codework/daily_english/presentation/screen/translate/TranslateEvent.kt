package com.codework.daily_english.presentation.screen.translate

sealed class TranslateEvent {
    data class OnTextChange(val text: String) : TranslateEvent()
    object OnTranslateClick : TranslateEvent()
    object OnSwapLanguage : TranslateEvent()
    object OnClearText : TranslateEvent()
}