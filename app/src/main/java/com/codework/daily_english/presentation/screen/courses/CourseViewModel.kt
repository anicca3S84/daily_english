package com.codework.daily_english.presentation.screen.courses

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.TopicItem
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
// 1. CHỈ ĐỊNH KIỂU DỮ LIỆU T LÀ CourseData
class CourseViewModel @Inject constructor() : BaseViewModel<CourseData>() {

    private val allLevels = listOf("Beginner (A1)", "Elementary (A2)", "Intermediate (B1)", "Advanced (B2)")

    init {
        loadCourseData(allLevels.first())
    }

    // 2. ĐÃ XÓA hàm navigateTo() VÀ CÁC BIẾN Channel/StateFlow VÌ ĐÃ NẰM TRONG BaseViewModel

    fun onEvent(event: CourseEvent) {
        when (event) {
            is CourseEvent.OnLevelSelected -> {
                loadCourseData(event.level)
            }
            is CourseEvent.OnNavigateToLesson -> {
                // Gọi thẳng hàm navigateTo() từ lớp cha cực kỳ gọn gàng
                navigateTo(Route.LessonScreen)
            }
        }
    }

    private fun loadCourseData(level: String) {
        viewModelScope.launch {
            // Có thể dùng thẳng _uiState của BaseViewModel
            _uiState.update {
                UiState.Loading
            }

            delay(500) // Giả lập network

            val topics = mockTopicsForLevel(level)
            val data = CourseData(
                selectedLevel = level,
                availableLevels = allLevels,
                topics = topics
            )

            _uiState.update {
                UiState.Success(data)
            }
        }
    }

    // ... (Hàm mockTopicsForLevel giữ nguyên y như cũ) ...
    private fun mockTopicsForLevel(level: String): List<TopicItem> {
        return when {
            level.contains("A1") -> listOf(
                TopicItem("1", "Greeting & Introduction", "Learn how to say hello and introduce yourself.", "https://picsum.photos/300/200", 5, 1.0f, false),
                TopicItem("1", "Family & Friends", "Learn how to say hello and introduce yourself.", "https://picsum.photos/300/201", 5, 1.0f, false),
                TopicItem("1", "Greeting & Introduction", "Learn how to say hello and introduce yourself.", "https://picsum.photos/300/200", 5, 0.8f, false),
                TopicItem("2", "Family & Friends", "Talk about your family members.", "https://picsum.photos/300/201", 8, 0.4f, false),
                TopicItem("2", "Family & Friends", "Talk about your family members.", "https://picsum.photos/300/201", 8, 0.4f, false),
                TopicItem("2", "Family & Friends", "Talk about your family members.", "https://picsum.photos/300/201", 8, 0.4f, false),
                TopicItem("2", "Family & Friends", "Talk about your family members.", "https://picsum.photos/300/201", 8, 0.4f, false),
                TopicItem("2", "Family & Friends", "Talk about your family members.", "https://picsum.photos/300/201", 8, 0.4f, false),
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