package com.codework.daily_english.domain.translate.usecase

import com.codework.daily_english.domain.translate.model.Translation
import com.codework.daily_english.domain.translate.repository.TranslateRepository
import javax.inject.Inject

class SaveTranslationHistoryUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    suspend operator fun invoke(item: Translation) {
        repository.saveTranslationHistory(item)
    }
}