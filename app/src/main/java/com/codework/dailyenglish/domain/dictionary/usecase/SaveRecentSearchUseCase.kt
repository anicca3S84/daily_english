package com.codework.dailyenglish.domain.dictionary.usecase

import com.codework.dailyenglish.data.dictionary.local.RecentWordSearchEntity
import com.codework.dailyenglish.data.dictionary.local.datasource.DictionaryLocalDataSource
import jakarta.inject.Inject

class SaveRecentSearchUseCase @Inject constructor(
    private val dataSource: DictionaryLocalDataSource
) {
    suspend operator fun invoke(wordId: String, wordText: String) {
        val entity = RecentWordSearchEntity(
            wordId = wordId,
            wordText = wordText,
            timestamp = System.currentTimeMillis()
        )
        dataSource.insert(entity)
    }
}