package com.codework.dailyenglish.data.dictionary.local.datasource

import com.codework.dailyenglish.data.dictionary.local.RecentWordSearchDao
import com.codework.dailyenglish.data.dictionary.local.RecentWordSearchEntity
import jakarta.inject.Inject

class DictionaryLocalDataSourceImpl @Inject constructor(
    private val dao: RecentWordSearchDao
) : DictionaryLocalDataSource {
    override suspend fun insert(entity: RecentWordSearchEntity) = dao.insertRecentSearch(entity)
    override fun getRecentSearches() = dao.getRecentSearches()
    override suspend fun delete(wordId: String) = dao.deleteRecentSearch(wordId)
    override suspend fun clearAll() = dao.clearAll() // Thêm hàm này
}