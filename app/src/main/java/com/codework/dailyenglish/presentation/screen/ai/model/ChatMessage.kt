package com.codework.dailyenglish.presentation.screen.ai.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean, // true = User, false = AI
    val timestamp: Long = System.currentTimeMillis()
)