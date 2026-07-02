package com.codework.dailyenglish.data.ai.remote.dto

import com.codework.dailyenglish.domain.ai.model.ChatMessage

data class ChatSessionDetailDto(
    val id: String,
    val title: String,
    val messages: List<ChatMessage> // Chứa toàn bộ list tin nhắn (của cả user và AI)
)