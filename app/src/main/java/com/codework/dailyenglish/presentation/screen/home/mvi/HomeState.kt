package com.codework.dailyenglish.presentation.screen.home.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.presentation.screen.home.model.DailyStudyTime

data class HomeState(
    val userName: String = "",
    val avatarUrl: String? = null,
    val streakDay: Int = 0,
    val dailyQuest: String = "",
    val isDailyQuestFinished: Boolean = false,
    val weeklyStatsState: BaseUiState<List<DailyStudyTime>> = BaseUiState(),
    val isPremium: Boolean = false,
)