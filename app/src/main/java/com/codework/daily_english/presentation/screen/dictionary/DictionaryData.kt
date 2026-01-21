package com.codework.daily_english.presentation.screen.dictionary

import com.codework.daily_english.presentation.model.WordItem

data class DictionaryData(
    val query: String = "",
    val searchResults: List<WordItem> = emptyList(), // Danh sách kết quả tìm kiếm
    val recentSearches: List<String> = listOf("Serendipity", "Ephemeral", "Petrichor"), // Lịch sử tìm kiếm
    val wordOfTheDay: WordItem = WordItem(
        word = "Perseverance",
        phoneticUk = "/pəːsɪˈvɪər(ə)ns/", // Anh-Anh
        phoneticUs = "/ˌpərsəˈvirəns/",   // Anh-Mỹ
        type = "noun",
        definition = "Persistence in doing something despite difficulty or delay in achieving success.",
        example = "His perseverance was rewarded: after many rejections, he finally got a job."
    )
)