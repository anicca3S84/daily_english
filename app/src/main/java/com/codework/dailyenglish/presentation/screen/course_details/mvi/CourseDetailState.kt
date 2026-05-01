package com.codework.dailyenglish.presentation.screen.course_details.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.core.utils.Constants
import com.codework.dailyenglish.presentation.screen.course_details.model.ModuleUi
import com.codework.dailyenglish.presentation.theme.CourseTheme
import com.codework.dailyenglish.presentation.theme.provider.CourseThemeProvider

data class CourseDetailState(
    val courseTitle: String = "",
    val duration: String = "",
    val cefr: String = "",
    val introduction: String = "",
    val overallProgress: Float = 0f,
    val totalLessons: Int = 0,
    val completedLessons: Int = 0,
    val modulesState: BaseUiState<List<ModuleUi>> = BaseUiState(),
    val courseTheme: CourseTheme = CourseThemeProvider.getThemeByCode(Constants.LEVEL_ROOKIE),
)