package com.codework.daily_english.data.translate.datasource.local

import com.codework.daily_english.data.translate.local.TranslationDao
import com.codework.daily_english.data.translate.local.entity.TranslationEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface TranslateLocalDataSource {
    suspend fun insert(entity: TranslationEntity)
    fun getAll(): Flow<List<TranslationEntity>>
}

class TranslateLocalDataSourceImpl @Inject constructor(
    private val dao: TranslationDao
) : TranslateLocalDataSource {
    override suspend fun insert(entity: TranslationEntity) = dao.insertTranslation(entity)
    override fun getAll() = dao.getAllHistory()
}