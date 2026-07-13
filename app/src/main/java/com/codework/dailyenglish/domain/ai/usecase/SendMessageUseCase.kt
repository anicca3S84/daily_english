package com.codework.dailyenglish.domain.ai.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.repository.AiRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val aiRepository: AiRepository
) {
    suspend operator fun invoke(
        sessionId: String,
        message: String,
        isNeedSuggestions: Boolean
    ): AppResult<ChatMessage> {
        return aiRepository.sendMessage(
            sessionId = sessionId,
            message = message,
            isNeedSuggestions = isNeedSuggestions
        )
    }
}