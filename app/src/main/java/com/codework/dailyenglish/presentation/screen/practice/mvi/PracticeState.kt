package com.codework.dailyenglish.presentation.screen.practice.mvi

import com.codework.dailyenglish.presentation.screen.practice.model.PracticeCategory

data class PracticeState(
    val streak: Int = 0,
    val totalPoints: Int = 0,
    val categories: List<PracticeCategory> = emptyList(),
    val error: String? = null,
)