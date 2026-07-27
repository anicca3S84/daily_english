package com.codework.dailyenglish.presentation.screen.leaderboard

import androidx.lifecycle.viewModelScope
import com.codework.dailyenglish.core.base.BaseViewModel
import com.codework.dailyenglish.core.network.onSuccess
import com.codework.dailyenglish.core.network.onError
import com.codework.dailyenglish.domain.leaderboard.usecase.GetLeaderBoardUseCase
import com.codework.dailyenglish.presentation.screen.leaderboard.mvi.*
import com.codework.dailyenglish.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val getLeaderBoardUseCase: GetLeaderBoardUseCase
) : BaseViewModel<LeaderboardState, LeaderboardEvent, LeaderboardEffect>(LeaderboardState()) {

    init {
        fetchLeaderboard()
    }

    override fun onEvent(event: LeaderboardEvent) {
        when (event) {
            is LeaderboardEvent.OnTabChanged -> {
                if (_state.value.selectedTab != event.tab) {
                    _state.update { it.copy(selectedTab = event.tab) }
                    fetchLeaderboard()
                }
            }
            is LeaderboardEvent.OnTimeFilterChanged -> {
                if (_state.value.selectedTimeFilter != event.filter) {
                    _state.update { it.copy(selectedTimeFilter = event.filter) }
                    fetchLeaderboard()
                }
            }
            LeaderboardEvent.LoadLeaderboard -> fetchLeaderboard()
        }
    }

    private fun fetchLeaderboard() {
        _state.update {
            it.copy(
                leaderboardState = it.leaderboardState.copy(
                    isLoading = true,
                    error = null,
                    data = null
                )
            )
        }

        val type = _state.value.selectedTab
        val timeFilter = if (type == "STREAK") {
            "ALL"
        } else {
            when (_state.value.selectedTimeFilter.uppercase()) {
                "THIS WEEK", "THIS_WEEK" -> "THIS_WEEK"
                "THIS MONTH", "THIS_MONTH" -> "THIS_MONTH"
                else -> "ALL"
            }
        }

        viewModelScope.launch {
            getLeaderBoardUseCase(type = type, timeFilter = timeFilter, page = 0, size = 50)
                .onSuccess { wrapper ->
                    _state.update { it.copy(leaderboardState = it.leaderboardState.copy(isLoading = false, data = wrapper)) }
                }
                .onError { err ->
                    _state.update { it.copy(leaderboardState = it.leaderboardState.copy(isLoading = false, error = err.asUiText())) }
                }
        }
    }
}