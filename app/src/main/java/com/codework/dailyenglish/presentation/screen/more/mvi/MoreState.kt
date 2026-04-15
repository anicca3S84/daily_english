package com.codework.dailyenglish.presentation.screen.more.mvi

data class MoreState(
    val userName: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val streak: Int = 0,
    val totalXp: Int = 0,
    val wordsLearned: Int = 0,
    val appVersion: String = "1.0.0"
)