package com.codework.dailyenglish.presentation.screen.ai_speaking.mvi

sealed interface AiSpeakingEvent {
    data class OnSendMessage(val text: String) : AiSpeakingEvent
    data class OnToggleRecording(val context: android.content.Context) : AiSpeakingEvent
    object OnToggleSuggestions : AiSpeakingEvent
    object OnRetryChat : AiSpeakingEvent
    object OnStopRecording : AiSpeakingEvent
}