package com.codework.daily_english.data.translate.local

import com.codework.daily_english.data.translate.local.TranslationEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface TranslateLocalDataSource {
    suspend fun insert(entity: TranslationEntity)
    fun getAllHistory(): Flow<List<TranslationEntity>>
    fun getRecentHistory(): Flow<List<TranslationEntity>>
}

class TranslateLocalDataSourceImpl @Inject constructor(
    private val dao: TranslationDao
) : TranslateLocalDataSource {
    override suspend fun insert(entity: TranslationEntity) = dao.insertTranslation(entity)
    override fun getAllHistory() = dao.getAllHistory()
    override fun getRecentHistory() = dao.getRecentHistory()
}