package com.codework.dailyenglish.domain.ai.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.model.ChatSession

interface AiRepository {
    suspend fun getHistorySessions(type: String): AppResult<List<ChatSession>>

    suspend fun sendMessage(
        sessionId: String,
        message: String,
        isNeedSuggestions: Boolean
    ): AppResult<ChatMessage>
    suspend fun getSessionMessages(sessionId: String): AppResult<List<ChatMessage>>

    suspend fun sendAgentMessage(
        sessionId: String,
        message: String,
        topicId: String,
        topicName: String,
        isNeedSuggestions: Boolean
    ): AppResult<ChatMessage>

    suspend fun getAgentHistorySessions(type: String, topicId: String?): AppResult<List<ChatSession>>

}