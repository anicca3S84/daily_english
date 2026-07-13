package com.codework.dailyenglish.presentation.screen.ai.mvi


sealed interface AiEvent {
    data class OnSendMessage(val message: String) : AiEvent
    data object OnNewChat : AiEvent
    data class OnLoadSession(val sessionId: String) : AiEvent
    data object OnImageClick : AiEvent
    data object OnDismissSubscriptionPopup : AiEvent
    data class OnSuggestionClick(val text: String) : AiEvent
    data class OnMicToggle(val context: android.content.Context) : AiEvent
    data object OnDismissLimitConfirmDialog : AiEvent
    data object OnConfirmLimitUpgrade : AiEvent
}