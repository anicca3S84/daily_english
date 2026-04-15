package com.codework.dailyenglish.presentation.screen.practice.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface PracticeEffect: BaseEffect{
    data class NavigateToCategory(val id: String) : PracticeEffect
    data object NavigateToFlashcard : PracticeEffect
    data class NavigateToAiSpeaking(val type: String) : PracticeEffect
    data class NavigateToGame(val gameName: String) : PracticeEffect
    data class ShowToast(val message: String) : PracticeEffect
}