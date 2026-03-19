package com.codework.daily_english.presentation.screen.dictionary.mvi

import com.codework.daily_english.presentation.screen.dictionary.model.Word

sealed interface DictionaryEvent {
    data class OnSearchQueryChange(val query: String) : DictionaryEvent
    data class OnWordClick(val word: Word) : DictionaryEvent
    data class OnPlayAudio(val word: String, val accent: String) : DictionaryEvent // Thêm accent để biết là UK hay US
    data object OnClearSearch : DictionaryEvent

    // Các Event bổ sung cho Dashboard UI của bạn
    data class OnQuickActionClick(val action: String) : DictionaryEvent
    data class OnTopicClick(val topic: String) : DictionaryEvent
    data class OnToggleSaveWord(val word: Word) : DictionaryEvent
}