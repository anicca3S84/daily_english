package com.codework.dailyenglish.presentation.screen.dictionary.mvi

import com.codework.dailyenglish.domain.dictionary.model.RecentWord
import com.codework.dailyenglish.domain.dictionary.model.Word

sealed interface DictionaryEvent {
    data class OnSearchQueryChange(val query: String) : DictionaryEvent
    data class OnWordClick(val word: Word) : DictionaryEvent
    data class OnPlayAudio(val word: Word, val accent: String) : DictionaryEvent
    data object OnClearSearch : DictionaryEvent
    data class OnQuickActionClick(val action: String) : DictionaryEvent
    data class OnTopicClick(val topicId: String, val topicName: String) : DictionaryEvent
    data class OnToggleSaveWord(val word: Word) : DictionaryEvent
    data object OnExploreTopicsClick : DictionaryEvent
    data object LoadTopics : DictionaryEvent
    data class OnRecentSearchClick(val recentWord: RecentWord) : DictionaryEvent
    data class OnDeleteRecentSearch(val wordId: String) : DictionaryEvent // Xóa 1 từ lịch sử
    data object OnClearRecentSearches : DictionaryEvent // Xóa toàn bộ lịch sử
    data class OnWordOfTheDayClick(val word: String) : DictionaryEvent
}