package com.codework.daily_english.data.translate.local.datasource

import com.codework.daily_english.data.translate.local.TranslationDao
import com.codework.daily_english.data.translate.local.TranslationEntity
import jakarta.inject.Inject

class TranslateLocalDataSourceImpl @Inject constructor(
    private val dao: TranslationDao
) : TranslateLocalDataSource {
    override suspend fun insert(entity: TranslationEntity) = dao.insertTranslation(entity)
    override fun getAllHistory() = dao.getAllHistory()
    override fun getRecentHistory() = dao.getRecentHistory()
}