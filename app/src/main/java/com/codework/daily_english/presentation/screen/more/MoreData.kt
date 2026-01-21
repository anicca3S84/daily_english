package com.codework.daily_english.presentation.screen.more

data class MoreData(
    val userName: String,
    val email: String,
    val avatarUrl: String,
    val streak: Int,
    val totalXp: Int,
    val wordsLearned: Int,
    val appVersion: String = "1.0.0"
)