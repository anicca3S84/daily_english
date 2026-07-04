package com.codework.dailyenglish.domain.dictionary.usecase

import com.codework.dailyenglish.data.dictionary.local.datasource.DictionaryLocalDataSource

import javax.inject.Inject

class ClearRecentSearchesUseCase @Inject constructor(
    private val dataSource: DictionaryLocalDataSource
) {
    suspend operator fun invoke() {
        dataSource.clearAll()
    }
}