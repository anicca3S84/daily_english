package com.codework.daily_english.presentation.screen.dictionary

import com.codework.daily_english.presentation.model.WordItem

sealed class DictionaryEvent {
    data class OnSearchQueryChange(val query: String) : DictionaryEvent()
    data class OnWordClick(val word: WordItem) : DictionaryEvent()
    data class OnPlayAudio(val word: String) : DictionaryEvent() // Giả lập phát âm
    object OnClearSearch : DictionaryEvent()
}