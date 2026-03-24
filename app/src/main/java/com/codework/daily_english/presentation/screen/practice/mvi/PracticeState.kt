package com.codework.daily_english.presentation.screen.practice.mvi

import com.codework.daily_english.presentation.screen.practice.model.PracticeCategory

data class PracticeState(
    val streak: Int = 0,
    val totalPoints: Int = 0,
    val categories: List<PracticeCategory> = emptyList(),
    val error: String? = null,
)