package com.codework.dailyenglish.domain.ai.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.model.ChatSession

interface AiRepository {
    suspend fun getHistorySessions(): AppResult<List<ChatSession>>

    suspend fun sendMessage(
        sessionId: String,
        message: String,
        topicId: String? = null,
        topicName: String? = null,
        isNeedSuggestions: Boolean      
    ): AppResult<ChatMessage>
    suspend fun getSessionMessages(sessionId: String): AppResult<List<ChatMessage>>
}