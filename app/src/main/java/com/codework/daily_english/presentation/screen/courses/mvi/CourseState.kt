package com.codework.daily_english.presentation.screen.courses.mvi

import com.codework.daily_english.presentation.screen.courses.model.TopicItem

data class CourseState(
    val selectedLevel: String,
    val availableLevels: List<String>,
    val topics: List<TopicItem>
)