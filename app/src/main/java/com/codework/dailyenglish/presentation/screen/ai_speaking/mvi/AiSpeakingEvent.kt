package com.codework.dailyenglish.presentation.screen.ai_speaking.mvi

sealed interface AiSpeakingEvent {
    data class OnSendMessage(val text: String) : AiSpeakingEvent
    object OnToggleRecording : AiSpeakingEvent
    object OnToggleSuggestions : AiSpeakingEvent
}