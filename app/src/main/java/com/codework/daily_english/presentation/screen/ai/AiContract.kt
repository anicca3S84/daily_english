package com.codework.daily_english.presentation.screen.ai

import com.codework.daily_english.presentation.model.ChatMessage
import com.codework.daily_english.presentation.model.ChatSession

// 1. STATE (Đổi AiData thành AiState)
data class AiState(
    val messages: List<ChatMessage> = emptyList(),
    val historySessions: List<ChatSession> = emptyList(),
    val isTyping: Boolean = false,
    val currentSessionId: String? = null
)

// 2. EVENT
sealed interface AiEvent {
    data class OnSendMessage(val message: String) : AiEvent
    data object OnNewChat : AiEvent
    data class OnLoadSession(val sessionId: String) : AiEvent
    data object OnImageClick : AiEvent
    data object OnMicClick : AiEvent
    data class OnSuggestionClick(val text: String) : AiEvent
}

// 3. EFFECT (Dành cho việc hiện Toast báo lỗi mạng hoặc điều hướng đặc biệt)
sealed interface AiEffect {
    data class ShowToast(val message: String) : AiEffect
    // Màn AI này chủ yếu xử lý tại chỗ, ít khi cần điều hướng (trừ khi bạn muốn mở camera/thư viện ảnh)
    data object OpenGallery : AiEffect
    data object OpenMicrophone : AiEffect
}