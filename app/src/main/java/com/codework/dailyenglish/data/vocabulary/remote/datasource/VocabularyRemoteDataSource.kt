package com.codework.dailyenglish.data.vocabulary.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.dictionary.remote.dto.WordDto
import com.codework.dailyenglish.data.remote.response.PageDto
import com.codework.dailyenglish.data.vocabulary.remote.dto.VocabTopicDto

interface VocabularyRemoteDataSource {
    suspend fun getVocabTopics(): AppResult<List<VocabTopicDto>>
    suspend fun getVocabulariesByTopic(
        topicPublicId: String,
        page: Int,
        size: Int
    ): AppResult<PageDto<WordDto>>

    suspend fun getWordDetail(wordId: String): AppResult<WordDto>
}