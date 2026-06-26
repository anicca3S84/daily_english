package com.codework.dailyenglish.domain.vocabulary.usecase


import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.vocabulary.repository.VocabularyRepository
import javax.inject.Inject

class GetVocabulariesByTopicUseCase @Inject constructor(
    private val repository: VocabularyRepository
) {
    suspend operator fun invoke(topicPublicId: String): AppResult<List<Word>> {
        return repository.getVocabulariesByTopic(topicPublicId)
    }
}