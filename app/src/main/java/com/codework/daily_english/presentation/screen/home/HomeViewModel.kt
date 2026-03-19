package com.codework.daily_english.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.core.base.BaseState
import com.codework.daily_english.presentation.screen.home.model.DailyStudyTime
import com.codework.daily_english.presentation.screen.home.mvi.HomeEffect
import com.codework.daily_english.presentation.screen.home.mvi.HomeEvent
import com.codework.daily_english.presentation.screen.home.mvi.HomeState
import com.codework.daily_english.presentation.screen.lessons.lesson.mvi.LessonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeState, HomeEvent, HomeEffect>() {

    init {
        loadHomeData()
    }

    override fun onEvent(event: HomeEvent) {
        when(event) {
            HomeEvent.OnContinueLesson -> { println("User clicked Continue Lesson") }
            HomeEvent.OnDailyQuestClick -> toggleDailyQuest()
            HomeEvent.OnProfileClick -> {}
            HomeEvent.OnRetry -> loadHomeData()

            // Dùng sendEffect để báo cho NavGraph chuyển trang
            HomeEvent.OnQuickActionGemini -> sendEffect(HomeEffect.NavigateToAi)
            HomeEvent.OnQuickActionDictionary -> sendEffect(HomeEffect.NavigateToDictionary)
            HomeEvent.OnQuickActionGrammar -> sendEffect(HomeEffect.NavigateToGrammar)
            HomeEvent.OnQuickActionTranslate -> sendEffect(HomeEffect.NavigateToTranslate)
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { BaseState.Loading }

            // Giả lập delay mạng
            delay(2000)

            try {
                // Mock data
                val mockLessons = listOf(
                    LessonState("1", "Tourist trip", imageUrl = "https://images.unsplash.com/photo-1504609773096-104ff2c73ba4",level = "A1-B1",totalProgress = 0.6f, steps = mutableListOf(), description = "" ),
                    LessonState(
                        "2",
                        "Business Meeting",
                        imageUrl = "https://images.unsplash.com/photo-1517048676732-d65bc937f952",
                        level = "A1-B1",
                        totalProgress = 0.6f,
                        steps = mutableListOf(),
                        description = ""
                    ),
                    LessonState("3", "Airport Check-in", imageUrl ="https://images.unsplash.com/photo-1436491865332-7a61a109cc05", level = "A1-B1", totalProgress = 0.8f, steps = mutableListOf(), description = "")
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

                // Dùng HomeState thay cho HomeData
                val data = HomeState(
                    userName = "CodeWork",
                    streakDay = 15,
                    continueLearningList = mockLessons,
                    dailyQuest = "Hoàn thành 1 bài hội thoại AI",
                    isDailyQuestFinished = false,
                    weeklyStats = weeklyStats
                )

                _uiState.update { BaseState.Success(data) }

            } catch (e: Exception) {
                _uiState.update { BaseState.Error(e.message ?: "Đã có lỗi xảy ra") }
            }
        }
    }

    private fun toggleDailyQuest() {
        val currentState = _uiState.value
        if (currentState is BaseState.Success) {
            val currentData = currentState.data
            val newData = currentData.copy(
                isDailyQuestFinished = !currentData.isDailyQuestFinished
            )
            _uiState.update { BaseState.Success(newData) }
        }
    }
}