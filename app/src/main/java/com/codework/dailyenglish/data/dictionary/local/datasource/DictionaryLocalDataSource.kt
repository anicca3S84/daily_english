package com.codework.dailyenglish.data.dictionary.local.datasource

import com.codework.dailyenglish.data.dictionary.local.RecentWordSearchEntity
import kotlinx.coroutines.flow.Flow

interface DictionaryLocalDataSource {
    suspend fun insert(entity: RecentWordSearchEntity)
    fun getRecentSearches(): Flow<List<RecentWordSearchEntity>>
    suspend fun delete(wordId: String)
    suspend fun clearAll()
}