package com.codework.dailyenglish.domain.dictionary.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.dictionary.repository.DictionaryRepository
import jakarta.inject.Inject


class ToggleSaveWordUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {
    suspend operator fun invoke(vocabId: String): AppResult<Unit> {
        return repository.saveVocabulary(vocabId)
    }
}