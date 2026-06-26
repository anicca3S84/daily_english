package com.codework.dailyenglish.presentation.screen.leaderboard.mvi

sealed interface LeaderboardEvent {
    object LoadLeaderboard : LeaderboardEvent
}