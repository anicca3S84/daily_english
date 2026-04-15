package com.codework.dailyenglish.domain.translate.usecase

import com.codework.dailyenglish.domain.translate.model.Translation
import com.codework.dailyenglish.domain.translate.repository.TranslateRepository
import javax.inject.Inject

class SaveTranslationHistoryUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    suspend operator fun invoke(item: Translation) {
        repository.saveTranslationHistory(item)
    }
}