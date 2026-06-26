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
            LeaderboardEvent.LoadLeaderboard -> fetchLeaderboard()
        }
    }

    private fun fetchLeaderboard() {
        _state.update { it.copy(leaderboardState = it.leaderboardState.copy(isLoading = true, error = null)) }
        viewModelScope.launch {
            getLeaderBoardUseCase(page = 0, size = 50)
                .onSuccess { wrapper ->
                    _state.update { it.copy(leaderboardState = it.leaderboardState.copy(isLoading = false, data = wrapper)) }
                }
                .onError { err ->
                    _state.update { it.copy(leaderboardState = it.leaderboardState.copy(isLoading = false, error = err.asUiText())) }
                }
        }
    }
}