package com.codework.daily_english.presentation.screen.ai

sealed class AiEvent {
    data class OnSendMessage(val message: String) : AiEvent()
    object OnNewChat : AiEvent() // Tạo đoạn chat mới
    data class OnLoadSession(val sessionId: String) : AiEvent() // Bấm vào lịch sử
    object OnImageClick : AiEvent() // Bấm icon ảnh
    object OnMicClick : AiEvent()   // Bấm icon Mic
    data class OnSuggestionClick(val text: String) : AiEvent() // Bấm gợi ý
}