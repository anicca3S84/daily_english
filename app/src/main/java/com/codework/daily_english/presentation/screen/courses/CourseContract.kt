package com.codework.daily_english.presentation.screen.courses

import com.codework.daily_english.presentation.model.TopicItem

// 1. STATE: Không cần isLoading hay error nữa!
data class CourseState(
    val selectedLevel: String,
    val availableLevels: List<String>,
    val topics: List<TopicItem>
)

// 2. EVENT
sealed interface CourseEvent {
    data class OnLevelSelected(val level: String) : CourseEvent
    data class OnNavigateToLesson(val lessonId: String): CourseEvent
}

// 3. EFFECT
sealed interface CourseEffect {
    data class NavigateToLesson(val lessonId: String) : CourseEffect
}