package com.codework.daily_english.presentation.screen.course_details.mvi

import com.codework.daily_english.core.base.BaseUiState
import com.codework.daily_english.core.utils.Constants
import com.codework.daily_english.presentation.screen.course_details.model.ModuleUi
import com.codework.daily_english.presentation.theme.CourseTheme
import com.codework.daily_english.presentation.theme.provider.CourseThemeProvider

data class CourseDetailState(
    val courseTitle: String = "",
    val overallProgress: Float = 0f,
    val totalLessons: Int = 0,
    val completedLessons: Int = 0,
    val modulesState: BaseUiState<List<ModuleUi>> = BaseUiState(),
    val courseTheme: CourseTheme = CourseThemeProvider.getThemeByCode(Constants.LEVEL_ROOKIE),
)