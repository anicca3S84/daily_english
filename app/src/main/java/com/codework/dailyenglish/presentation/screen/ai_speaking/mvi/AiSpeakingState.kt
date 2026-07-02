package com.codework.dailyenglish.presentation.screen.ai_speaking.mvi

import com.codework.dailyenglish.domain.ai.model.ChatMessage

data class AiSpeakingState(
    val messages: List<ChatMessage> = emptyList(),
    val isRecording: Boolean = false,
    val isTyping: Boolean = false,
    val topicName: String = "",
    val suggestions: List<String> = emptyList()
)