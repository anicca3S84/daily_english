package com.codework.dailyenglish.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.presentation.screen.home.model.DailyStudyTime
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeEffect
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeEvent
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeState
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeState, HomeEvent, HomeEffect>(
    initialState = HomeState()
) {

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
            try {
            } catch (e: Exception) {
            }
        }
    }

    private fun toggleDailyQuest() {
//        val currentState = _state.value
//        if (currentState is BaseState.Success) {
//            val currentData = currentState.data
//            val newData = currentData.copy(
//                isDailyQuestFinished = !currentData.isDailyQuestFinished
//            )
//            _state.update { BaseState.Success(newData) }
//        }
    }
}