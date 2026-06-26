package com.codework.dailyenglish.domain.leaderboard.model

data class LeaderBoardWrapper(
    val leaderboardList: List<LeaderBoardProfile>,
    val myRank: LeaderBoardProfile
)