package com.codework.dailyenglish.data.dictionary.repository

import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.dictionary.mapper.toDomain
import com.codework.dailyenglish.data.dictionary.remote.datasource.DictionaryRemoteDataSource
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.dictionary.repository.DictionaryRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val remoteDataSource: DictionaryRemoteDataSource,
    private val dispatchers: Dispatcher
) : DictionaryRepository {

    override suspend fun searchWord(query: String): AppResult<List<Word>> =
        withContext(dispatchers.io) {
            val responseResult = remoteDataSource.searchWord(query)

            responseResult.map { pageDto ->
                pageDto.content.map { it.toDomain() }
            }
        }

    override suspend fun getSavedWords(): AppResult<List<Word>> =
        withContext(dispatchers.io) {
            val responseResult = remoteDataSource.getSavedWords()

            responseResult.map { pageDto ->
                pageDto.content.map { it.toDomain() }
            }
        }

    override suspend fun saveVocabulary(id: String): AppResult<Unit> =
        withContext(dispatchers.io) {
            remoteDataSource.saveVocabulary(id)
        }

}