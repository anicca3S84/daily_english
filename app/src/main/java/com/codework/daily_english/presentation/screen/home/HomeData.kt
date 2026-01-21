package com.codework.daily_english.presentation.screen.home

import com.codework.daily_english.presentation.model.DailyStudyTime
import com.codework.daily_english.presentation.screen.lessons.LessonData

data class HomeData(
    val userName: String,
    val streakDay: Int,
    // THAY ĐỔI: Chuyển thành List
    val continueLearningList: List<LessonData>,
    val dailyQuest: String,
    val isDailyQuestFinished: Boolean,
    val weeklyStats: List<DailyStudyTime>
)