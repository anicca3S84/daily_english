package com.codework.dailyenglish.domain.vocabulary.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic
import com.codework.dailyenglish.domain.vocabulary.repository.VocabularyRepository
import javax.inject.Inject

class GetVocabTopicsUseCase @Inject constructor(
    private val repository: VocabularyRepository
) {
    suspend operator fun invoke(): AppResult<List<VocabTopic>> {
        return repository.getVocabTopics()
    }
}