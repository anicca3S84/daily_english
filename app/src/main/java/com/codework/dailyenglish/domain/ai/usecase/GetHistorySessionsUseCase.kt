package com.codework.dailyenglish.domain.ai.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.ai.model.ChatSession
import com.codework.dailyenglish.domain.ai.repository.AiRepository
import jakarta.inject.Inject

class GetHistorySessionsUseCase @Inject constructor(
    private val repository: AiRepository
) {
    suspend operator fun invoke(type: String): AppResult<List<ChatSession>> {
        return repository.getHistorySessions(type)
    }
}