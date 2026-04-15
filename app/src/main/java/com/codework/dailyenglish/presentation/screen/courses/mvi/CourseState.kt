package com.codework.dailyenglish.presentation.screen.courses.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.presentation.screen.courses.model.CourseUi

data class CourseState(
    val courses: List<CourseUi> = emptyList(),
    val streakValue: Int = 0,
    val expValue: Int = 0,
    val lessonsDone: Int = 0,
    val wordsLearned: Int = 0,
    val dashboardState: BaseUiState<List<CourseUi>> = BaseUiState(),
)