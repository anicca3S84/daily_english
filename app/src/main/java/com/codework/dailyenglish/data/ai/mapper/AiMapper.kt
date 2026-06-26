package com.codework.dailyenglish.data.ai.mapper

import com.codework.dailyenglish.data.ai.remote.dto.ChatSessionDto
import com.codework.dailyenglish.domain.ai.model.ChatSession

object AiMapper {
    fun mapToDomain(dto: ChatSessionDto): ChatSession {
        return ChatSession(
            id = dto.id.toString(),
            title = dto.title,
            // Nếu dto.lastMessage là null, dùng "No messages yet"
            lastMessage = dto.lastMessage ?: "No messages yet",
            // Nếu dto.date là null, dùng "Just now"
            date = dto.date ?: "Just now"
        )
    }
}