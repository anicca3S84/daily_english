package com.codework.daily_english.presentation.screen.practice

import com.codework.daily_english.presentation.model.PracticeCategory

// 1. STATE
data class PracticeState(
    val streak: Int = 0,
    val totalPoints: Int = 0,
    val categories: List<PracticeCategory> = emptyList()
)

// 2. EVENT
sealed interface PracticeEvent {
    data object LoadData : PracticeEvent
    data object OnRetry : PracticeEvent
    data class OnCategoryClick(val id: String) : PracticeEvent

    // Thêm event cho các UI item của bạn
    data object OnFlashcardClick : PracticeEvent
    data class OnAiCardClick(val type: String) : PracticeEvent
    data class OnGameClick(val gameName: String) : PracticeEvent
}

// 3. EFFECT
sealed interface PracticeEffect {
    data class NavigateToCategory(val id: String) : PracticeEffect
    data object NavigateToFlashcard : PracticeEffect
    data class NavigateToAiSpeaking(val type: String) : PracticeEffect
    data class NavigateToGame(val gameName: String) : PracticeEffect
    data class ShowToast(val message: String) : PracticeEffect
}