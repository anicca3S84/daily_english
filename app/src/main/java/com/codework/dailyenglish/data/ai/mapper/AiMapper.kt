package com.codework.dailyenglish.data.ai.mapper

import com.codework.dailyenglish.data.ai.remote.dto.ChatResponseDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatSessionDto
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.model.ChatSession

object AiMapper {
    fun mapToDomain(dto: ChatSessionDto): ChatSession {
        return ChatSession(
            id = dto.id.toString(),
            title = dto.title,
            lastMessage = dto.lastMessage ?: "No messages yet",
            date = dto.date ?: "Just now"
        )
    }

    fun mapToDomain(dto: ChatResponseDto): ChatMessage {
        return ChatMessage(
            text = dto.aiReply,
            isUser = false,
            suggestions = dto.suggestions.orEmpty()
        )
    }
}