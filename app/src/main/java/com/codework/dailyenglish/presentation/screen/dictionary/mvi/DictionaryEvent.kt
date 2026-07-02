package com.codework.dailyenglish.presentation.screen.dictionary.mvi

import com.codework.dailyenglish.domain.dictionary.model.Word

sealed interface DictionaryEvent {
    data class OnSearchQueryChange(val query: String) : DictionaryEvent
    data class OnWordClick(val word: Word) : DictionaryEvent
    data class OnPlayAudio(val word: String, val accent: String) : DictionaryEvent
    data object OnClearSearch : DictionaryEvent
    data class OnQuickActionClick(val action: String) : DictionaryEvent
    data class OnTopicClick(val topicPublicId: String, val topicName: String) : DictionaryEvent
    data class OnToggleSaveWord(val word: Word) : DictionaryEvent
    data object OnExploreTopicsClick : DictionaryEvent
    data object LoadTopics : DictionaryEvent

}