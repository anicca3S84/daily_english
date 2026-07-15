package com.codework.dailyenglish.presentation.screen.ai_speaking.mvi

sealed interface AiSpeakingEvent {
    data class OnSendMessage(val text: String) : AiSpeakingEvent
    data class OnToggleRecording(val context: android.content.Context) : AiSpeakingEvent
    data object OnToggleSuggestions : AiSpeakingEvent
    data object OnRetryChat : AiSpeakingEvent
    data object OnStopRecording : AiSpeakingEvent

    // BỔ SUNG SỰ KIỆN LỊCH SỬ
    data object OnNewChat : AiSpeakingEvent
    data class OnLoadSession(val sessionId: String) : AiSpeakingEvent

    data object OnDismissLimitConfirmDialog : AiSpeakingEvent
    data object OnConfirmLimitUpgrade : AiSpeakingEvent
    data object OnDismissSubscriptionPopup : AiSpeakingEvent
}