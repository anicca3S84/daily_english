package com.codework.dailyenglish.data.ai.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.ai.remote.dto.ChatRequestDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatResponseDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatSessionDto

interface AiRemoteDataSource {
    suspend fun getSessions(): AppResult<List<ChatSessionDto>>
    suspend fun sendMessage(request: ChatRequestDto): AppResult<ChatResponseDto>
}