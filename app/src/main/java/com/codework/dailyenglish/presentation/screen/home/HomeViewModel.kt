package com.codework.dailyenglish.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.user.usecase.GetUserInfoUseCase
import com.codework.dailyenglish.domain.user_statistic.usecase.GetWeeklyStatsUseCase
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeEffect
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeEvent
import com.codework.dailyenglish.presentation.screen.home.mvi.HomeState
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeeklyStatsUseCase: GetWeeklyStatsUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(
    initialState = HomeState()
) {

    init {
        loadHomeData()
    }

    override fun onEvent(event: HomeEvent) {
        when(event) {
            HomeEvent.OnContinueLesson -> { /* TODO */ }
            HomeEvent.OnDailyQuestClick -> toggleDailyQuest()
            HomeEvent.OnProfileClick -> {}
            HomeEvent.OnRetry -> loadHomeData()
            HomeEvent.OnQuickActionGemini -> sendEffect(HomeEffect.NavigateToAi)
            HomeEvent.OnQuickActionDictionary -> sendEffect(HomeEffect.NavigateToDictionary)
            HomeEvent.OnQuickActionSavedWord -> sendEffect(HomeEffect.NavigateToSavedWord)
            HomeEvent.OnQuickActionTranslate -> sendEffect(HomeEffect.NavigateToTranslate)
            // Event lưu giờ học đã được MainActivity xử lý trực tiếp via UseCase,
            // không cần đưa vào đây nữa để ViewModel được gọn nhẹ
            else -> {}
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _state.update {
                it.copy(weeklyStatsState = it.weeklyStatsState.copy(isLoading = true))
            }
            val user = getUserInfoUseCase()
            _state.update { it.copy(userName = user?.fullName ?: "Guest", avatarUrl = user?.avatarUrl) }

            getWeeklyStatsUseCase()
                .onSuccess { stats ->
                    _state.update { state ->
                        state.copy(
                            weeklyStatsState = state.weeklyStatsState.copy(
                                data = stats,
                                isLoading = false
                            )
                        )
                    }
                }
                .onError { appError ->
                    _state.update { state ->
                        state.copy(
                            weeklyStatsState = state.weeklyStatsState.copy(
                                isLoading = false,
                                error = appError.asUiText()
                            )
                        )
                    }
                }
        }
    }

    private fun toggleDailyQuest() {
        // Cập nhật state bất biến bằng .copy()
        _state.update { it.copy(isDailyQuestFinished = !it.isDailyQuestFinished) }
    }
}