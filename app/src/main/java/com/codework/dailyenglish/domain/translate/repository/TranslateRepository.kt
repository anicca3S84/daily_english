package com.codework.dailyenglish.domain.translate.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.translate.model.Translation
import kotlinx.coroutines.flow.Flow

interface TranslateRepository {
    suspend fun translateText(text: String, sourceLang: String, targetLang: String): AppResult<String>
    suspend fun saveTranslationHistory(translation: Translation)
    suspend fun clearAllHistory(): AppResult<Unit>
    fun observeHistory(): Flow<List<Translation>>
}