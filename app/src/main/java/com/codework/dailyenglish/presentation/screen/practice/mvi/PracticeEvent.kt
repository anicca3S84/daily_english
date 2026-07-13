package com.codework.dailyenglish.presentation.screen.practice.mvi

sealed interface PracticeEvent {
    data object LoadData : PracticeEvent
    data object OnRetry : PracticeEvent
    data class OnAiTopicClick(val id: String, val name: String) : PracticeEvent
    data class OnGameClick(val gameName: String) : PracticeEvent
    object OnExploreMoreAiTopics : PracticeEvent
}