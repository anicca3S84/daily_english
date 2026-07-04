package com.codework.dailyenglish.domain.dictionary.usecase

import com.codework.dailyenglish.data.dictionary.local.datasource.DictionaryLocalDataSource
import com.codework.dailyenglish.domain.dictionary.model.RecentWord
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecentSearchesUseCase @Inject constructor(
    private val dataSource: DictionaryLocalDataSource
) {
    operator fun invoke(): Flow<List<RecentWord>> {
        return dataSource.getRecentSearches().map { entities ->
            entities.map { RecentWord(wordId = it.wordId, wordText = it.wordText) }
        }
    }
}