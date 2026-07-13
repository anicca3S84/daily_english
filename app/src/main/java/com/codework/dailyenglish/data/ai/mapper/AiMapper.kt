package com.codework.dailyenglish.data.ai.mapper

import com.codework.dailyenglish.data.ai.remote.dto.ChatMessageDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatResponseDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatSessionDto
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.model.ChatSession

object AiMapper {
    fun mapToDomain(dto: ChatSessionDto): ChatSession {
        return ChatSession(
            id = dto.id,
            title = dto.title ?: "New Chat",
            lastMessage = dto.lastMessage ?: "",
            date = dto.date ?: ""
        )
    }

    fun mapToDomain(dto: ChatResponseDto): ChatMessage {
        return ChatMessage(
            text = dto.aiReply,
            isUser = false,
            suggestions = dto.suggestions.orEmpty(),
            sessionId = dto.sessionId,
            isPremium = dto.isPremium ?: false,
            remainingMessages = dto.remainingMessages ?: 10,
            activeCharLimit = dto.activeCharLimit ?: 150
        )
    }

    fun mapToDomain(dto: ChatMessageDto, sessionId: String): ChatMessage {
        return ChatMessage(
            text = dto.content,
            isUser = dto.senderType == "USER",
            sessionId = sessionId
        )
    }
}