package com.codework.daily_english.presentation.screen.grammar.model

data class GrammarTopic(
    val id: String,
    val title: String,       // VD: "Present Simple"
    val description: String, // VD: "Habits, facts and timetables"
    val level: String,       // "A1", "A2"...
    val progress: Float,     // 0.0 -> 1.0
    val isLocked: Boolean = false,
    val iconUrl: String = "" // URL ảnh icon (nếu có)
)