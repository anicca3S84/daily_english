package com.codework.dailyenglish.presentation.screen.practice.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface PracticeEffect: BaseEffect{
    data class NavigateToAiSpeaking(val topicId: String, val topicName: String) : PracticeEffect
    data class NavigateToGame(val gameName: String) : PracticeEffect
    data class NavigateToTopicSelection(val origin: String) : PracticeEffect}