package com.codework.daily_english.data.translate.repository

import com.codework.daily_english.core.network.safeApiCall
import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.data.translate.local.TranslationDao
import com.codework.daily_english.data.translate.remote.TranslateApi
import com.codework.daily_english.data.translate.mapper.toDomain
import com.codework.daily_english.data.translate.mapper.toEntity
import com.codework.daily_english.domain.translate.model.Translation
import com.codework.daily_english.domain.translate.repository.TranslateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TranslateRepositoryImpl @Inject constructor(
    private val api: TranslateApi,
    private val dao: TranslationDao
) : TranslateRepository {

    override suspend fun translateText(
        text: String,
        sourceLang: String,
        targetLang: String
    ): AppResult<String> {
        return safeApiCall {
            api.translate(text, sourceLang, targetLang).translatedText
        }
    }

    override suspend fun saveTranslationHistory(translation: Translation) {
        dao.insertTranslation(translation.toEntity())
    }

    override fun observeHistory(): Flow<List<Translation>> {
        return dao.getAllHistory().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}