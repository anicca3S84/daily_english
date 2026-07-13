package com.codework.dailyenglish.data.ai.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.data.ai.remote.AiApiService
import com.codework.dailyenglish.data.ai.remote.dto.ChatMessageDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatRequestDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatResponseDto
import com.codework.dailyenglish.data.ai.remote.dto.ChatSessionDto
import jakarta.inject.Inject

class AiRemoteDataSourceImpl @Inject constructor(
    private val apiService: AiApiService
) : AiRemoteDataSource {

    override suspend fun getSessions(): AppResult<List<ChatSessionDto>> {
        return safeApiCall { apiService.getSessions() }.map { it.data.orEmpty() }
    }

    override suspend fun sendMessage(request: ChatRequestDto): AppResult<ChatResponseDto> {
        return safeApiCall { apiService.sendMessage(request) }.map {
            it.data ?: throw Exception("Data is null")
        }
    }

    override suspend fun getSessionMessages(sessionId: String): AppResult<List<ChatMessageDto>> {
        return safeApiCall { apiService.getSessionMessages(sessionId) }.map {
            it.data ?: throw Exception("Data is null")
        }
    }
}