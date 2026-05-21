package com.codework.dailyenglish.data.dictionary.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.dictionary.remote.dto.WordDto
import com.codework.dailyenglish.data.remote.response.PageDto

interface DictionaryRemoteDataSource {
    suspend fun searchWord(query: String): AppResult<PageDto<WordDto>>
    suspend fun getSavedWords(): AppResult<PageDto<WordDto>>
    suspend fun saveVocabulary(query: String): AppResult<Unit>
}