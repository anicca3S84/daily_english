package com.codework.daily_english.presentation.screen.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.TopicItem
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<CourseData>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    // Cache dữ liệu giả
    private val allLevels = listOf("Beginner (A1)", "Elementary (A2)", "Intermediate (B1)", "Advanced (B2)")

    init {
        loadCourseData(allLevels.first())
    }

    fun onEvent(event: CourseEvent) {
        when (event) {
            is CourseEvent.OnLevelSelected -> {
                loadCourseData(event.level)
            }
        }
    }

    private fun loadCourseData(level: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(500) // Giả lập network

            val topics = mockTopicsForLevel(level)
            val data = CourseData(
                selectedLevel = level,
                availableLevels = allLevels,
                topics = topics
            )
            _uiState.value = UiState.Success(data)
        }
    }

    // ... (Hàm mockTopicsForLevel giữ nguyên như cũ) ...
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

