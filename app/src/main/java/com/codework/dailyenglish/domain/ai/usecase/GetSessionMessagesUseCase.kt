package com.codework.dailyenglish.domain.ai.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.ai.model.ChatMessage
import com.codework.dailyenglish.domain.ai.repository.AiRepository
import jakarta.inject.Inject

class GetSessionMessagesUseCase @Inject constructor(
    private val repository: AiRepository
) {
    suspend operator fun invoke(sessionId: String): AppResult<List<ChatMessage>> {
        return repository.getSessionMessages(sessionId)
    }
}