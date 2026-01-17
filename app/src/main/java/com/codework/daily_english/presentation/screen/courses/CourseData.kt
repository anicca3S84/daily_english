package com.codework.daily_english.presentation.screen.courses

import com.codework.daily_english.presentation.model.TopicItem

data class CourseData(
    val selectedLevel: String, // Level đang chọn: "A1", "A2", "B1"...
    val availableLevels: List<String>, // Danh sách các tab: ["A1", "A2", "B1", "B2"]
    val topics: List<TopicItem> // Danh sách chủ đề tương ứng với Level
)