package com.codework.dailyenglish.data.vocabulary.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.data.dictionary.remote.dto.WordDto
import com.codework.dailyenglish.data.remote.response.PageDto
import com.codework.dailyenglish.data.vocabulary.remote.VocabularyApi
import com.codework.dailyenglish.data.vocabulary.remote.dto.VocabTopicDto
import jakarta.inject.Inject

class VocabularyRemoteDataSourceImpl @Inject constructor(
    private val api: VocabularyApi
) : VocabularyRemoteDataSource {
    override suspend fun getVocabTopics(): AppResult<List<VocabTopicDto>> {
        return safeApiCall { api.getVocabTopics() }.unwrap()
    }

    override suspend fun getVocabulariesByTopic(
        topicPublicId: String,
        page: Int,
        size: Int
    ): AppResult<PageDto<WordDto>> {
        return safeApiCall {
            api.getVocabulariesByTopic(topicPublicId, page, size)
        }.unwrap()
    }
}