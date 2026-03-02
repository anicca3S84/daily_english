package com.codework.daily_english.presentation.screen.grammar

import com.codework.daily_english.presentation.model.GrammarTip
import com.codework.daily_english.presentation.model.GrammarTopic

data class GrammarData(
    val selectedLevel: String = "All", // Filter hiện tại: "All", "A1", "A2", "B1"
    val grammarTopics: List<GrammarTopic> = emptyList(), // Danh sách hiển thị sau khi lọc
    val dailyTip: GrammarTip? = null
)