package com.codework.dailyenglish.domain.leaderboard.model

data class LeaderBoardProfile(
    val userId: String,
    val fullName: String,
    val avatarUrl: String,
    val score: Int,
    val rank: Int,
    val isCurrentUser: Boolean
)