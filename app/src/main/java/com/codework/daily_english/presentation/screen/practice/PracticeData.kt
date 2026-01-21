package com.codework.daily_english.presentation.screen.practice

import com.codework.daily_english.presentation.model.PracticeCategory

data class PracticeData(
    val streak: Int = 0,
    val totalPoints: Int = 0,
    val categories: List<PracticeCategory> = emptyList()
)