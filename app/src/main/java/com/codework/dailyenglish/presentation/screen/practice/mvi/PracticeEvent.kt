package com.codework.dailyenglish.presentation.screen.practice.mvi

sealed interface PracticeEvent {
    data object LoadData : PracticeEvent
    data object OnRetry : PracticeEvent
    data class OnCategoryClick(val id: String) : PracticeEvent

    data object OnFlashcardClick : PracticeEvent
    data class OnAiCardClick(val type: String) : PracticeEvent
    data class OnGameClick(val gameName: String) : PracticeEvent
    data object OnClickLeaderboard : PracticeEvent
}