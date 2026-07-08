package com.codework.dailyenglish.presentation.screen.ai_speaking.mvi

sealed interface AiSpeakingEvent {
    data class OnSendMessage(val text: String) : AiSpeakingEvent
    object OnToggleRecording : AiSpeakingEvent
    object OnToggleSuggestions : AiSpeakingEvent
    object OnRetryChat : AiSpeakingEvent // Event cho nút "Thử lại" khi lỗi kết nối mạng (StateWrapper trigger)
}