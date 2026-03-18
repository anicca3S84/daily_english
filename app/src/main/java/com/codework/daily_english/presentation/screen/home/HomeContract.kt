package com.codework.daily_english.presentation.screen.home

import com.codework.daily_english.presentation.model.DailyStudyTime
import com.codework.daily_english.presentation.screen.lessons.LessonState

data class HomeState(
    val userName: String = "",
    val streakDay: Int = 0,
    val continueLearningList: List<LessonState> = emptyList(),
    val dailyQuest: String = "",
    val isDailyQuestFinished: Boolean = false,
    val weeklyStats: List<DailyStudyTime> = emptyList()
)

sealed interface HomeEvent {
    data object OnContinueLesson : HomeEvent
    data object OnDailyQuestClick : HomeEvent
    data object OnProfileClick : HomeEvent
    data object OnRetry : HomeEvent
    data object OnQuickActionGemini : HomeEvent
    data object OnQuickActionDictionary : HomeEvent
    data object OnQuickActionGrammar : HomeEvent
    data object OnQuickActionTranslate : HomeEvent
}

sealed interface HomeEffect {
    data object NavigateToAi : HomeEffect
    data object NavigateToDictionary : HomeEffect
    data object NavigateToGrammar : HomeEffect
    data object NavigateToTranslate : HomeEffect
}