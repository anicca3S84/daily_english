package com.codework.dailyenglish.domain.ai.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.repository.AiRepository
import javax.inject.Inject

class SendAgentMessageUseCase @Inject constructor(
    private val aiRepository: AiRepository
) {
    suspend operator fun invoke(
        sessionId: String,
        message: String,
        topicId: String,
        topicName: String,
        isNeedSuggestions: Boolean
    ): AppResult<ChatMessage> {
        return aiRepository.sendAgentMessage(
            sessionId = sessionId,
            message = message,
            topicId = topicId,
            topicName = topicName,
            isNeedSuggestions = isNeedSuggestions
        )
    }
}