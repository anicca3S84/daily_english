package com.codework.dailyenglish.data.translate.repository

import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.translate.local.datasource.TranslateLocalDataSource
import com.codework.dailyenglish.data.translate.mapper.toDomain
import com.codework.dailyenglish.data.translate.mapper.toEntity
import com.codework.dailyenglish.data.translate.remote.datasource.TranslateRemoteDataSource
import com.codework.dailyenglish.data.translate.remote.dto.TranslateRequestDto
import com.codework.dailyenglish.domain.translate.model.Translation
import com.codework.dailyenglish.domain.translate.repository.TranslateRepository
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
        val responseResult = remoteDataSource.translate(
            translateRequest = TranslateRequestDto(
                q = text,
                source = sourceLang,
                target = targetLang
            )
        )
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