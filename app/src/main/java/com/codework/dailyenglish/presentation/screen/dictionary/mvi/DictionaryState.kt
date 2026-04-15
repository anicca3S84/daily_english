package com.codework.dailyenglish.presentation.screen.dictionary.mvi

import com.codework.dailyenglish.presentation.screen.dictionary.model.Word

data class DictionaryState(
    val query: String = "",
    val searchResults: List<Word> = emptyList(),
    val recentSearches: List<String> = listOf("Serendipity", "Ephemeral", "Petrichor"),
    val wordOfTheDay: Word = Word(
        word = "Perseverance",
        phoneticUk = "/pəːsɪˈvɪər(ə)ns/",
        phoneticUs = "/ˌpərsəˈvirəns/",
        type = "noun",
        definition = "Persistence in doing something despite difficulty or delay in achieving success.",
        example = "His perseverance was rewarded: after many rejections, he finally got a job."
    )
)