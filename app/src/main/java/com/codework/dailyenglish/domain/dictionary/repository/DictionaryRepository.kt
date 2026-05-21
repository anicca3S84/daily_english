package com.codework.dailyenglish.domain.dictionary.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.dictionary.model.Word

interface DictionaryRepository {
    suspend fun searchWord(query: String): AppResult<List<Word>>
    suspend fun getSavedWords(): AppResult<List<Word>>
    suspend fun saveVocabulary(id: String): AppResult<Unit>
}