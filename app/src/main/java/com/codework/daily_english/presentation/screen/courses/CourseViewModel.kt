package com.codework.daily_english.presentation.screen.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.TopicItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<CourseData>(isLoading = true))
    val uiState = _uiState.asStateFlow()

    // Cache dữ liệu giả để demo
    private val allLevels = listOf("Beginner (A1)", "Elementary (A2)", "Intermediate (B1)", "Advanced (B2)")

    init {
        loadCourseData(allLevels.first())
    }

    fun onEvent(event: CourseEvent) {
        when (event) {
            is CourseEvent.OnLevelSelected -> {
                // Khi đổi tab, load lại data theo level mới
                loadCourseData(event.level)
            }
            is CourseEvent.OnTopicClick -> {
                // Handle navigation logic here
                println("Clicked topic: ${event.topicId}")
            }
        }
    }

    private fun loadCourseData(level: String) {
        viewModelScope.launch {
            // Giả lập loading nhẹ để UX mượt hơn
            _uiState.update { it.copy(isLoading = true) }
            delay(500)

            val topics = mockTopicsForLevel(level)

            val data = CourseData(
                selectedLevel = level,
                availableLevels = allLevels,
                topics = topics
            )

            _uiState.update { it.copy(isLoading = false, data = data) }
        }
    }

    // Mock data theo level
    private fun mockTopicsForLevel(level: String): List<TopicItem> {
        return when {
            level.contains("A1") -> listOf(
                TopicItem("1", "Greeting & Introduction", "Learn how to say hello and introduce yourself.", "https://picsum.photos/300/200", 5, 1.0f, false),
                TopicItem("2", "Family & Friends", "Talk about your family members.", "https://picsum.photos/300/201", 8, 0.4f, false),
                TopicItem("3", "Daily Routine", "Describe your everyday activities.", "https://picsum.photos/300/202", 6, 0.0f, true)
            )
            level.contains("A2") -> listOf(
                TopicItem("4", "Travel & Transport", "Booking tickets and asking for directions.", "https://picsum.photos/300/203", 10, 0.0f, false),
                TopicItem("5", "Food & Dining", "Ordering food in a restaurant.", "https://picsum.photos/300/204", 7, 0.0f, true)
            )
            else -> listOf(
                TopicItem("99", "Coming Soon", "Content is updating...", "https://picsum.photos/300/205", 0, 0.0f, true)
            )
        }
    }
}