package com.codework.dailyenglish.data.vocabulary.remote.repository

import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.vocabulary.mapper.toDomain
import com.codework.dailyenglish.data.vocabulary.remote.datasource.VocabularyRemoteDataSource
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic
import com.codework.dailyenglish.domain.vocabulary.repository.VocabularyRepository
import jakarta.inject.Inject
import kotlinx.coroutines.withContext

class VocabularyRepositoryImpl @Inject constructor(
    private val remoteDataSource: VocabularyRemoteDataSource,
    private val dispatchers: Dispatcher
) : VocabularyRepository {

    override suspend fun getVocabTopics(): AppResult<List<VocabTopic>> = withContext(dispatchers.io) {
        remoteDataSource.getVocabTopics().map { listDto ->
            listDto.map { dto ->
                VocabTopic(
                    id = dto.id,
                    name = dto.name,
                    imageUrl = dto.imageUrl.orEmpty()
                )
            }
        }
    }


    override suspend fun getVocabulariesByTopic(
        topicPublicId: String,
        page: Int,
        size: Int
    ): AppResult<List<Word>> = withContext(dispatchers.io) {
        remoteDataSource.getVocabulariesByTopic(topicPublicId, page, size).map { pageDto ->
            pageDto.content.map { it.toDomain() }
        }
    }
}