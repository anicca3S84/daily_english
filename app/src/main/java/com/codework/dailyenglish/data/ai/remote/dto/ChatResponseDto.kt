package com.codework.dailyenglish.data.ai.remote.dto

data class ChatResponseDto(
    val sessionId: String,
    val aiReply: String,
    val timestamp: String,
    val suggestions: List<String>? = null
)