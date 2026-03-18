package com.codework.daily_english.presentation.screen.dictionary

import com.codework.daily_english.presentation.model.WordItem

// 1. STATE (Đổi DictionaryData thành DictionaryState)
data class DictionaryState(
    val query: String = "",
    val searchResults: List<WordItem> = emptyList(),
    val recentSearches: List<String> = listOf("Serendipity", "Ephemeral", "Petrichor"),
    val wordOfTheDay: WordItem = WordItem(
        word = "Perseverance",
        phoneticUk = "/pəːsɪˈvɪər(ə)ns/",
        phoneticUs = "/ˌpərsəˈvirəns/",
        type = "noun",
        definition = "Persistence in doing something despite difficulty or delay in achieving success.",
        example = "His perseverance was rewarded: after many rejections, he finally got a job."
    )
)

// 2. EVENT
sealed interface DictionaryEvent {
    data class OnSearchQueryChange(val query: String) : DictionaryEvent
    data class OnWordClick(val word: WordItem) : DictionaryEvent
    data class OnPlayAudio(val word: String, val accent: String) : DictionaryEvent // Thêm accent để biết là UK hay US
    data object OnClearSearch : DictionaryEvent

    // Các Event bổ sung cho Dashboard UI của bạn
    data class OnQuickActionClick(val action: String) : DictionaryEvent
    data class OnTopicClick(val topic: String) : DictionaryEvent
    data class OnToggleSaveWord(val word: WordItem) : DictionaryEvent
}

// 3. EFFECT
sealed interface DictionaryEffect {
    data class PlayAudio(val word: String, val accent: String) : DictionaryEffect // Ra lệnh cho UI phát âm thanh
    data class NavigateToFeature(val feature: String) : DictionaryEffect // Điều hướng từ Quick Action
    data class ShowToast(val message: String) : DictionaryEffect
}