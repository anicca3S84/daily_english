package com.codework.dailyenglish.domain.vocabulary.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic

interface VocabularyRepository {
    suspend fun getVocabTopics(): AppResult<List<VocabTopic>>
    suspend fun getVocabulariesByTopic(
        topicPublicId: String,
        page: Int = 0,
        size: Int = 10
    ): AppResult<List<Word>>

    suspend fun getWordDetail(wordId: String): AppResult<Word>

}