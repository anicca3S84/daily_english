// FILE: app/src/main/java/com/codework/dailyenglish/domain/dictionary/usecase/SearchWordUseCase.kt
package com.codework.dailyenglish.domain.dictionary.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.dictionary.repository.DictionaryRepository
import javax.inject.Inject

class SearchWordUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {
    suspend operator fun invoke(query: String): AppResult<List<Word>> {
        return repository.searchWord(query)
    }
}