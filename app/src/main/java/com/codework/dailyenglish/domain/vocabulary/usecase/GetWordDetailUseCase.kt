package com.codework.dailyenglish.domain.vocabulary.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.vocabulary.repository.VocabularyRepository
import javax.inject.Inject

class GetWordDetailUseCase @Inject constructor(
    private val repository: VocabularyRepository
) {
    suspend operator fun invoke(wordId: String): AppResult<Word> {
        return repository.getWordDetail(wordId)
    }
}