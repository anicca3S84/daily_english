package com.codework.dailyenglish.presentation.screen.home.mvi

import com.codework.dailyenglish.presentation.screen.home.model.DailyStudyTime
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonState

data class HomeState(
    val userName: String = "",
    val streakDay: Int = 0,
    val continueLearningList: List<LessonState> = emptyList(),
    val dailyQuest: String = "",
    val isDailyQuestFinished: Boolean = false,
    val weeklyStats: List<DailyStudyTime> = emptyList()
)