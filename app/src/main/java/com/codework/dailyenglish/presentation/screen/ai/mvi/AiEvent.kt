package com.codework.dailyenglish.presentation.screen.ai.mvi

sealed interface AiEvent {
    data class OnSendMessage(val message: String) : AiEvent
    data object OnNewChat : AiEvent
    data class OnLoadSession(val sessionId: String) : AiEvent
    data object OnImageClick : AiEvent
    data object OnMicClick : AiEvent
    data class OnSuggestionClick(val text: String) : AiEvent
    object OnMicToggle : AiEvent
}