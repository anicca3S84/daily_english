package com.codework.daily_english.data.translate.repository

import com.codework.daily_english.core.dispatchers.Dispatcher
import com.codework.daily_english.core.network.AppResult
import com.codework.daily_english.core.network.map
import com.codework.daily_english.data.translate.local.datasource.TranslateLocalDataSource
import com.codework.daily_english.data.translate.mapper.toDomain
import com.codework.daily_english.data.translate.mapper.toEntity
import com.codework.daily_english.data.translate.remote.datasource.TranslateRemoteDataSource
import com.codework.daily_english.domain.translate.model.Translation
import com.codework.daily_english.domain.translate.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TranslateRepositoryImpl @Inject constructor(
    private val remoteDataSource: TranslateRemoteDataSource,
    private val localDataSource: TranslateLocalDataSource,
    private val dispatchers: Dispatcher
) : TranslateRepository {

    override suspend fun translateText(
        text: String,
        sourceLang: String,
        targetLang: String
    ): AppResult<String> = withContext(dispatchers.io) {
        val responseResult = remoteDataSource.translate(text, sourceLang, targetLang)
        responseResult.map { dto ->
            dto.translatedText
        }
    }

    override suspend fun saveTranslationHistory(translation: Translation) =
        withContext(dispatchers.io) {
            localDataSource.insert(translation.toEntity())
        }

    override fun observeHistory(): Flow<List<Translation>> {
        return localDataSource.getRecentHistory()
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .flowOn(dispatchers.io)
    }
}