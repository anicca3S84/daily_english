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

            // Mock List bài đang học
            val learningList = listOf(
                LessonItem(
                    id = "1",
                    title = "Unit 3: Shopping",
                    progress = 0.45f,
                    topicIcon = "shopping"
                ),
                LessonItem(id = "2", title = "Unit 5: Travel", progress = 0.8f, topicIcon = "travel"),
                LessonItem(id = "3", title = "Unit 1: Intro", progress = 0.1f, topicIcon = "intro")
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
                continueLearningList = learningList, // Truyền list vào đây
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