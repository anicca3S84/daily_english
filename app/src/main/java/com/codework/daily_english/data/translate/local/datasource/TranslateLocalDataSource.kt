package com.codework.daily_english.data.translate.local.datasource

import com.codework.daily_english.data.translate.local.TranslationEntity
import kotlinx.coroutines.flow.Flow

interface TranslateLocalDataSource {
    suspend fun insert(entity: TranslationEntity)
    fun getAllHistory(): Flow<List<TranslationEntity>>
    fun getRecentHistory(): Flow<List<TranslationEntity>>
}