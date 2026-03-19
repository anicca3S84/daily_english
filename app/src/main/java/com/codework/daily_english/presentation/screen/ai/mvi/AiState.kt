package com.codework.daily_english.presentation.screen.ai.mvi

import com.codework.daily_english.presentation.screen.ai.model.ChatMessage
import com.codework.daily_english.presentation.screen.ai.model.ChatSession

data class AiState(
    val messages: List<ChatMessage> = emptyList(),
    val historySessions: List<ChatSession> = emptyList(),
    val isTyping: Boolean = false,
    val currentSessionId: String? = null
)