package com.codework.daily_english.presentation.screen.practice.mvi

sealed interface PracticeEffect {
    data class NavigateToCategory(val id: String) : PracticeEffect
    data object NavigateToFlashcard : PracticeEffect
    data class NavigateToAiSpeaking(val type: String) : PracticeEffect
    data class NavigateToGame(val gameName: String) : PracticeEffect
    data class ShowToast(val message: String) : PracticeEffect
}