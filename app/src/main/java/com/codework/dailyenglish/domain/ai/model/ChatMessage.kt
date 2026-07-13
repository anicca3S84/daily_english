package com.codework.dailyenglish.domain.ai.model

import java.util.UUID

data class ChatMessage(
    val sessionId: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean, // true = User, false = AI
    val timestamp: Long = System.currentTimeMillis(),
    val suggestions: List<String> = emptyList(),

    val isPremium: Boolean = false,
    val remainingMessages: Int = 10,
    val activeCharLimit: Int = 150
)