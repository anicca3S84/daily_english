package com.codework.dailyenglish.data.ai.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.ai.remote.dto.ChatMessageDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatRequestDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatResponseDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatSessionDto

interface AiRemoteDataSource {
    suspend fun getSessions(type: String): AppResult<List<ChatSessionDto>>
    suspend fun sendMessage(request: ChatRequestDto): AppResult<ChatResponseDto>
    suspend fun getSessionMessages(sessionId: String): AppResult<List<ChatMessageDto>>
    suspend fun sendAgentMessage(request: ChatRequestDto): AppResult<ChatResponseDto>
    suspend fun getAgentSessions(type: String, topicId: String?): AppResult<List<ChatSessionDto>>

}