package com.codework.daily_english.domain.translate.usecase

import com.codework.daily_english.domain.translate.model.Translation
import com.codework.daily_english.domain.translate.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTranslationHistoryUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    // Dùng Flow để lắng nghe liên tục mọi thay đổi từ Database
    operator fun invoke(): Flow<List<Translation>> {
        return repository.observeHistory()
    }
}