package com.codework.dailyenglish.domain.dictionary.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.translate.repository.TranslateRepository
import jakarta.inject.Inject

class ClearTranslationHistoryUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    suspend operator fun invoke(): AppResult<Unit> {
        return repository.clearAllHistory()
    }
}