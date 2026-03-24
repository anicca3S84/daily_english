package com.codework.daily_english.presentation.screen.courses.mvi

import com.codework.daily_english.presentation.screen.courses.model.TopicItem

data class CourseState(
    val selectedLevel: String = "Beginner",
    val availableLevels: List<String> = emptyList(),
    val topics: List<TopicItem> = emptyList(),
    val isLoading: Boolean = false, // Nên thêm cái này để BaseScreen dùng
    val error: String? = null
)