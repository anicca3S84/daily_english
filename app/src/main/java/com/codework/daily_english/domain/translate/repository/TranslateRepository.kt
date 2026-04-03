package com.codework.daily_english.domain.translate.repository

import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.domain.translate.model.Translation
import kotlinx.coroutines.flow.Flow

interface TranslateRepository {
    suspend fun translateText(text: String, sourceLang: String, targetLang: String): AppResult<String>
    suspend fun saveTranslationHistory(translation: Translation)

    fun observeHistory(): Flow<List<Translation>>
}