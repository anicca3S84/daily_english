package com.codework.dailyenglish.domain.ai.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.repository.AiRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: AiRepository
) {
    suspend operator fun invoke(
        sessionId: String,
        message: String,
        topicId: String? = null,
        topicName: String? = null,
        isNeedSuggestions: Boolean
    ): AppResult<ChatMessage> {
        return repository.sendMessage(
            sessionId = sessionId,
            message = message,
            topicId = topicId,
            topicName = topicName,
            isNeedSuggestions = isNeedSuggestions
        )
    }
}