package com.codework.dailyenglish.data.dictionary.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.data.dictionary.remote.DictionaryApi
import com.codework.dailyenglish.data.dictionary.remote.dto.WordDto
import com.codework.dailyenglish.data.remote.response.PageDto
import javax.inject.Inject

class DictionaryRemoteDataSourceImpl @Inject constructor(
    private val api: DictionaryApi
) : DictionaryRemoteDataSource {

    override suspend fun searchWord(query: String): AppResult<PageDto<WordDto>> {
        return safeApiCall { api.searchWord(query) }.unwrap()
    }

    override suspend fun getSavedWords(): AppResult<PageDto<WordDto>> {
        return safeApiCall { api.getSavedWords() }.unwrap()
    }

    override suspend fun saveVocabulary(id: String): AppResult<Unit> {
        return safeApiCall { api.toggleSaveVocabulary(id)}
    }
}