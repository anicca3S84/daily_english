package com.codework.dailyenglish.data.ai.repository

import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.ai.mapper.AiMapper
import com.codework.dailyenglish.data.ai.remote.datasource.AiRemoteDataSource
import com.codework.dailyenglish.data.ai.remote.dto.ChatRequestDto
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.model.ChatSession
import com.codework.dailyenglish.domain.ai.repository.AiRepository
import jakarta.inject.Inject
import kotlinx.coroutines.withContext

class AiRepositoryImpl @Inject constructor(
    private val remoteDataSource: AiRemoteDataSource,
    private val dispatchers: Dispatcher
) : AiRepository {

    override suspend fun getHistorySessions(): AppResult<List<ChatSession>> = withContext(dispatchers.io) {
        remoteDataSource.getSessions().map { listDto ->
            listDto.map { AiMapper.mapToDomain(it) }
        }
    }

    override suspend fun sendMessage(
        sessionId: String,
        message: String,
        topicId: String?,
        topicName: String?,
        isNeedSuggestions: Boolean
    ): AppResult<ChatMessage> = withContext(dispatchers.io) {
        val requestDto = ChatRequestDto(
            sessionId = sessionId,
            message = message,
            topicId = topicId,
            topicName = topicName,
            isNeedSuggestions = isNeedSuggestions
        )
        remoteDataSource.sendMessage(requestDto).map { dto ->
            AiMapper.mapToDomain(dto)
        }
    }

    override suspend fun getSessionMessages(sessionId: String): AppResult<List<ChatMessage>> = withContext(dispatchers.io) {
        remoteDataSource.getSessionMessages(sessionId).map { listDto ->
            listDto.map { AiMapper.mapToDomain(it, sessionId) }
        }
    }
}