package com.codework.dailyenglish.domain.translate.usecase

import com.codework.dailyenglish.domain.translate.model.Translation
import com.codework.dailyenglish.domain.translate.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTranslationHistoryUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    operator fun invoke(): Flow<List<Translation>> {
        return repository.observeHistory()
    }
}