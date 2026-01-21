package com.codework.daily_english.presentation.screen.ai

import com.codework.daily_english.presentation.model.ChatMessage
import com.codework.daily_english.presentation.model.ChatSession

data class AiData(
    val messages: List<ChatMessage> = emptyList(),
    val historySessions: List<ChatSession> = emptyList(), // Danh sách lịch sử
    val isTyping: Boolean = false,
    val currentSessionId: String? = null // ID bài chat đang mở
)