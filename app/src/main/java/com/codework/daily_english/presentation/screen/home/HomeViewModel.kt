package com.codework.daily_english.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.DailyStudyTime
import com.codework.daily_english.presentation.navigation.Route
import com.codework.daily_english.presentation.screen.lessons.LessonData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _navigationChannel = Channel<Route>()
    val navigationChannel = _navigationChannel.receiveAsFlow()
    private val _uiState = MutableStateFlow<UiState<HomeData>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeData>> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            HomeEvent.OnContinueLesson -> { println("User clicked Continue Lesson") }
            HomeEvent.OnDailyQuestClick -> toggleDailyQuest()
            HomeEvent.OnProfileClick -> {}
            HomeEvent.OnRetry -> loadHomeData()

            HomeEvent.OnQuickActionGemini -> {
                // Gửi event vào channel
                // check logic điều kiện nếu cần
                viewModelScope.launch {
                    _navigationChannel.send(Route.AIScreen)
                }
            }

            HomeEvent.OnQuickActionDictionary -> {
                viewModelScope.launch {
                    _navigationChannel.send(Route.DictionaryScreen)
                }
            }
            HomeEvent.OnQuickActionGrammar -> {}
            HomeEvent.OnQuickActionTranslate -> {
                viewModelScope.launch {
                    _navigationChannel.send(Route.TranslateScreen)
                }
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            // SỬA 2: Gán trực tiếp trạng thái Loading
            _uiState.value = UiState.Loading

            // Giả lập delay mạng
            delay(2000)

            try {
                // Mock data
                val mockLessons = listOf(
                    LessonData("1", "Tourist trip", imageUrl = "https://images.unsplash.com/photo-1504609773096-104ff2c73ba4",level = "A1-B1",totalProgress = 0.6f, steps = mutableListOf(), description = "" ),
                    LessonData("2", "Business Meeting", imageUrl ="https://images.unsplash.com/photo-1517048676732-d65bc937f952", level = "A1-B1", totalProgress = 0.6f, steps = mutableListOf(), description = ""  ),
                    LessonData("3", "Airport Check-in", imageUrl ="https://images.unsplash.com/photo-1436491865332-7a61a109cc05", level = "A1-B1", totalProgress = 0.8f, steps = mutableListOf(), description = "")
                )

                val weeklyStats = listOf(
                    DailyStudyTime("Mon", 20),
                    DailyStudyTime("Tue", 5),
                    DailyStudyTime("Wed", 10),
                    DailyStudyTime("Thu", 50),
                    DailyStudyTime("Fri", 30, isToday = true),
                    DailyStudyTime("Sat", 0),
                    DailyStudyTime("Sun", 0)
                )

                val data = HomeData(
                    userName = "CodeWork",
                    streakDay = 15,
                    continueLearningList = mockLessons,
                    dailyQuest = "Hoàn thành 1 bài hội thoại AI",
                    isDailyQuestFinished = false,
                    weeklyStats = weeklyStats
                )

                // SỬA 3: Gán trạng thái Success chứa data
                _uiState.value = UiState.Success(data)

            } catch (e: Exception) {
                // SỬA 4: Gán trạng thái Error
                _uiState.value = UiState.Error(e.message ?: "Đã có lỗi xảy ra")
            }
        }
    }

    private fun toggleDailyQuest() {
        // SỬA 5: Logic cập nhật khi dùng Sealed Class
        // Phải kiểm tra xem state hiện tại có phải là Success không thì mới lấy được data cũ
        val currentState = _uiState.value

        if (currentState is UiState.Success) {
            val currentData = currentState.data

            // Copy data cũ và thay đổi thuộc tính
            val newData = currentData.copy(
                isDailyQuestFinished = !currentData.isDailyQuestFinished
            )

            // Emit trạng thái Success mới với data mới
            _uiState.value = UiState.Success(newData)
        }
    }
}