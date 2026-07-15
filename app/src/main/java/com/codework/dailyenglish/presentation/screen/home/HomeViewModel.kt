package com.codework.dailyenglish.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.domain.auth.usecase.GetMyProfileUseCase // BỔ SUNG: Import UseCase lấy Profile từ Server
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

import com.codework.dailyenglish.domain.user_statistic.usecase.StudyTimeManagerUseCase
import com.codework.dailyenglish.presentation.components.AppAdManager

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeeklyStatsUseCase: GetWeeklyStatsUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val studyTimeManager: StudyTimeManagerUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(
    initialState = HomeState()
) {


    override fun onEvent(event: HomeEvent) {
        when(event) {
            HomeEvent.OnEnterHomeScreen -> loadHomeData()
            HomeEvent.OnContinueLesson -> { /* TODO */ }
            HomeEvent.OnDailyQuestClick -> toggleDailyQuest()
            HomeEvent.OnProfileClick -> {}
            HomeEvent.OnRetry -> loadHomeData()
            HomeEvent.OnQuickActionGemini -> sendEffect(HomeEffect.NavigateToAi)
            HomeEvent.OnQuickActionDictionary -> sendEffect(HomeEffect.NavigateToDictionary)
            HomeEvent.OnQuickActionSavedWord -> sendEffect(HomeEffect.NavigateToSavedWord)
            HomeEvent.OnQuickActionTranslate -> sendEffect(HomeEffect.NavigateToTranslate)
            is HomeEvent.OnPremiumCrownClick -> {
                _state.update { it.copy(showSubscriptionPopup = true) }
            }
            is HomeEvent.OnDismissSubscriptionPopup -> {
                _state.update { it.copy(showSubscriptionPopup = false) }
                loadHomeData()
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _state.update {
                it.copy(weeklyStatsState = it.weeklyStatsState.copy(isLoading = true))
            }
            val cachedUser = getUserInfoUseCase()
            val isPremiumCached = cachedUser?.isPremium ?: false

            _state.update {
                it.copy(
                    userName = cachedUser?.fullName ?: "Guest",
                    avatarUrl = cachedUser?.avatarUrl,
                    isPremium = isPremiumCached
                )
            }
            AppAdManager.isPremiumUser = isPremiumCached

            try {
                studyTimeManager.syncIfEnoughTime()
            } catch (e: Exception) {
            }

            launch {
                getMyProfileUseCase()
                    .onSuccess { freshUser ->
                        _state.update {
                            it.copy(
                                userName = freshUser.fullName,
                                avatarUrl = freshUser.avatarUrl,
                                isPremium = freshUser.isPremium
                            )
                        }
                        AppAdManager.isPremiumUser = freshUser.isPremium
                    }
                    .onError { }
            }

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
        _state.update { it.copy(isDailyQuestFinished = !it.isDailyQuestFinished) }
    }
}