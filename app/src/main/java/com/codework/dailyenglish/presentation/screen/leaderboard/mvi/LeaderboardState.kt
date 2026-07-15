package com.codework.dailyenglish.presentation.screen.leaderboard.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.leaderboard.model.LeaderBoardWrapper

data class LeaderboardState(
    val leaderboardState: BaseUiState<LeaderBoardWrapper> = BaseUiState(),
    val selectedTab: String = "EXP",
    val selectedTimeFilter: String = "All"
)