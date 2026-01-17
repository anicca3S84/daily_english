package com.codework.daily_english.presentation.screen.home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.DailyStudyTime
import com.codework.daily_english.presentation.screen.home.HomeData
import com.codework.daily_english.presentation.model.LessonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    // Sử dụng UiState<HomeData>
    private val _uiState = MutableStateFlow<UiState<HomeData>>(UiState(isLoading = true))
    val uiState: StateFlow<UiState<HomeData>> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    // Hàm nhận Event từ UI
    fun onEvent(event: HomeEvent) {
        when(event) {
            HomeEvent.OnContinueLesson -> {
                println("User clicked Continue Lesson")
            }
            HomeEvent.OnDailyQuestClick -> toggleDailyQuest()
            HomeEvent.OnProfileClick -> {}
            HomeEvent.OnRetry -> loadHomeData()
            HomeEvent.OnQuickActionGemini -> {}
            HomeEvent.OnQuickActionDictionary -> {}
            HomeEvent.OnQuickActionGrammar -> {}
            HomeEvent.OnQuickActionTranslate -> {}

        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(2000)

            // Mock data trong HomeViewModel hoặc HomeScreen
            val mockLessons = listOf(
                LessonItem(
                    id = "1",
                    title = "Tourist trip",
                    imageUrl = "https://images.unsplash.com/photo-1504609773096-104ff2c73ba4",
                    lessonNum = 3,
                    level = "A1-B1",
                    progress = 0.6f
                ),
                LessonItem(
                    id = "2",
                    title = "Business Meeting",
                    imageUrl = "https://images.unsplash.com/photo-1517048676732-d65bc937f952",
                    lessonNum = 5,
                    level = "B2",
                    progress = 0.2f
                ),
                LessonItem(
                    id = "2",
                    title = "Business Meeting",
                    imageUrl = "https://images.unsplash.com/photo-1517048676732-d65bc937f952",
                    lessonNum = 5,
                    level = "B2",
                    progress = 0.2f
                ),

                LessonItem(
                    id = "1",
                    title = "Tourist trip",
                    imageUrl = "https://images.unsplash.com/photo-1504609773096-104ff2c73ba4",
                    lessonNum = 3,
                    level = "A1-B1",
                    progress = 0.6f
                ),

                )


            val weeklyStats = listOf(
                DailyStudyTime("Mon", 20),
                DailyStudyTime("Tue", 5),
                DailyStudyTime("Wed", 10),
                DailyStudyTime("Thu", 50),
                DailyStudyTime("Fri", 30, isToday = true), // Hôm nay
                DailyStudyTime("Sat", 0),
                DailyStudyTime("Sun", 0)
            )

            val data = HomeData(
                userName = "CodeWork",
                streakDay = 15,
                continueLearningList = mockLessons, // Truyền list vào đây
                dailyQuest = "Hoàn thành 1 bài hội thoại AI",
                isDailyQuestFinished = false,
                weeklyStats = weeklyStats
            )

            _uiState.update { it.copy(isLoading = false, data = data) }
        }
    }

    private fun toggleDailyQuest() {
        // Cập nhật optimistic UI (Hiển thị tick xanh ngay lập tức)
        val currentData = _uiState.value.data ?: return

        val newData = currentData.copy(
            isDailyQuestFinished = !currentData.isDailyQuestFinished
        )
        _uiState.update { it.copy(data = newData) }
    }
}