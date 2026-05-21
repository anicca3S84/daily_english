package com.codework.dailyenglish.domain.dictionary.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.dictionary.repository.DictionaryRepository
import jakarta.inject.Inject

class GetSavedWordUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {

    suspend operator fun invoke(): AppResult<List<Word>> {
        return repository.getSavedWords()
    }
}