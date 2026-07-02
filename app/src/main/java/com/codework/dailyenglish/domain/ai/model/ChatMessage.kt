package com.codework.dailyenglish.domain.ai.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean, // true = User, false = AI
    val timestamp: Long = System.currentTimeMillis(),
    val suggestions: List<String> = emptyList()
)