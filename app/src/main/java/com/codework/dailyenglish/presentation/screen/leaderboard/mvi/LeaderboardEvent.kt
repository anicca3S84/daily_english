package com.codework.dailyenglish.presentation.screen.leaderboard.mvi

sealed interface LeaderboardEvent {
    data class OnTabChanged(val tab: String) : LeaderboardEvent
    data class OnTimeFilterChanged(val filter: String) : LeaderboardEvent
    object LoadLeaderboard : LeaderboardEvent
}