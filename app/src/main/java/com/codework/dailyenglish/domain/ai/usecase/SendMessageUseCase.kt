package com.codework.dailyenglish.domain.ai.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.repository.AiRepository
import jakarta.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: AiRepository
) {
    suspend operator fun invoke(sessionId: String, message: String): AppResult<ChatMessage> {
        return repository.sendMessage(sessionId, message)
    }
}