package com.codework.dailyenglish.data.vocabulary.local


import com.codework.dailyenglish.domain.dictionary.model.Word
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlashcardDataStore @Inject constructor() {
    private var _words: List<Word> = emptyList()

    var words: List<Word>
        get() = _words
        set(value) { _words = value }

    fun clear() {
        _words = emptyList()
    }
}